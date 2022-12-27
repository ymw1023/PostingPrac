package com.sparta.posting.controller;

import com.sparta.posting.dto.PostingDto;
import com.sparta.posting.dto.PostsRequestDto;
import com.sparta.posting.entity.Posts;
import com.sparta.posting.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostsController {

    private final PostsService postsService;

    @PostMapping("/create")  //게시물추가
    public Posts createPosts(@RequestBody PostsRequestDto requestDto) {
        return postsService.create(requestDto);
    }

    @GetMapping("/get")   //게시물전체조회
    public List<PostingDto> getPosts() {
        return postsService.getPosts();
    }

    @GetMapping("/get/title/{title}") //게시물제목으로조회
    public List<PostingDto> postsTitleGet(@PathVariable String title) {
        return postsService.getOneTitlePosts(title);
    }

    @GetMapping("/get/username/{username}")   //게시물유저이름으로조회
    public List<PostingDto> postsUsernameGet(@PathVariable String username) {
        return postsService.getOneUsernamePosts(username);
    }

    @PutMapping("/update/{id}")   //게시물 ID 로찾아서업데이트, 업데이트시 비밀번호가 다르면 수정안됨
    public String updatePosts(@PathVariable long id, @RequestBody PostsRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    @DeleteMapping("/delete/{id}")   //게시물 id 로 삭제  //검사 하기 위해서 비밀번호를 받음
    public String deletePosts(@PathVariable long id, @RequestBody PostsRequestDto requestDto) {
        return postsService.deletePosts(id, requestDto.getPassword());
    }
}
