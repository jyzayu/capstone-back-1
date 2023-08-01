package capstone.be.global.jwt;

import capstone.be.domain.user.service.CustomUserDetailsService;
import capstone.be.global.advice.exception.security.CAuthenticationEntryPointException;
import capstone.be.global.advice.exception.security.CJwtException;
import capstone.be.global.advice.exception.security.CLogoutTokenException;
import capstone.be.global.dto.jwt.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64UrlCodec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.lang.String;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private String ROLES = "roles";
    private final Long accessTokenValidMillisecond =  24 * 60 * 60 * 1000L; // 24 hour
    private final Long refreshTokenValidMillisecond = 30 * 24 * 60 * 60 * 1000L; // 30 day
    private final CustomUserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;


    @PostConstruct
    protected void init() {
        // 암호화
        secretKey = Base64UrlCodec.BASE64URL.encode(secretKey.getBytes(StandardCharsets.UTF_8));
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 생성
// Jwt 생성
    public TokenDto createTokenDto(Long userPk, List<String> roles) {

        // Claims 에 user 구분을 위한 User pk 및 authorities 목록 삽입
        Claims claims = Jwts.claims().setSubject(String.valueOf(userPk));
        claims.put(ROLES, roles);

        // 생성날짜, 만료날짜를 위한 Date
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    //JWT 토큰의 만료시간
    public Long getExpiration(String token){
        Date expiration = Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getExpiration();
        long now = new Date().getTime();
        return expiration.getTime() - now;
    }

    // Jwt 로 인증정보를 조회
    public Authentication getAuthentication(String token) {

        // Jwt 에서 claims 추출
        Claims claims = parseClaims(token);

        // 권한 정보가 없음
        if (claims.get(ROLES) == null) {
            throw new CAuthenticationEntryPointException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getSubjects(String token) {
        Claims claims = parseClaims(token);

        return claims.getSubject();
    }

    // Jwt 토큰 복호화해서 가져오기
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // HTTP Request 의 Header 에서 Token Parsing -> "X-AUTH-TOKEN: jwt"
    public String resolveToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

    // jwt 의 유효성 및 만료일자 확인
    public boolean validationToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
                //Redis에 있는 엑세스 토큰인 경우 로그아웃 처리된 엑세스 토큰임.
                String blackToken = redisTemplate.opsForValue().get(token);
                if(StringUtils.hasText(blackToken))
                    throw new CLogoutTokenException("로그아웃 처리된 엑세스 토큰입니다.");
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 Jwt 서명입니다.");
            throw new CJwtException();
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
            throw new CJwtException();
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰입니다.");
            throw new CJwtException();
        } catch (IllegalArgumentException e) {
            log.error("잘못된 토큰입니다.");
            throw new CJwtException();
        } catch (CLogoutTokenException e){
            log.error("로그아웃 토근입니다.");
            throw new CJwtException();
        } catch (SignatureException e) {
            log.error("시그니처 에러");
            throw new CJwtException();
        }
    }

}