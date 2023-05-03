package capstone.be.global.jwt;

import capstone.be.global.advice.exception.CJwtException;
import capstone.be.global.advice.exception.CLogoutTokenException;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Todo: JWT filter exception 처리 에러 미 해결
@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final Gson gson;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (CJwtException e) {
            sendResponse((HttpServletResponse)response, "AUTH_008", 401);
        }
    }

    private void sendResponse(HttpServletResponse response, String code, int statusCode) throws IOException {

        ErrorResponse result = new ErrorResponse(code);

        String res = gson.toJson(result);
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }

    @Data
    public static class ErrorResponse{
        private final String code;
    }

}
