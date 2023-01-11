package com.sparta.posting.entity;

import com.sparta.posting.dto.ChatRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Chat extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<LikeChat> likeChats = new ArrayList<>();

    @Column(nullable = false)
    private String comments;

    @Column(nullable = false)
    private Long likeCount;

    public Chat(ChatRequestDto requestDto, Post post, User user) {
        this.comments = requestDto.getComments();
        this.post = post;
        this.user = user;
        this.likeCount = 0L;
    }

    public void like(Long like) {
        this.likeCount += like;
    }

    public void update(ChatRequestDto requestDto) {
        this.comments = requestDto.getComments();
    }
}
