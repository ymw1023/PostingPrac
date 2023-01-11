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

        response.setCharacterEncoding("utf-8"); //한글 깨짐 방지
        if(request.getRequestURI().equals("/users/signup") || request.getRequestURI().equals("/users/login")) {     //회원 가입과 로그인 시에 토큰 검증 x
            filterChain.doFilter(request,response);                                                                 //이유: 중간에 토큰이 없을 경우 에러를 내기 위해서(45번줄) <- 이러면 회원가입, 로그인 시에도 토큰이 없기 때문에 앞에서 if 문을 써준 것
            return;
        }
        if(token != null) {     //토큰이 있는지 확인함
            if(!jwtUtil.validateToken(token).equals("success")){      //토큰을 확인함
                jwtExceptionHandler(response, jwtUtil.validateToken(token), HttpStatus.UNAUTHORIZED.value());   //토큰이 이상할 경우 예외를 던짐
            }       //false 일때, 토큰이 통과한 것
            Claims info = jwtUtil.getUserInfoFromToken(token);  //유저의 정보를 갖고 옴
            setAuthentication(info.getSubject(), response);   //username 으로 함수 실행
            filterChain.doFilter(request,response);     //인증이 필요없는 URI 도 있음
            return;
        }
        jwtExceptionHandler(response, "토큰이 존재하지 않습니다", HttpStatus.UNAUTHORIZED.value());
    }

    public void setAuthentication(String username, HttpServletResponse response) {  //예외 발생시 response 로 상태코드 넘김
        SecurityContext context = SecurityContextHolder.createEmptyContext();   //빈 박스를 만듬
        Authentication authentication = jwtUtil.createAuthentication(username, response); //인증 객체를 만듬
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