package com.sparta.posting.service;

import com.sparta.posting.dto.PostsRequestDto;
import com.sparta.posting.entity.Posts;
import com.sparta.posting.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Posts create(PostsRequestDto requestDto) {
        Posts posts = new Posts(requestDto);
        postsRepository.save(posts);
        return posts;
    }

    @Transactional
    public List<Posts> getPosts() {
        return postsRepository.findAllByOrderByModifiedAtDesc();
    }

    public String update(String title, PostsRequestDto requestDto) {
        Posts posts = postsRepository.findByTitle(title);
        posts.update(requestDto);   //이 줄이 없으면 수정이 안됨
        postsRepository.flush();    //flush()는 변경사항을 저장하는거인데
        return "수정 성공!";         //entity의 변경사항을 저장하는듯
    }

    public Long deletePosts(Long id) {
        postsRepository.deleteById(id);
        return id;
    }

    public List<Posts> getOneTitlePosts(String title) {
        return postsRepository.findPostsByTitleIsOrderByModifiedAtDesc(title);
    }

    public List<Posts> getOneUsernamePosts(String username) {
        return postsRepository.findPostsByUsernameIsOrderByModifiedAtDesc(username);
    }
}
