package com.sparta.posting.service;

import com.sparta.posting.dto.*;
import com.sparta.posting.entity.Chat;
import com.sparta.posting.entity.Post;
import com.sparta.posting.entity.User;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Object create(PostRequestDto requestDto, HttpServletRequest request) {  //게시물 만들기

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

            if (userRepository.findByUsername(claims.getSubject()).isEmpty()) {
                return new ResponseStatusDto("사용자가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
            }
            User user = userRepository.findByUsername(claims.getSubject()).get();


            Post post = new Post(requestDto, user);
            postRepository.save(post);
            return new PostResponseDto(post);
        }
        return new ResponseStatusDto("Token Error", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public Object find() {   //게시물 전체 조회하기

        if(postRepository.findAllByOrderByCreatedAtDesc().isEmpty()) {
            return new ResponseStatusDto("게시물이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        List<Post> post = postRepository.findAllByOrderByCreatedAtDesc().get();


        List<PostAndChatDto> responseDto = new ArrayList<>();
        post.forEach(posting -> {
            List<Chat> chats = chatRepository.findByPost_IdOrderByCreatedAtDesc(posting.getId()).get();
            if (chats.isEmpty()) {
                responseDto.add(new PostAndChatDto(posting, null));
                return;
            }

            List<ChatResponseDto> chatting = new ArrayList<>();

            for(Chat chat: chats) {
                chatting.add(new ChatResponseDto(chat));
            }

            responseDto.add(new PostAndChatDto(posting, chatting));
        });

        return responseDto;
    }

    @Transactional
    public Object findOne(Long id) {    //게시물 선택 조회하기

        if(postRepository.findById(id).isEmpty()) {
            return new ResponseStatusDto("선택한 게시물이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Post post = postRepository.findById(id).get();


        if(chatRepository.findByPost_IdOrderByCreatedAtDesc(post.getId()).get().isEmpty()) {
            return new PostAndChatDto(post, null);
        }
        List<Chat> chats = chatRepository.findByPost_IdOrderByCreatedAtDesc(post.getId()).get();


        List<ChatResponseDto> chatting = new ArrayList<>();

        for(Chat chat: chats) {
            chatting.add(new ChatResponseDto(chat));
        }

        return new PostAndChatDto(post, chatting);
    }

    @Transactional
    public ResponseStatusDto update(Long id, PostRequestDto requestDto, HttpServletRequest request) {   //게시물 수정하기

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

            if (userRepository.findByUsername(claims.getSubject()).isEmpty()) {
                return new ResponseStatusDto("사용자가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
            }
            User user = userRepository.findByUsername(claims.getSubject()).get();


            if (postRepository.findById(id).isEmpty()) {
                return new ResponseStatusDto("아이디가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
            }
            Post post = postRepository.findById(id).get();

            // 로그인한 유저의 비밀번호와 게시글 작성자의 비밀번호를 비교
            if(!post.getUser().getUsername().equals(user.getUsername())) {
                return new ResponseStatusDto("본인이 작성한 게시글만 수정 가능합니다.", HttpStatus.BAD_REQUEST);
            }
            post.update(requestDto, user);
            return new ResponseStatusDto("수정 성공!", HttpStatus.OK);
        }
        return new ResponseStatusDto("Token Error", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseStatusDto delete(Long id, HttpServletRequest request) {  //게시물 삭제하기

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if(token != null) {

            // Token 검증
            if (!jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                return new ResponseStatusDto("Token Error", HttpStatus.BAD_REQUEST);
            }
            claims = jwtUtil.getUserInfoFromToken(token);


            if (userRepository.findByUsername(claims.getSubject()).isEmpty()) {
                return new ResponseStatusDto("사용자가 존재하지 않습니다", HttpStatus.BAD_REQUEST);
            }
            User user = userRepository.findByUsername(claims.getSubject()).get();


            if (postRepository.findById(id).isEmpty()) {
                return new ResponseStatusDto("아이디가 존재하지 않습니다", HttpStatus.BAD_REQUEST);
            }
            Post post = postRepository.findById(id).get();


            // 로그인한 유저의 비밀번호와 게시글 작성자의 비밀번호를 비교
            if(!post.getUser().getUsername().equals(user.getUsername())) {
                return new ResponseStatusDto("본인이 작성한 게시글만 삭제 가능합니다.", HttpStatus.BAD_REQUEST);
            }

            System.out.println("PostService.delete5");
            postRepository.deleteById(id);
            System.out.println("PostService.delete6");
            return new ResponseStatusDto("삭제 성공!", HttpStatus.OK);
        }
        return new ResponseStatusDto("Token Error", HttpStatus.BAD_REQUEST);
    }
}
