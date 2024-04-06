package com.example.project04_240225.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.project04_240225.Adapter.Data.ChatItem;
import com.example.project04_240225.R;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter{

    public List<ChatItem> list; // 제네릭 타입의 데이터 리스트
    // 메시지 타입을 구분하는 상수
    private static final int MESSAGE_TYPE_LEFT = 0;
    private static final int MESSAGE_TYPE_RIGHT = 1;

//    private int layoutId;
    public ChatsAdapter(List<ChatItem> list) {//,int layoutId
        super();
        this.list = list;
//        this.layoutId = layoutId;
    }


    @Override
    public int getItemViewType(int position) {
        ChatItem chatItem = list.get(position);
        // 메시지가 발신인지 수신인지에 따라 뷰 타입 결정
        if (chatItem.isMyChat) {
            return MESSAGE_TYPE_RIGHT;
        } else {
            return MESSAGE_TYPE_LEFT;
        }
    }


//    @NonNull
//    @Override
//    public ChatListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // 아이템 레이아웃을 인플레이트하여 ViewHolder 생성
//
//        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
//        return new ChatListAdapter.MyViewHolder<>(view);
//    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right_chat, parent, false);
            return new RightViewHolder(view);
        } else { // MESSAGE_TYPE_LEFT
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_chat, parent, false);
            return new LeftViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ChatItem message = list.get(position);
        if (holder.getItemViewType() == MESSAGE_TYPE_RIGHT) {
            ((RightViewHolder) holder).bind(message);
        } else {
            ((LeftViewHolder) holder).bind(message);
        }
    }
    @Override
    public int getItemCount() {
        // 데이터 리스트의 전체 아이템 수 반환
        return list.size();
    }


    // 수신 메시지를 위한 ViewHolder
    class LeftViewHolder extends RecyclerView.ViewHolder {
        // 뷰 바인딩 로직

        TextView 채팅내용텍스트뷰, 닉네임텍스트뷰;
        public LeftViewHolder(View itemView) {
            super(itemView);
            // 뷰 초기화
            채팅내용텍스트뷰 = itemView.findViewById(R.id.textView78);
            닉네임텍스트뷰 = itemView.findViewById(R.id.textView77);

        }

        public void bind(ChatItem message) {
            // 메시지 내용 바인딩
            채팅내용텍스트뷰.setText(message.content);
            닉네임텍스트뷰.setText(message.userName);
        }
    }

    // 발신 메시지를 위한 ViewHolder
    class RightViewHolder extends RecyclerView.ViewHolder {
        // 뷰 바인딩 로직
        TextView 채팅내용텍스트뷰;
        public RightViewHolder(View itemView) {
            super(itemView);
            // 뷰 초기화
            채팅내용텍스트뷰 = itemView.findViewById(R.id.textView72);

        }

        public void bind(ChatItem message) {
            // 메시지 내용 바인딩
            채팅내용텍스트뷰.setText(message.content);

        }
    }
}

