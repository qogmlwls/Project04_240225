package com.example.project04_240225.ViewModel;

import androidx.lifecycle.MutableLiveData;
import com.example.project04_240225.Model.Chat;
import com.example.project04_240225.Network.DTO.Data;

public class ChatViewModel {

    Chat chat;

    public ChatViewModel(Chat chat){
        this.chat = chat;
    }

    private MutableLiveData<Data> chatListResult = new MutableLiveData<>();
    public MutableLiveData<Data> getChatListResult() {
        return chatListResult;
    }



    private MutableLiveData<Data> chatsResult = new MutableLiveData<>();
    public MutableLiveData<Data> getChatsResult() {
        return chatsResult;
    }


    public void 채팅목록가져오기(){

        chat.채팅목록가져오기().observeForever(result -> {

            chatListResult.setValue(result);
        });

    }


    public void 채팅내용가져오기(int roomId){

        chat.채팅내용가져오기(roomId).observeForever(result -> {

            chatsResult.setValue(result);
        });

    }



}

