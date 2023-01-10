package com.sparta.posting.repository;

import com.sparta.posting.entity.LikeChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeChatRepository extends JpaRepository<LikeChat, Long> {
    List<LikeChat> findAllByUsername(String username);

    List<LikeChat> findAllByChatId(Long id);
}