package com.sparta.posting.service;

import com.sparta.posting.dto.*;
import com.sparta.posting.entity.*;
import com.sparta.posting.repository.ChatRepository;
import com.sparta.posting.repository.LikeChatRepository;
import com.sparta.posting.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final PostRepository postRepository;
    private final LikeChatRepository likeChatRepository;

    @Transactional
    public ChatStatusDto create(ChatRequestDto requestDto, User user) {

//        if( postRepository.findById(requestDto.getId()).isEmpty()) {
//            return new ChatStatusDto("게시물이 존재하지 않습니다", HttpStatus.BAD_REQUEST, null);
//        }
        Post post = postRepository.findById(requestDto.getId()).orElseThrow();


        Chat chat = new Chat(requestDto, post, user);

        chatRepository.save(chat);
        return new ChatStatusDto("Success", HttpStatus.OK, new ChatResponseDto(chat));
    }

    @Transactional
    public ResponseStatusDto update(ChatRequestDto requestDto, User user) {

//        if(chatRepository.findById(requestDto.getId()).isEmpty()) {
//            return new ResponseStatusDto("아이디가 존재하지 않습니다", HttpStatus.BAD_REQUEST);
//        }
        Chat chat = chatRepository.findById(requestDto.getId()).orElseThrow();


        // 로그인한 유저의 비밀번호와 게시글 작성자의 비밀번호를 비교
        if(!(chat.getUser().getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN))) {
            return new ResponseStatusDto("본인이 작성한 댓글만 수정 가능합니다.", HttpStatus.BAD_REQUEST);
        }
        chat.update(requestDto);
        return new ResponseStatusDto("수정 성공!", HttpStatus.OK);
    }

    @Transactional
    public ResponseStatusDto delete(ChatRequestDto chatRequestDto, User user) {  //게시물 삭제하기

//        if (chatRepository.findById(chatRequestDto.getId()).isEmpty()) {
//            return new ResponseStatusDto("아이디가 존재하지 않습니다", HttpStatus.BAD_REQUEST);
//        }
        Chat chat = chatRepository.findById(chatRequestDto.getId()).orElseThrow();


        // 로그인한 유저와 댓글 작성자의 유저 네임을 비교
        if(!(chat.getUser().getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN))) {
            return new ResponseStatusDto("본인이 작성한 댓글만 삭제 가능합니다.", HttpStatus.BAD_REQUEST);
        }
        chatRepository.deleteById(chatRequestDto.getId());

        List<LikeChat> chats = likeChatRepository.findAllByChatId(chatRequestDto.getId());

        for(LikeChat chatting: chats) {
            likeChatRepository.deleteById(chatting.getChatId());
        }

        return new ResponseStatusDto("삭제 성공!", HttpStatus.OK);
    }

    @Transactional         //id 는 post 의 id
    public ResponseStatusDto likeUpdate(Long id, User user) {   //게시물 좋아요

//        if (chatRepository.findById(id).isEmpty()) {
//            return new ResponseStatusDto("아이디가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
//        }
        List<LikeChat> chats = likeChatRepository.findAllByUsername(user.getUsername());

        for(LikeChat chat: chats) {
            if(chat.getChatId().equals(id)) {
                chatRepository.findById(id).orElseThrow().like(-1L);
                likeChatRepository.deleteById(chat.getId());
                return new ResponseStatusDto("좋아요를 취소 하셨습니다!", HttpStatus.OK);
            }
        }
        chatRepository.findById(id).orElseThrow().like(1L);
        likeChatRepository.save(new LikeChat(id, user.getUsername()));
        return new ResponseStatusDto("좋아요 성공!", HttpStatus.OK);
    }
}
