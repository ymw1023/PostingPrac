package com.sparta.posting.repository;

import com.sparta.posting.entity.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    List<LikePost> findAllByUsername(String name);

    List<LikePost> findAllByPostId(Long id);
}
