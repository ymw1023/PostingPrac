package com.sparta.posting.service;

import com.sparta.posting.dto.ChatRequestDto;
import com.sparta.posting.dto.ChatResponseDto;
import com.sparta.posting.dto.PostResponseDto;
import com.sparta.posting.dto.ResponseMessageDto;
import com.sparta.posting.entity.Chat;
import com.sparta.posting.entity.Post;
import com.sparta.posting.entity.User;
import com.sparta.posting.jwt.JwtUtil;
import com.sparta.posting.repository.ChatRepository;
import com.sparta.posting.repository.PostRepository;
import com.sparta.posting.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
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
    public ChatResponseDto create(ChatRequestDto requestDto, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if(token != null) {

            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다")
            );

            Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                    () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
            );

            Chat chat = new Chat(requestDto, post, user);
            chatRepository.save(chat);
            return new ChatResponseDto(chat);
        }
        return null;
    }

    @Transactional
    public ResponseMessageDto update(Long id, ChatRequestDto requestDto, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if(token != null) {

            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다")
            );

            Chat chat = chatRepository.findById(id).orElseThrow(
                    ( ) -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );

            // 로그인한 유저의 비밀번호와 게시글 작성자의 비밀번호를 비교
            if(!chat.getUser().getUsername().equals(user.getUsername())) {
                return new ResponseMessageDto("본인이 작성한 댓글만 수정 가능합니다.");
            }
            chat.update(requestDto, user);
            return new ResponseMessageDto("수정 성공!");
        }
        return null;
    }
}
