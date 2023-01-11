package com.sparta.posting.repository;

import com.sparta.posting.entity.LikeChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeChatRepository extends JpaRepository<LikeChat, Long> {
    Optional<LikeChat> findByChat_IdAndUser_Id(Long chat_id, Long user_id);
}