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

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;  //외래키를 사용한 인덱스

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public LikePost(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}
