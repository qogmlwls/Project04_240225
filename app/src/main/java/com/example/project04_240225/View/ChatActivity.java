package com.example.project04_240225.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.project04_240225.Adapter.ChatListAdapter;
import com.example.project04_240225.Adapter.ChatsAdapter;
import com.example.project04_240225.Adapter.Data.ChatItem;
import com.example.project04_240225.Adapter.Data.ChatListItem;
import com.example.project04_240225.Model.Chat;
import com.example.project04_240225.MyApplication;
import com.example.project04_240225.R;
import com.example.project04_240225.ViewModel.ChatViewModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    MyApplication application;
    ChatViewModel chatViewModel;
    Chat chat;

    RecyclerView recyclerView;

    List<ChatItem> list;
    ChatsAdapter chatsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        int roomId;
        roomId = getIntent().getIntExtra("roomId",-1);
//        Toast.makeText(this, Integer.toString(roomId), Toast.LENGTH_SHORT).show();


        recyclerView = findViewById(R.id.recyclerView4);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        chatsAdapter = new ChatsAdapter(list);
        recyclerView.setAdapter(chatsAdapter);



        application = (MyApplication) getApplication();
        chat = new Chat(application.getUserService(),application.getSharedPreferencesManager());
        chatViewModel = new ChatViewModel(chat);
        // 채팅 내용 가져오기
        // 서버에 데이터 요청
        chatViewModel.getChatsResult().observe(ChatActivity.this, result ->{

            Toast.makeText(application, "GETCHATS", Toast.LENGTH_SHORT).show();
            if(result.result && result.response.has("result") && (result.response.get("result").getAsString()).equals("success")){
//                getAsJsonObject("data");
                JsonArray jsonArray = result.response.get("data").getAsJsonArray();
                for(int i=0;i<jsonArray.size();i++){
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                    String content = jsonObject.get("content").getAsString();
                    boolean isMyChat = jsonObject.get("isMyChat").getAsBoolean();

                    String name = jsonObject.get("name").getAsString();

                    ChatItem chatItem = new ChatItem();
                    chatItem.content = content;
                    chatItem.isMyChat = isMyChat;
                    chatItem.userName = name;

                    list.add(chatItem);

                    Toast.makeText(application, Integer.toString(list.size()), Toast.LENGTH_SHORT).show();
                }
                chatsAdapter.notifyDataSetChanged();

            }

        });
        chatViewModel.채팅내용가져오기(roomId);




    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 메모리 누수 방지
        // 옵저버를 제거하지 않으면, 엑티비티가 계속 살아있을 수 있음.
        // 강한 참조를 해서, 가비지 컬렉터가 엑티비티를 메모리에서 제거하지 못하여

//        chatViewModel.getChatListResult().removeObservers(ChatActivity.this);

    }

}