package com.example.project04_240225.Adapter.Data;

import java.util.List;

public class ChatListItem {

    public int roomId;

    public String roomName;
    List<String> profileImages;
    String lastChatContent;

    int 안읽은채팅수;
    int 채팅방인원수;
    
    boolean 알림활성화설정;
    String 시간;
    
}
