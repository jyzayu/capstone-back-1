package capstone.be.global.config.security;

import capstone.be.global.jwt.JwtAuthenticationFilter;
import capstone.be.global.jwt.JwtExceptionFilter;
import capstone.be.global.jwt.JwtProvider;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    // Authentication 객체에 로그인 완료후 인증유저 정보 UserDetails를 구현한 (User? principal) 객체가 존재
    // UserDetailsService 를 상속받는 PrincipalDetailsService 구현 -
    // (UsernamePasswordAuthenticationFilter 를 상속받는 JwtAuthenticationFilter) 에서 인증을 위해 호출하는 메서드
    //  loadUserByUsername(String username) { memberRepository.findById(username).orElseThrow(() -> new Exception))
    // 이 필터를 시큐리티필터에 추가해 Authentication을 검증한다.

    // session(context) -> Authentication -> UserDetails(Principal)
    // 인증완료 유저정보로 회원가입및 토큰생성

    //요청 처리위해 위임시키는 Servlet 도달하기 전에 CorsFilter AuthenticationFitler를 거친다.
    private final JwtProvider jwtProvider;
    private final RedisTemplate redisTemplate;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final Gson gson;



    //CORS 차단 해제
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");  // 모든 Origin에 응답 허용
        configuration.addAllowedHeader("*");         // 모든 HTTP Header에 응답 허용
        configuration.addAllowedMethod("*");         // 모든 HTTP Method에 응답 허용
        configuration.setAllowCredentials(true);     // json 서버응답을 js에서 처리할수있게 해줌

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()  //session 비활성화
                //  formLogin, csrf, headers, http-basic, rememberMe, logout filter 비활성화
                .csrf().disable().formLogin().disable().httpBasic().disable().headers().disable().rememberMe().disable().logout().disable()
//               CORS설정
                .cors().configurationSource(corsConfigurationSource()).and()

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .mvcMatchers(HttpMethod.POST, "/api/**").permitAll()
                        .mvcMatchers(HttpMethod.GET, "/exception/**", "/login/oauth2/code/kakao", "/api/redisTest").permitAll()
                        .anyRequest().authenticated())

                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()

//               JWT 검증 필터
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(gson), JwtAuthenticationFilter.class)
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
