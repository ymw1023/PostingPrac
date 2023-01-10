package com.sparta.posting.controller;

import com.sparta.posting.dto.ChatRequestDto;
import com.sparta.posting.dto.ChatStatusDto;
import com.sparta.posting.dto.PostAndChatStatusDto;
import com.sparta.posting.dto.ResponseStatusDto;
import com.sparta.posting.security.UserDetailsImpl;
import com.sparta.posting.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/chats")
    public ChatStatusDto create(@RequestBody ChatRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.create(requestDto, userDetails.getUser());
    }

    @PutMapping("/chats")
    public ResponseStatusDto update(@RequestBody ChatRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.update(requestDto, userDetails.getUser());
    }

    @DeleteMapping("/chats")
    public ResponseStatusDto delete(@RequestBody ChatRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.delete(requestDto, userDetails.getUser());
    }

    @PatchMapping("/chats/{id}")   //게시물 좋아요
    public ResponseStatusDto likeUpdate(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.likeUpdate(id, userDetails.getUser());
    }
}
