package com.example.project04_240225.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project04_240225.Model.UserRepository;
import com.example.project04_240225.R;
import com.example.project04_240225.ViewModel.LoginViewModel;
import com.example.project04_240225.ViewModel.ViewModel;
import com.example.project04_240225.adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Button 로그아웃버튼;

    TextView 닉네임;


    UserRepository userRepository;
    ViewModel viewModel;
    BottomNavigationView navigation;

    RecyclerView recyclerView,recyclerView2,recyclerView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageButton imageButton, imageButton1, imageButton3;
        imageButton = findViewById(R.id.imageButton11);
        imageButton1 = findViewById(R.id.imageButton13);
        imageButton3 = findViewById(R.id.imageButton12);

        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView3 = findViewById(R.id.recyclerView3);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                ViewHolder vh = new ViewHolder(mView);
                return vh;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 15;
            }
            class ViewHolder extends  RecyclerView.ViewHolder{

                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                }
            }
        });


        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                ViewHolder vh = new ViewHolder(mView);
                return vh;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 15;
            }
            class ViewHolder extends  RecyclerView.ViewHolder{

                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                }
            }
        });
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        recyclerView3.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                ViewHolder vh = new ViewHolder(mView);
                return vh;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 15;
            }
            class ViewHolder extends  RecyclerView.ViewHolder{

                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                }
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
            }
        });
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView2.setVisibility(View.GONE);
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView3.setVisibility(View.GONE);
            }
        });

        navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView1);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {

//                        Toast.makeText(MainActivity.this, "Home Selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.navigation_challenger) {
//                        Toast.makeText(MainActivity.this, "Dashboard Selected", Toast.LENGTH_SHORT).show();
                    startActivity(ChatListViewActivity.class);
                    return true;
                } else if (itemId == R.id.navigation_person) {
//                        Toast.makeText(MainActivity.this, "Notifications Selected", Toast.LENGTH_SHORT).show();
                    startActivity(MyInfoActivity.class);
                    return true;
                }
                return false;
            }
        });


        로그아웃버튼 = findViewById(R.id.button9);
        닉네임 = findViewById(R.id.textView11);
        userRepository = new UserRepository(getApplicationContext());

        viewModel = new ViewModel(userRepository);


        로그아웃버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewModel.로그아웃();

            }
        });

        viewModel.getLogoutResult().observe(MainActivity.this, result -> {
            if(result.isSuccess()){

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

                finish();
            }
            else{

            }
        });
        viewModel.getNameResult().observe(MainActivity.this, result -> {
            닉네임.setText(result);
        });
        viewModel.닉네임가져오기();


    }


    @Override
    protected void onRestart() {

        navigation.setSelectedItemId(R.id.navigation_home); // 선택 상태 업데이트
        super.onRestart();
    }

    public<T> void startActivity(Class<T> tClass){
//        new Intent(MainActivity.this,MyInfoActivity.class)
        Intent intent = new Intent(MainActivity.this, tClass);
        startActivity(intent);
    }






}