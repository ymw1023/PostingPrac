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
        Post Post = new Post(requestDto);
        postRepository.save(Post);
        return new PostResponseDto(Post);
    }

    @Transactional
    public List<GetResponseDto> getPost() {   //게시물 전체 조회하기
        List<Post> Post = postRepository.findAllByOrderByModifiedAtDesc().orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );
        return Post.stream().map(GetResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public GetResponseDto getPost(Long id) {    //게시물 선택 조회하기
        Post Post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        return new GetResponseDto(Post);
    }

    @Transactional
    public ResponseMessageDto update(Long id, PostRequestDto requestDto) {   //게시물 수정하기
        Post Post = postRepository.findById(id).orElseThrow(
                ( ) -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if(!Post.getPassword().equals(requestDto.getPassword())) {
            return new ResponseMessageDto("비밀번호가 다릅니다.");
        }
        Post.update(requestDto);
        return new ResponseMessageDto("수정 성공!");
    }

    @Transactional
    public ResponseMessageDto deletePost(Long id, String password) {  //게시물 삭제하기
        Post posting = postRepository.findById(id).orElseThrow(
                ( ) -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if(!posting.getPassword().equals(password)) {
            return new ResponseMessageDto("비밀번호가 다릅니다.");
        }
        postRepository.deleteById(id);
        return new ResponseMessageDto("삭제 성공!");
    }
}
