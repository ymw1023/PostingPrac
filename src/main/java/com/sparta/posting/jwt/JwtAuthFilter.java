package com.sparta.posting.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.posting.dto.ResponseStatusDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {   //Jwt 인증 필터 서블릿보다 먼저 실행

    private final JwtUtil jwtUtil;  //JwtUtil 을 사용하기 위한 의존성 주입

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request);       //토큰을 가져옴

        if(token != null) {     //토큰이 있는지 확인함
            if(!jwtUtil.validateToken(token).equals("success")){      //토큰을 확인함
                response.setCharacterEncoding("utf-8"); //한글 깨짐 방지
                jwtExceptionHandler(response, jwtUtil.validateToken(token), HttpStatus.UNAUTHORIZED.value());
                log.info(jwtUtil.validateToken(token));
                return;
            }       //false 일때, 토큰이 통과한 것
            Claims info = jwtUtil.getUserInfoFromToken(token);  //유저의 정보를 갖고 옴
            setAuthentication(info.getSubject());   //username 으로 함수 실행
        }
        filterChain.doFilter(request,response);     //인증이 필요없는 URI 도 있음
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();   //빈 박스를 만듬
        Authentication authentication = jwtUtil.createAuthentication(username); //인증 객체를 만듬
        context.setAuthentication(authentication);              //박스에 담음

        SecurityContextHolder.setContext(context);              //홀더에 담음
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new ResponseStatusDto(msg, HttpStatus.valueOf(statusCode)));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}