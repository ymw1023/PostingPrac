package com.sparta.posting.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class LikePost { //이 테이블은 유저 쪽에서 좋아요한 포스트를 파악하기 위해서
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String username;

    public LikePost(Long postId, String username) {
        this.postId = postId;
        this.username = username;
    }
}
