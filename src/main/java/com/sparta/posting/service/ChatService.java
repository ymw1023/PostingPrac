package com.sparta.posting.service;

import com.sparta.posting.dto.*;
import com.sparta.posting.entity.Chat;
import com.sparta.posting.entity.Post;
import com.sparta.posting.entity.User;
import com.sparta.posting.entity.UserRoleEnum;
import com.sparta.posting.jwt.JwtUtil;
import com.sparta.posting.repository.ChatRepository;
import com.sparta.posting.repository.PostRepository;
import com.sparta.posting.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Object create(ChatRequestDto requestDto, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if(token != null) {

            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                return new ResponseStatusDto("Token Error", HttpStatus.BAD_REQUEST);
            }

            if(userRepository.findByUsername(claims.getSubject()).isEmpty()) {
                return new ResponseStatusDto("사용자가 존재하지 않습니다", HttpStatus.BAD_REQUEST);
            }
            User user = userRepository.findByUsername(claims.getSubject()).get();


            if( postRepository.findById(requestDto.getId()).isEmpty()) {
                return new ResponseStatusDto("게시물이 존재하지 않습니다", HttpStatus.BAD_REQUEST);
            }
            Post post = postRepository.findById(requestDto.getId()).get();


            Chat chat = new Chat(requestDto, post, user);

            chatRepository.save(chat);
            return new ChatResponseDto(chat);
        }
        return new ResponseStatusDto("Token Error", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseStatusDto update(Long id, ChatRequestDto requestDto, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if(token != null) {

            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                return new ResponseStatusDto("Token Error", HttpStatus.BAD_REQUEST);
            }

            if(userRepository.findByUsername(claims.getSubject()).isEmpty()) {
                return new ResponseStatusDto("사용자가 존재하지 않습니다", HttpStatus.BAD_REQUEST);
            }
            User user = userRepository.findByUsername(claims.getSubject()).get();


            if(chatRepository.findById(id).isEmpty()) {
                return new ResponseStatusDto("아이디가 존재하지 않습니다", HttpStatus.BAD_REQUEST);
            }
            Chat chat = chatRepository.findById(id).get();


            // 로그인한 유저의 비밀번호와 게시글 작성자의 비밀번호를 비교
            if(!(chat.getUser().getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN))) {
                return new ResponseStatusDto("본인이 작성한 댓글만 수정 가능합니다.", HttpStatus.BAD_REQUEST);
            }
            chat.update(requestDto, user);
            return new ResponseStatusDto("수정 성공!", HttpStatus.OK);
        }
        return new ResponseStatusDto("Token Error",HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseStatusDto delete(Long id, HttpServletRequest request) {  //게시물 삭제하기

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if(token != null) {

            // Token 검증
            if (!jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                return new ResponseStatusDto("Token Error", HttpStatus.UNAUTHORIZED);
            }
            claims = jwtUtil.getUserInfoFromToken(token);


            if (userRepository.findByUsername(claims.getSubject()).isEmpty()) {
                return new ResponseStatusDto("사용자가 존재하지 않습니다", HttpStatus.UNAUTHORIZED);
            }
            User user = userRepository.findByUsername(claims.getSubject()).get();


            if (chatRepository.findById(id).isEmpty()) {
                return new ResponseStatusDto("아이디가 존재하지 않습니다", HttpStatus.NOT_FOUND);
            }
            Chat chat = chatRepository.findById(id).get();


            // 로그인한 유저의 비밀번호와 게시글 작성자의 비밀번호를 비교
            if(!(chat.getUser().getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN))) {
                return new ResponseStatusDto("본인이 작성한 댓글만 삭제 가능합니다.", HttpStatus.UNAUTHORIZED);
            }
            postRepository.deleteById(id);
            return new ResponseStatusDto("삭제 성공!", HttpStatus.OK);
        }
        return new ResponseStatusDto("Token Error", HttpStatus.UNAUTHORIZED);
    }
}
