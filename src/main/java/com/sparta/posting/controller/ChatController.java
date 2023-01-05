package com.sparta.posting.controller;

import com.sparta.posting.dto.ChatRequestDto;
import com.sparta.posting.dto.ChatStatusDto;
import com.sparta.posting.dto.ResponseStatusDto;
import com.sparta.posting.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/chats")
    public ChatStatusDto create(@RequestBody ChatRequestDto requestDto, HttpServletRequest request) {
        return chatService.create(requestDto, request);
    }

    @PutMapping("/chats")
    public ResponseStatusDto update(@RequestBody ChatRequestDto requestDto, HttpServletRequest request) {
        return chatService.update(requestDto, request);
    }

    @DeleteMapping("/chats")
    public ResponseStatusDto delete(@RequestBody ChatRequestDto requestDto, HttpServletRequest request) {
        return chatService.delete(requestDto, request);
    }
}
