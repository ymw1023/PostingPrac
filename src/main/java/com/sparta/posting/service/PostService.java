package com.sparta.posting.service;

import com.sparta.posting.dto.*;
import com.sparta.posting.entity.Chat;
import com.sparta.posting.entity.LikePost;
import com.sparta.posting.entity.Post;
import com.sparta.posting.entity.User;
import com.sparta.posting.repository.ChatRepository;
import com.sparta.posting.repository.LikePostRepository;
import com.sparta.posting.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ChatRepository chatRepository;
    private final LikePostRepository likePostRepository;

    @Transactional
    public PostStatusDto create(PostRequestDto requestDto, User user) {  //게시물 만들기
            Post post = new Post(requestDto, user);
            postRepository.save(post);
            return new PostStatusDto("Success", HttpStatus.OK, new PostResponseDto(post));
    }

    @Transactional(readOnly = true)
    public PostAndChatStatusDto find() {   //게시물 전체 조회하기

//        if(postRepository.findAllByOrderByCreatedAtDesc().isEmpty()) {
//            return new PostAndChatStatusDto("게시물이 존재하지 않습니다.", HttpStatus.BAD_REQUEST, null);
//        }
        List<Post> post = postRepository.findAllByOrderByCreatedAtDesc();


        List<PostAndChatDto> responseDto = new ArrayList<>();
        post.forEach(posting -> {
//            if (chatRepository.findByPost_IdOrderByCreatedAtDesc(posting.getId()).isEmpty()) {
//                responseDto.add(new PostAndChatDto(posting, null));
//                return;
//            }
            List<Chat> chats = chatRepository.findByPost_IdOrderByCreatedAtDesc(posting.getId());

            List<CommentDto> chatting = new ArrayList<>();

            for(Chat chat: chats) {
                chatting.add(new CommentDto(chat.getComments(), chat.getLikeCount()));
            }

            responseDto.add(new PostAndChatDto(posting, chatting));
        });

        return new PostAndChatStatusDto("Success", HttpStatus.OK, responseDto);
    }

    @Transactional
    public PostChatStatusDto findOne(Long id) {    //게시물 선택 조회하기

//        if(postRepository.findById(id).isEmpty()) {
//            return new PostChatStatusDto("선택한 게시물이 존재하지 않습니다.", HttpStatus.BAD_REQUEST, null);
//        }
        Post post = postRepository.findById(id).orElseThrow();


//        if(chatRepository.findByPost_IdOrderByCreatedAtDesc(post.getId()).isEmpty()) {
//            return new PostChatStatusDto("Success", HttpStatus.OK, new PostAndChatDto(post, null));
//        }
        List<Chat> chats = chatRepository.findByPost_IdOrderByCreatedAtDesc(post.getId());


        List<CommentDto> chatting = new ArrayList<>();

        for(Chat chat: chats) {
            chatting.add(new CommentDto(chat.getComments(), chat.getLikeCount()));
        }

        return new PostChatStatusDto("Success", HttpStatus.OK, new PostAndChatDto(post, chatting));
    }

    @Transactional
    public ResponseStatusDto update(Long id, PostRequestDto requestDto, User user) {   //게시물 수정하기

//        if (postRepository.findById(id).isEmpty()) {
//            return new ResponseStatusDto("아이디가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
//        }
        Post post = postRepository.findById(id).orElseThrow();

        // 로그인한 유저의 비밀번호와 게시글 작성자의 비밀번호를 비교
        if(!post.getUser().getUsername().equals(user.getUsername())) {
            return new ResponseStatusDto("본인이 작성한 게시글만 수정 가능합니다.", HttpStatus.BAD_REQUEST);
        }
        post.update(requestDto, user);
        return new ResponseStatusDto("수정 성공!", HttpStatus.OK);

    }

    @Transactional
    public ResponseStatusDto delete(Long id, User user) {  //게시물 삭제하기

//        if (postRepository.findById(id).isEmpty()) {
//            return new ResponseStatusDto("아이디가 존재하지 않습니다", HttpStatus.BAD_REQUEST);
//        }
        Post post = postRepository.findById(id).orElseThrow();


        // 로그인한 유저의 비밀번호와 게시글 작성자의 유저 이름을 비교
        if(!post.getUser().getUsername().equals(user.getUsername())) {
            return new ResponseStatusDto("본인이 작성한 게시글만 삭제 가능합니다.", HttpStatus.BAD_REQUEST);
        }
        postRepository.deleteById(id);

        List<LikePost> posts = likePostRepository.findAllByPostId(id);

        for(LikePost posting: posts) {
            likePostRepository.deleteById(posting.getPostId());
        }

        return new ResponseStatusDto("삭제 성공!", HttpStatus.OK);
    }

    @Transactional          //id 는 post 의 id
    public ResponseStatusDto likeUpdate(Long id, User user) {   //게시물 좋아요

//        if (postRepository.findById(id).isEmpty()) {
//            return new ResponseStatusDto("아이디가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
//        }
        List<LikePost> posts = likePostRepository.findAllByUsername(user.getUsername());

        for(LikePost post: posts) {     //해당 유저가 좋아요한 게시물들을 들고옴
            if(post.getPostId().equals(id)) {                           //좋아요 취소
                postRepository.findById(id).orElseThrow().like(-1L);    //게시글의 좋아요 감소
                likePostRepository.deleteById(post.getId());            //유저의 좋아요한 게시글에서 삭제
                return new ResponseStatusDto("좋아요를 취소 하셨습니다!", HttpStatus.OK);
            }
        }
        postRepository.findById(id).orElseThrow().like(1L);
        likePostRepository.save(new LikePost(id, user.getUsername()));
        return new ResponseStatusDto("좋아요 성공!", HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public PostAndChatStatusDto likeGet(User user) {   //좋아요한 게시글 가져오기

//        if(postRepository.findAllByOrderByCreatedAtDesc().isEmpty()) {
//            return new PostAndChatStatusDto("게시물이 존재하지 않습니다.", HttpStatus.BAD_REQUEST, null);
//        }

        List<PostAndChatDto> responseDto = new ArrayList<>();   //반환할 거

        List<LikePost> posts = likePostRepository.findAllByUsername(user.getUsername());    //username 으로 좋아요한 게시글 들을 찾음
        for(LikePost post: posts) {     //게시글들의 아이디로 게시글들을 찾음
            Post posting = postRepository.findById(post.getPostId()).orElseThrow(); //게시글 ㅇㅇ

            List<Chat> chats = chatRepository.findByPost_IdOrderByCreatedAtDesc(posting.getId());   //게시글의 아이디로 채팅들을 찾음

            List<CommentDto> chatting = new ArrayList<>();

            for(Chat chat: chats) {
                chatting.add(new CommentDto(chat.getComments(), chat.getLikeCount()));   //채팅들을 추가
            }

            responseDto.add(new PostAndChatDto(posting, chatting));
        }

        return new PostAndChatStatusDto("Success", HttpStatus.OK, responseDto);
    }
}
