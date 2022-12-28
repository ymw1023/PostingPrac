package com.sparta.posting.service;

import com.sparta.posting.dto.GetResponseDto;
import com.sparta.posting.dto.PostResponseDto;
import com.sparta.posting.dto.PostRequestDto;
import com.sparta.posting.dto.ResponseMessageDto;
import com.sparta.posting.entity.Post;
import com.sparta.posting.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public PostResponseDto create(PostRequestDto requestDto) {  //게시물 만들기
        Post post = new Post(requestDto);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    @Transactional
    public List<GetResponseDto> find() {   //게시물 전체 조회하기
        List<Post> post = postRepository.findAllByOrderByModifiedAtDesc().orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );
        return post.stream().map(GetResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public GetResponseDto findOne(Long id) {    //게시물 선택 조회하기
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        return new GetResponseDto(post);
    }

    @Transactional
    public ResponseMessageDto update(Long id, PostRequestDto requestDto) {   //게시물 수정하기
        Post post = postRepository.findById(id).orElseThrow(
                ( ) -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if(!post.getPassword().equals(requestDto.getPassword())) {
            return new ResponseMessageDto("비밀번호가 다릅니다.");
        }
        post.update(requestDto);
        return new ResponseMessageDto("수정 성공!");
    }

    @Transactional
    public ResponseMessageDto delete(Long id, String password) {  //게시물 삭제하기
        Post post = postRepository.findById(id).orElseThrow(
                ( ) -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if(!post.getPassword().equals(password)) {
            return new ResponseMessageDto("비밀번호가 다릅니다.");
        }
        postRepository.deleteById(id);
        return new ResponseMessageDto("삭제 성공!");
    }
}
