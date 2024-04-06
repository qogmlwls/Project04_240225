package com.example.project04_240225.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project04_240225.R;

import java.util.List;

public class MyAdapter<T> extends  RecyclerView.Adapter{
//        RecyclerView.Adapter<MyAdapter.MyViewHolder<T>>{
    //<MyAdapter.MyViewHolder<T>>
    private List<T> list; // 제네릭 타입의 데이터 리스트

    private int layoutId;
    public MyAdapter(List<T> list,int layoutId) {
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

//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder<T> holder, int position) {
//        // 데이터를 뷰 홀더의 뷰에 바인딩
//
//    }

    @Override
    public int getItemCount() {
        // 데이터 리스트의 전체 아이템 수 반환
        return list.size();
    }

    public class MyViewHolder<T> extends RecyclerView.ViewHolder {
//        TextView textView; // 아이템 뷰 내의 TextView

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            textView = itemView.findViewById(R.id.text_view); // item_layout.xml 내의 TextView ID
        }

        void bind(T item) {
//            // 데이터 객체를 문자열로 변환하여 TextView에 표시
//            textView.setText(item.toString());
        }
    }
}
