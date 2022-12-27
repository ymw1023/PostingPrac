package com.sparta.posting.service;

import com.sparta.posting.dto.PostsRequestDto;
import com.sparta.posting.entity.Posts;
import com.sparta.posting.repository.PostsRepository;
import com.sparta.posting.repository.UserInfoMapping;
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
    public List<UserInfoMapping> getPosts() {   //username, title, contents, 수정된 시간을 리턴
        return postsRepository.findAllByOrderByModifiedAtDesc();
    }

    @Transactional
    public List<UserInfoMapping> getOneTitlePosts(String title) {
        return postsRepository.findPostsByTitleIsOrderByModifiedAtDesc(title);
    }

    @Transactional
    public List<UserInfoMapping> getOneUsernamePosts(String username) {
        return postsRepository.findPostsByUsernameIsOrderByModifiedAtDesc(username);
    }

    @Transactional
    public String update(long id, PostsRequestDto requestDto) {
        Posts posts = postsRepository.findById(id);
        if(!posts.getPassword().equals(requestDto.getPassword())) { //수정된 내용의 비밀번호가 다를경우 수정안됨
            return "비밀번호가 다릅니다.";
        }
        posts.update(requestDto);   //이 줄이 없으면 수정이 안됨
        postsRepository.flush();    //flush()는 변경사항을 저장하는거인데
        return "수정 성공!";         //entity의 변경사항을 저장하는듯
    }

    @Transactional
    public String deletePosts(long id, String password) {
        Posts posting = postsRepository.findById(id);
        if(!posting.getPassword().equals(password)) {
            return "비밀번호가 다릅니다.";
        }
        postsRepository.deleteById(id);
        return "삭제성공!";
    }
}
