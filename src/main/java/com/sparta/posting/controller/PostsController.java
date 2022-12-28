package com.sparta.posting.controller;

import com.sparta.posting.dto.PostingDto;
import com.sparta.posting.dto.PostsRequestDto;
import com.sparta.posting.entity.Posts;
import com.sparta.posting.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PutMapping({"/update", "/update?id={id}&password={password}"})   //게시물 ID 로찾아서업데이트, 업데이트시 비밀번호가 다르면 수정안됨
    public Map<String, String> updatePosts(@RequestParam(value = "id", required = true, defaultValue = "-1") long id, @RequestParam(value = "password", required = true, defaultValue = "") String password, @RequestBody PostsRequestDto requestDto) {
        Map<String, String> map = new HashMap<>();                                  //id, password 를 입력 안 할시 error 안나고 실패 문구가 리턴되게 수정
        map.put("message", postsService.update(id, password, requestDto));
        return map;
    }   //update 와 delete 가 다르게 생긴이유
        //update 는 body 에 수정된 정보를 넘겨야 되고
        //delete 는 body 에 넘길게 따로 없어서
        //delete 는 body 에 password 를 넣음
    @DeleteMapping("/delete/{id}")   //게시물 id 로 삭제  //검사 하기 위해서 비밀번호를 받음
    public Map<String, String> deletePosts(@PathVariable long id, @RequestBody PostsRequestDto requestDto) {
        Map<String, String> map = new HashMap<>();
        map.put("message", postsService.deletePosts(id, requestDto.getPassword()));
        return map;
    }   //성공 실패 메시지를 JSON 형태로 반환하기 위해 Map 을 씀 - @ResponseBody 가 있어서 가능한듯
}
