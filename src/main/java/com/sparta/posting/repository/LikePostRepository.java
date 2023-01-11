package com.sparta.posting.repository;

import com.sparta.posting.entity.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    Optional<List<LikePost>> findAllByUser_Id(Long Id);

    Optional<LikePost> findByPost_IdAndUser_Id(Long post_id, Long user_id);
}
