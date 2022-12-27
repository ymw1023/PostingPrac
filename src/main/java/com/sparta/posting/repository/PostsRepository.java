package com.sparta.posting.repository;

import com.sparta.posting.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
//    List<UserInfoMapping> findAllByOrderByModifiedAtDesc();     //비밀번호, 만든시간 출력안함
    List<Posts> findAllByOrderByModifiedAtDesc();
    List<Posts> findPostsByTitleIsOrderByModifiedAtDesc(String title);
    List<Posts> findPostsByUsernameIsOrderByModifiedAtDesc(String username);
}
