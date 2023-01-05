package com.sparta.posting.service;

import com.sparta.posting.dto.LoginRequestDto;
import com.sparta.posting.dto.ResponseStatusDto;
import com.sparta.posting.dto.SignupRequestDto;
import com.sparta.posting.entity.User;
import com.sparta.posting.entity.UserRoleEnum;
import com.sparta.posting.jwt.JwtUtil;
import com.sparta.posting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public ResponseStatusDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        String checkUsername = "^[A-Za-z\\d]{4,10}$";
        String checkPassword = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$";

        if(!Pattern.matches(checkUsername, username)) {
            return new ResponseStatusDto("4자 이상, 10자 이하의 알파벳과 숫자만 있어야 합니다!", HttpStatus.BAD_REQUEST);
        }
        if(!Pattern.matches(checkPassword, password)) {
            return new ResponseStatusDto("비밀번호는 8자 이상, 15자 이하이고 알파벳과 숫자로만 이루어져 있으며, 한개 이상의 알파벳과 숫자, 특수문자가 있어야 합니다!", HttpStatus.BAD_REQUEST);
        }

        User found = userRepository.findByUsername(username);
        if(found != null) {
            return new ResponseStatusDto("중복된 username 존재합니다", HttpStatus.BAD_REQUEST);
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                return new ResponseStatusDto("관리자 암호가 틀려 등록이 불가능합니다.", HttpStatus.BAD_REQUEST);
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, role);
        userRepository.save(user);

        return new ResponseStatusDto("회원가입을 성공하셨습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseStatusDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        if(userRepository.findByUsername(username) == null){
            return new ResponseStatusDto("회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByUsername(username);

        if (!user.getPassword().equals(password)) {
            return new ResponseStatusDto("회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));

        return new ResponseStatusDto("로그인을 성공하셨습니다.", HttpStatus.OK);
    }
}
