package com.sparta.posting.controller;

import com.sparta.posting.dto.PostingDto;
import com.sparta.posting.dto.PostsCreateDto;
import com.sparta.posting.dto.PostsRequestDto;
import com.sparta.posting.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostsController {

    private final PostsService postsService;

    @PostMapping("/create")  //게시물추가
    public PostsCreateDto createPosts(@RequestBody PostsRequestDto requestDto) {
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
    public Map<String, String> updatePosts(@RequestParam(value = "id", required = true, defaultValue = "-1") Long id, @RequestParam(value = "password", required = true, defaultValue = "") String password, @RequestBody PostsRequestDto requestDto) {
        return postsService.update(id, password, requestDto);               //@RequestParam 을 써서 id 와 password 받고, 수정에 필요한 정보는 @ResponseBody 로 body 에서 받음
    }                                                                       //id 와 password 의 default 값을 줘서 URI 에 값을 안 넘겨줬을 경우 error 를 안 내고 실패 문구가 나오게 처리

    @DeleteMapping("/delete/{id}")   //게시물 id 로 삭제  //검사 하기 위해서 비밀번호를 받음
    public Map<String, String> deletePosts(@PathVariable Long id, @RequestBody PostsRequestDto requestDto) {
        return postsService.deletePosts(id, requestDto.getPassword());
    }   //성공 실패 메시지를 JSON 형태로 반환하기 위해 Map 을 씀 - @ResponseBody 가 있어서 가능한듯
}
