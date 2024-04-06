package com.example.project04_240225.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project04_240225.Adapter.Data.ChatListItem;
import com.example.project04_240225.View.ChatActivity;
import com.example.project04_240225.R;

import java.util.List;

public class ChatListAdapter<T>  extends RecyclerView.Adapter{

    public List<T> list; // 제네릭 타입의 데이터 리스트

    private int layoutId;
    public ChatListAdapter(List<T> list,int layoutId) {
        super();
        this.list = list;
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public MyViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 아이템 레이아웃을 인플레이트하여 ViewHolder 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new MyViewHolder<>(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        T item = list.get(position);
        MyViewHolder<T> myViewHolder = (MyViewHolder<T>) holder;
        myViewHolder.bind(item);
    }
    @Override
    public int getItemCount() {
        // 데이터 리스트의 전체 아이템 수 반환
        return list.size();
    }

    class MyViewHolder<T> extends RecyclerView.ViewHolder {
//        TextView textView; // 아이템 뷰 내의 TextView

        TextView 방이름텍스트뷰;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            방이름텍스트뷰 = itemView.findViewById(R.id.textView67);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i("MyViewHolder","d");
//                    Toast.makeText(itemView.getContext(), "test", Toast.LENGTH_SHORT).show();

//                    ChatActivity
                    // 클릭 시 아이템 정보를 가져옵니다.
                    int position = getAdapterPosition(); // 현재 ViewHolder의 포지션을 가져옵니다.
                    if (position != RecyclerView.NO_POSITION) { // 포지션이 유효한지 확인

                        ChatListItem item = (ChatListItem)list.get(position);

                        Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                        intent.putExtra("roomId", item.roomId);
                        itemView.getContext().startActivity(intent);

                    }
//                    Toast.makeText(v.getContext().getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
                }
            });
//            textView = itemView.findViewById(R.id.text_view); // item_layout.xml 내의 TextView ID
        }

        void bind(T item) {
//            // 데이터 객체를 문자열로 변환하여 TextView에 표시
            ChatListItem chatListItem = (ChatListItem)item;
            방이름텍스트뷰.setText(chatListItem.roomName);
//            textView.setText(item.toString());
        }
    }

}
