package com.sparta.posting.controller;

import com.sparta.posting.dto.*;
import com.sparta.posting.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")  //게시물추가
    public PostResponseDto create(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.create(requestDto, request);
    }

    @GetMapping("/posts")   //게시물 전체 조회
    public List<GetResponseDto> find() {
        return postService.find();
    }

    @GetMapping("/posts/{id}") //게시물 id 로 조회
    public GetResponseDto findOne(@PathVariable Long id) {
        return postService.findOne(id);
    }

    @PutMapping("/posts/{id}")   //게시물 업데이트
    public ResponseMessageDto update(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.update(id, requestDto, request);
    }

    @DeleteMapping("/posts/{id}")   //게시물 삭제
    public ResponseStatusDto delete(@PathVariable Long id, HttpServletRequest request) {
        System.out.println("PostController.delete1");
        return postService.delete(id, request);
    }
}
