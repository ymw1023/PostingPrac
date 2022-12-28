package com.sparta.posting.repository;

import com.sparta.posting.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    List<Posts> findAllByOrderByModifiedAtDesc();
    List<Posts> findPostsByTitleIsOrderByModifiedAtDesc(String title);
    List<Posts> findPostsByUsernameIsOrderByModifiedAtDesc(String username);
}
