package com.sparta.posting.controller;

import com.sparta.posting.dto.ChatRequestDto;
import com.sparta.posting.dto.ChatStatusDto;
import com.sparta.posting.dto.ResponseStatusDto;
import com.sparta.posting.security.UserDetailsImpl;
import com.sparta.posting.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/chats")
    public ChatStatusDto create(@RequestBody ChatRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        return chatService.create(requestDto, userDetails.getUser(), response);
    }

    @PutMapping("/chats")
    public ResponseStatusDto update(@RequestBody ChatRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        return chatService.update(requestDto, userDetails.getUser(), response);
    }

    @DeleteMapping("/chats")
    public ResponseStatusDto delete(@RequestBody ChatRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        return chatService.delete(requestDto, userDetails.getUser(), response);
    }

    @PatchMapping("/chats/{id}")   //댓글 좋아요 업데이트, 댓글 의 id 를 받아옴
    public ResponseStatusDto likeUpdate(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        return chatService.likeUpdate(id, userDetails.getUser(), response);
    }
}
