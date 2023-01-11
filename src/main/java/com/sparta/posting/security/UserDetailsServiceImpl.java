package com.sparta.posting.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.posting.dto.ResponseStatusDto;
import com.sparta.posting.entity.User;
import com.sparta.posting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    //    @Override
    public UserDetails loadUserByUsername(String username, HttpServletResponse response)/* throws UsernameNotFoundException*/ {
        //유저 이름으로 유저를 찾아서, 유저와 유저이름을 dto 에 담아서 반환

        if(userRepository.findByUsername(username).isEmpty()) {
            userDetailsExceptionHandler(response, "사용자를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED.value());
        }
        User user = userRepository.findByUsername(username).get();

        return new UserDetailsImpl(user, user.getUsername());
    }

    public void userDetailsExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
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