package com.sparta.posting.repository;

import com.sparta.posting.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    //Optional<List<Chat>> findAllByOrderByCreatedAtDesc();
    Optional<List<Chat>> findByPost_IdOrderByCreatedAtDesc(Long id);
}
