package com.trey.chat.repository;

import com.trey.chat.model.ChatMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMsgRepository extends JpaRepository<ChatMsg, String> {
    @Modifying
    @Query("update ChatMsg set sign_flag=1 where id=:id")
    void updateSignFlag(String id);
}