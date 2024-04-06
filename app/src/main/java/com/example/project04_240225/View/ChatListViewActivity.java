package com.example.project04_240225.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.project04_240225.Adapter.ChatListAdapter;
import com.example.project04_240225.Adapter.Data.ChatListItem;
import com.example.project04_240225.Model.Chat;
import com.example.project04_240225.Model.UserInfo;
import com.example.project04_240225.MyApplication;
import com.example.project04_240225.Network.DTO.Data;
import com.example.project04_240225.R;
import com.example.project04_240225.ViewModel.ChatViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ChatListViewActivity extends ToolBarCommonActivity {

    BottomNavigationView navigation;
    RecyclerView recyclerView;

    List<ChatListItem> list;
    ChatListAdapter chatListAdapter;

    MyApplication application;
    ChatViewModel chatViewModel;
    Chat chat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list_view);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        chatListAdapter = new ChatListAdapter<ChatListItem>(list,R.layout.item_chat_list);
        recyclerView.setAdapter(chatListAdapter);

        navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_challenger) {
                    return true;
                } else if (itemId == R.id.navigation_person) {
                    startActivity(MyInfoActivity.class);
                    finish();
                    return true;
                }
                return false;
            }
        });
        navigation.setSelectedItemId(R.id.navigation_challenger); // 선택 상태 업데이트

        // Toolbar 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar10);
        툴바뒤로가기설정(toolbar);


        // 서버에 데이터 요청
        application = (MyApplication) getApplication();
        chat = new Chat(application.getUserService(),application.getSharedPreferencesManager());
        chatViewModel = new ChatViewModel(chat);
        chatViewModel.getChatListResult().observe(ChatListViewActivity.this, result ->{

//            handleValidationResult(닉네임입력창,닉네임상태메세지뷰, result);
            if(result.result && result.response.has("result") && (result.response.get("result").getAsString()).equals("success")){
//                getAsJsonObject("data");
                JsonArray jsonArray = result.response.get("data").getAsJsonArray();
                for(int i=0;i<jsonArray.size();i++){
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    String roomName = jsonObject.get("roomName").getAsString();
                    int roomId = jsonObject.get("roomId").getAsInt();

                    ChatListItem chatListItem = new ChatListItem();
                    chatListItem.roomName = roomName;
                    chatListItem.roomId = roomId;
                    list.add(chatListItem);

                }
                chatListAdapter.notifyDataSetChanged();

            }


        });
        chatViewModel.채팅목록가져오기();


    }


    public<T> void startActivity(Class<T> tClass){
        Intent intent = new Intent(ChatListViewActivity.this, tClass);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 메모리 누수 방지
        // 옵저버를 제거하지 않으면, 엑티비티가 계속 살아있을 수 있음.
        // 강한 참조를 해서, 가비지 컬렉터가 엑티비티를 메모리에서 제거하지 못하여

        chatViewModel.getChatListResult().removeObservers(ChatListViewActivity.this);

    }
}