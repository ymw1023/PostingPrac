package com.sparta.posting.controller;

import com.sparta.posting.dto.PostsRequestDto;
import com.sparta.posting.entity.Posts;
import com.sparta.posting.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostsController {

    private final PostsService postsService;

    @PostMapping("/posts")  //게시물추가
    public Posts createPosts(@RequestBody PostsRequestDto requestDto) {
        return postsService.create(requestDto);
    }

    @GetMapping("/posts")   //게시물전체조회
    public List<Posts> getPosts() {
        return postsService.getPosts();
    }

    @PutMapping("/posts/{title}")   //게시물제목으로찾아서업데이트
    public String updatePosts(@PathVariable String title, @RequestBody PostsRequestDto requestDto) {
        return postsService.update(title, requestDto);
    }

    @GetMapping("/posts/get/title/{title}") //게시물제목으로조회
    public List<Posts> postsTitleGet(@PathVariable String title) {
        return postsService.getOneTitlePosts(title);
    }

    @GetMapping("/posts/get/username/{username}")   //게시물유저이름으로조회
    public List<Posts> postsUsernameGet(@PathVariable String username) {
        return postsService.getOneUsernamePosts(username);
    }

    @DeleteMapping("/posts/{id}")   //게시물id로 삭제
    public Long deletePosts(@PathVariable Long id) {
        return postsService.deletePosts(id);
    }
}
