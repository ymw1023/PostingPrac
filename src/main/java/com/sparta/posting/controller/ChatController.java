package com.sparta.posting.controller;

import com.sparta.posting.dto.ChatRequestDto;
import com.sparta.posting.dto.ChatResponseDto;
import com.sparta.posting.dto.ResponseMessageDto;
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
    public ChatResponseDto create(@RequestBody ChatRequestDto requestDto, HttpServletRequest request) {
        return chatService.create(requestDto, request);
    }

    @PutMapping("/chats/{id}")
    public ResponseMessageDto update(@PathVariable Long id, @RequestBody ChatRequestDto requestDto, HttpServletRequest request) {
        return chatService.update(id, requestDto, request);
    }

    @DeleteMapping("/chats/{id}")
    public ResponseStatusDto delete(@PathVariable Long id, HttpServletRequest request) {
        return chatService.delete(id, request);
    }
}
