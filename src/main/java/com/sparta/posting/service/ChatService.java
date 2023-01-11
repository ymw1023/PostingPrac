package com.sparta.posting.service;

import com.sparta.posting.dto.*;
import com.sparta.posting.entity.*;
import com.sparta.posting.repository.ChatRepository;
import com.sparta.posting.repository.LikeChatRepository;
import com.sparta.posting.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final PostRepository postRepository;
    private final LikeChatRepository likeChatRepository;

    @Transactional
    public ChatStatusDto create(ChatRequestDto requestDto, User user, HttpServletResponse response) {

        if(postRepository.findById(requestDto.getId()).isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            throw new IllegalArgumentException("해당 아이디의 게시물이 존재하지 않습니다.");
        }
        Post post = postRepository.findById(requestDto.getId()).get();


        Chat chat = new Chat(requestDto, post, user);

        chatRepository.save(chat);
        return new ChatStatusDto("Success", HttpStatus.OK, new ChatResponseDto(chat));
    }

    @Transactional
    public ResponseStatusDto update(ChatRequestDto requestDto, User user, HttpServletResponse response) {

        Chat chat = checkChat(requestDto.getId(), response);

        // 로그인한 유저의 비밀번호와 게시글 작성자의 비밀번호를 비교
        if(!(chat.getUser().getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN))) {
            return new ResponseStatusDto("본인이 작성한 댓글만 수정 가능합니다.", HttpStatus.BAD_REQUEST);
        }
        chat.update(requestDto);
        return new ResponseStatusDto("수정 성공!", HttpStatus.OK);
    }

    @Transactional
    public ResponseStatusDto delete(ChatRequestDto requestDto, User user, HttpServletResponse response) {  //댓글 삭제하기

        Chat chat = checkChat(requestDto.getId(), response);


        // 로그인한 유저와 댓글 작성자의 유저 네임을 비교
        if(!(chat.getUser().getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN))) {
            return new ResponseStatusDto("본인이 작성한 댓글만 삭제 가능합니다.", HttpStatus.BAD_REQUEST);
        }
        chatRepository.deleteById(requestDto.getId());

        return new ResponseStatusDto("삭제 성공!", HttpStatus.OK);
    }

    @Transactional         //id 는 Chat 의 id
    public ResponseStatusDto likeUpdate(Long id, User user, HttpServletResponse response) {   //댓글 좋아요

        Chat chat = checkChat(id, response);

        if (likeChatRepository.findByChat_IdAndUser_Id(chat.getId(), user.getId()).isPresent()) {   //chat id 와 user id 가 일치하는 likeChat 이 존재하는 경우
            chatRepository.findById(chat.getId()).orElseThrow().like(-1L);      //예외를 던지는 경우 없음 -- if 문에서 id 있는 것만 들어옴
            likeChatRepository.deleteById(likeChatRepository.findByChat_IdAndUser_Id(chat.getId(), user.getId()).get().getId());
            return new ResponseStatusDto("좋아요 취소!", HttpStatus.OK);
        }

        chatRepository.findById(chat.getId()).orElseThrow().like(1L); //entity 의 좋아요 수 바꾸기
        likeChatRepository.save(new LikeChat(chat, user));
        return new ResponseStatusDto("좋아요 성공!", HttpStatus.OK);
    }

    public Chat checkChat(Long id, HttpServletResponse response) {
        if(chatRepository.findById(id).isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            throw new IllegalArgumentException("해당 아이디의 댓글이 존재하지 않습니다.");
        }
        return chatRepository.findById(id).get();
    }
}
