package com.sparta.posting.service;

import com.sparta.posting.dto.*;
import com.sparta.posting.entity.*;
import com.sparta.posting.repository.ChatRepository;
import com.sparta.posting.repository.LikePostRepository;
import com.sparta.posting.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
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
    public PostAndChatStatusDto find(HttpServletResponse response) {   //게시물 전체 조회하기

        if(postRepository.findAllByOrderByCreatedAtDesc().isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            throw new IllegalArgumentException("해당 아이디의 게시물이 존재하지 않습니다.");
        }
        List<Post> post = postRepository.findAllByOrderByCreatedAtDesc().get();


        List<PostAndChatDto> responseDto = new ArrayList<>();
        post.forEach(posting -> {
            List<CommentDto> chatting = new ArrayList<>();

            if (chatRepository.findByPost_IdOrderByCreatedAtDesc(posting.getId()).isEmpty()) {
                responseDto.add(new PostAndChatDto(posting, chatting));     //여기는 error 를 안내고 chat 을 빈칸으로 반환
                return;
            }
            List<Chat> chats = chatRepository.findByPost_IdOrderByCreatedAtDesc(posting.getId()).get();

            for(Chat chat: chats) {
                chatting.add(new CommentDto(chat.getComments(), chat.getLikeCount()));
            }

            responseDto.add(new PostAndChatDto(posting, chatting));
        });

        return new PostAndChatStatusDto("Success", HttpStatus.OK, responseDto);
    }

    @Transactional
    public PostChatStatusDto findOne(Long id, HttpServletResponse response) {    //게시물 선택 조회하기

        Post post = checkPost(id, response);

        List<CommentDto> chatting = new ArrayList<>();  //비어잇는 댓글 리스트

        if(chatRepository.findByPost_IdOrderByCreatedAtDesc(post.getId()).isPresent()) {    //댓글이 있으면 리스트에 추가
            List<Chat> chats = chatRepository.findByPost_IdOrderByCreatedAtDesc(post.getId()).get();
            for(Chat chat: chats) {
                chatting.add(new CommentDto(chat.getComments(), chat.getLikeCount()));
            }
        }

        return new PostChatStatusDto("Success", HttpStatus.OK, new PostAndChatDto(post, chatting));
    }

    @Transactional
    public ResponseStatusDto update(Long id, PostRequestDto requestDto, User user, HttpServletResponse response) {   //게시물 수정하기

        Post post = checkPost(id, response);

        // 로그인한 유저의 비밀번호와 게시글 작성자의 비밀번호를 비교
        if(!(post.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN)) {    //게시글 유저이름이 현재 유저이름과 다르고 어드민 계정이아니면 if 문실행
            return new ResponseStatusDto("본인이 작성한 게시글만 수정 가능합니다.", HttpStatus.BAD_REQUEST);
        }
        post.update(requestDto, user);
        return new ResponseStatusDto("수정 성공!", HttpStatus.OK);

    }

    @Transactional
    public ResponseStatusDto delete(Long id, User user, HttpServletResponse response) {  //게시물 삭제하기

        Post post = checkPost(id, response);

        // 로그인한 유저의 비밀번호와 게시글 작성자의 유저 이름을 비교
        if(!post.getUser().getUsername().equals(user.getUsername())) {
            return new ResponseStatusDto("본인이 작성한 게시글만 삭제 가능합니다.", HttpStatus.BAD_REQUEST);
        }
        postRepository.deleteById(id);

        return new ResponseStatusDto("삭제 성공!", HttpStatus.OK);
    }

    @Transactional          //id 는 post 의 id
    public ResponseStatusDto likeUpdate(Long id, User user, HttpServletResponse response) {   //게시물 좋아요

        Post post = checkPost(id, response);

        if (likePostRepository.findByPost_IdAndUser_Id(post.getId(), user.getId()).isPresent()) {   //post id 와 user id 가 일치하는 likeChat 이 존재하는 경우
            postRepository.findById(post.getId()).orElseThrow().like(-1L);
            likePostRepository.deleteById(likePostRepository.findByPost_IdAndUser_Id(post.getId(), user.getId()).get().getId());
            return new ResponseStatusDto("좋아요 취소!", HttpStatus.OK);
        }

        postRepository.findById(id).orElseThrow().like(1L);
        likePostRepository.save(new LikePost(post, user));
        return new ResponseStatusDto("좋아요 성공!", HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public PostAndChatStatusDto likeGet(User user, HttpServletResponse response) {   //좋아요한 게시글 가져오기

        List<PostAndChatDto> responseDto = new ArrayList<>();   //반환할 거

        if(likePostRepository.findAllByUser_Id(user.getId()).isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            throw new IllegalArgumentException("좋아요를 누르신 게시글이 존재하지 않습니다.");
        }
        List<LikePost> posts = likePostRepository.findAllByUser_Id(user.getId()).get();

        for(LikePost post: posts) { //현재 유저의 좋아요한 게시글들의 리스트    //게시글들의 아이디와 유저 아이디로 찾음 으로써 indexing 및 데이터가 엄청 많을 경우 용이!?
            Post posting = likePostRepository.findByPost_IdAndUser_Id(post.getId(), user.getId()).orElseThrow().getPost(); //위에서 예외 처리를 했기 때문에 예외 안나옴

            List<Chat> chats = chatRepository.findByPost_IdOrderByCreatedAtDesc(posting.getId()).orElseThrow();   //게시글의 아이디로 모든 사람 채팅들을 찾음

            List<CommentDto> chatting = new ArrayList<>();

            for(Chat chat: chats) {
                chatting.add(new CommentDto(chat.getComments(), chat.getLikeCount()));   //채팅들을 추가
            }

            responseDto.add(new PostAndChatDto(posting, chatting));
        }

        return new PostAndChatStatusDto("Success", HttpStatus.OK, responseDto);
    }

    public Post checkPost(Long id, HttpServletResponse response) {
        if(postRepository.findById(id).isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            throw new IllegalArgumentException("해당 아이디의 게시글이 존재하지 않습니다.");
        }
        return postRepository.findById(id).get();
    }
}
