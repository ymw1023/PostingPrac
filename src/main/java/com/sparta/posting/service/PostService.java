package com.sparta.posting.service;

import com.sparta.posting.dto.GetResponseDto;
import com.sparta.posting.dto.PostResponseDto;
import com.sparta.posting.dto.PostRequestDto;
import com.sparta.posting.dto.ResponseMessageDto;
import com.sparta.posting.entity.Post;
import com.sparta.posting.entity.User;
import com.sparta.posting.jwt.JwtUtil;
import com.sparta.posting.repository.PostRepository;
import com.sparta.posting.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public PostResponseDto create(PostRequestDto requestDto, HttpServletRequest request) {  //게시물 만들기

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

            Post post = new Post(requestDto);
            postRepository.save(post);
            return new PostResponseDto(post);
        }
        return null;
    }

    @Transactional
    public List<GetResponseDto> find(HttpServletRequest request) {   //게시물 전체 조회하기
        List<Post> post = postRepository.findAllByOrderByCreatedAtDesc().orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        return post.stream().map(GetResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public GetResponseDto findOne(Long id, HttpServletRequest request) {    //게시물 선택 조회하기
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );
        return new GetResponseDto(post);
    }

    @Transactional
    public ResponseMessageDto update(Long id, PostRequestDto requestDto, HttpServletRequest request) {   //게시물 수정하기

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

            Post post = postRepository.findById(id).orElseThrow(
                    ( ) -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );

            // 로그인한 유저의 비밀번호와 게시글 작성자의 비밀번호를 비교
            if(!post.getUser().getUsername().equals(user.getUsername())) {
                return new ResponseMessageDto("본인이 작성한 게시글만 수정 가능합니다.");
            }
            post.update(requestDto);
            return new ResponseMessageDto("수정 성공!");
        }
        return null;
    }

    @Transactional
    public ResponseMessageDto delete(Long id, HttpServletRequest request) {  //게시물 삭제하기

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

            Post post = postRepository.findById(id).orElseThrow(
                    ( ) -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );

            // 로그인한 유저의 비밀번호와 게시글 작성자의 비밀번호를 비교
            if(!post.getUser().getUsername().equals(user.getUsername())) {
                return new ResponseMessageDto("본인이 작성한 게시글만 삭제 가능합니다.");
            }
            postRepository.deleteById(id);
            return new ResponseMessageDto("삭제 성공!");
        }
        return null;
    }
}
