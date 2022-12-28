package com.sparta.posting.service;

import com.sparta.posting.dto.PostingDto;
import com.sparta.posting.dto.PostsCreateDto;
import com.sparta.posting.dto.PostsRequestDto;
import com.sparta.posting.entity.Posts;
import com.sparta.posting.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public PostsCreateDto create(PostsRequestDto requestDto) {
        Posts posts = new Posts(requestDto);
        postsRepository.save(posts);
        return new PostsCreateDto(posts);
    }

    @Transactional
    public List<PostingDto> getPosts() {   //username, title, contents, modifiedAt을 리턴
        List<Posts> posts = postsRepository.findAllByOrderByModifiedAtDesc();
        ArrayList<PostingDto> posting = new ArrayList<>();
        for(Posts post: posts) {
            posting.add(new PostingDto(post));
        }
        return posting;
    }

    @Transactional
    public List<PostingDto> getOneTitlePosts(String title) {
        List<Posts> posts = postsRepository.findPostsByTitleIsOrderByModifiedAtDesc(title);
        ArrayList<PostingDto> posting = new ArrayList<>();
        for(Posts post: posts) {
            posting.add(new PostingDto(post));
        }
        return posting;
    }

    @Transactional
    public List<PostingDto> getOneUsernamePosts(String username) {
        List<Posts> posts = postsRepository.findPostsByUsernameIsOrderByModifiedAtDesc(username);
        ArrayList<PostingDto> posting = new ArrayList<>();
        for(Posts post: posts) {
            posting.add(new PostingDto(post));
        }
        return posting;
    }

    @Transactional
    public String update(Long id, String password, PostsRequestDto requestDto) {
        if(id == -1 || "".equals(password)) {
            return "아이디와 비밀번호를 넘겨주세요!";
        }
        Posts posts = postsRepository.findById(id).orElseThrow(
                ( ) -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if(!posts.getPassword().equals(password)) { //수정된 내용의 비밀번호가 다를경우 수정안됨
            return "비밀번호가 다릅니다.";
        }
        posts.update(requestDto);
        return "수정 성공!";
    }

    @Transactional
    public String deletePosts(Long id, String password) {
        Posts posting = postsRepository.findById(id).orElseThrow(
                ( ) -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if(!posting.getPassword().equals(password)) {
            return "비밀번호가 다릅니다.";
        }
        postsRepository.deleteById(id);
        return "삭제성공!";
    }
}
