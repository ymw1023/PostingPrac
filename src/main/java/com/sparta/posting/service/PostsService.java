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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public PostsCreateDto create(PostsRequestDto requestDto) {  //게시물 만들기
        Posts posts = new Posts(requestDto);
        postsRepository.save(posts);
        return new PostsCreateDto(posts);
    }

    @Transactional
    public List<PostingDto> getPosts() {   //게시물 전체 조회하기
        List<Posts> posts = postsRepository.findAllByOrderByModifiedAtDesc();
        ArrayList<PostingDto> posting = new ArrayList<>();
        for(Posts post: posts) {
            posting.add(new PostingDto(post));
        }
        return posting;
    }

    @Transactional
    public List<PostingDto> getOneTitlePosts(String title) {    //게시물 제목으로 조회하기
        List<Posts> posts = postsRepository.findPostsByTitleIsOrderByModifiedAtDesc(title);
        ArrayList<PostingDto> posting = new ArrayList<>();
        for(Posts post: posts) {
            posting.add(new PostingDto(post));
        }
        return posting;
    }

    @Transactional
    public List<PostingDto> getOneUsernamePosts(String username) {  //게시물 유저이름으로 조회하기
        List<Posts> posts = postsRepository.findPostsByUsernameIsOrderByModifiedAtDesc(username);
        ArrayList<PostingDto> posting = new ArrayList<>();
        for(Posts post: posts) {
            posting.add(new PostingDto(post));
        }
        return posting;
    }

    @Transactional             //게시물 id 로 찾은 후, password 를 검사    이때 id 가 -1 이거나 password 가 ""이면 id 나 password 가 입력 안된 것
    public Map<String, String> update(Long id, String password, PostsRequestDto requestDto) {   //게시물 수정하기
        Map<String, String> map = new HashMap<>();
        if(id == -1 || "".equals(password)) {
            map.put("message", "아이디와 비밀번호를 넘겨주세요!");
            return map;
        }
        Posts posts = postsRepository.findById(id).orElseThrow(
                ( ) -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if(!posts.getPassword().equals(password)) { //수정된 내용의 비밀번호가 다를경우 수정안됨
            map.put("message", "비밀번호가 다릅니다.");
            return map;
        }
        posts.update(requestDto);
        map.put("message", "수정 성공!");
        return map;
    }

    @Transactional
    public Map<String, String> deletePosts(Long id, String password) {  //게시물 삭제하기
        Map<String, String> map = new HashMap<>();
        Posts posting = postsRepository.findById(id).orElseThrow(
                ( ) -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if(!posting.getPassword().equals(password)) {
            map.put("message", "비밀번호가 다릅니다.");
            return map;
        }
        postsRepository.deleteById(id);
        map.put("message", "삭제 성공!");
        return map;
    }
}
