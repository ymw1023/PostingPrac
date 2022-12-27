package com.sparta.posting.repository;

import java.time.LocalDateTime;

public interface UserInfoMapping {
    String getUsername();
    String getTitle();
    String getContents();
    LocalDateTime getModifiedAt();
}
