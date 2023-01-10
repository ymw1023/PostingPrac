package com.sparta.posting.controller;

import com.sparta.posting.dto.*;
import com.sparta.posting.security.UserDetailsImpl;
import com.sparta.posting.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")  //게시물추가
    public PostStatusDto create(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.create(requestDto, userDetails.getUser());
    }

    @GetMapping("/posts")   //게시물 전체 조회
    public PostAndChatStatusDto find() {
        return postService.find();
    }

    @GetMapping("/posts/{id}") //게시물 id 로 조회
    public PostChatStatusDto findOne(@PathVariable Long id) {
        return postService.findOne(id);
    }

    @PutMapping("/posts/{id}")   //게시물 업데이트
    public ResponseStatusDto update(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.update(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/posts/{id}")   //게시물 삭제
    public ResponseStatusDto delete(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.delete(id, userDetails.getUser());
    }

    @PatchMapping("/posts/{id}")   //게시물 좋아요
    public ResponseStatusDto likeUpdate(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.likeUpdate(id, userDetails.getUser());
    }

    @GetMapping("/posts/likes")   //게시물 좋아요
    public PostAndChatStatusDto likeGet( @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.likeGet(userDetails.getUser());
    }
}
