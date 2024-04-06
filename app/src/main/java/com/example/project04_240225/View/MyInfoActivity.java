package com.example.project04_240225.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project04_240225.Model.UserInfo;
import com.example.project04_240225.MyApplication;
import com.example.project04_240225.Network.SharedPreferencesManager;
import com.example.project04_240225.R;
import com.example.project04_240225.Utility.ImageLoader;
import com.example.project04_240225.Utility.ValidationUtil;
import com.example.project04_240225.ViewModel.UserInfoViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

public class MyInfoActivity extends ToolBarCommonActivity {


    ConstraintLayout 프로필수정으로이동레이아웃;
    Button 프로필수정으로이동버튼;

    UserInfoViewModel userInfoViewModel;
    TextView 닉네임텍스트뷰;
    ImageView 프로필이미지뷰;
    MyApplication application;
    UserInfo userInfo;
    ValidationUtil validationUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        닉네임텍스트뷰 = findViewById(R.id.textView36);
        프로필이미지뷰 = findViewById(R.id.imageView3);
        프로필수정으로이동레이아웃 = findViewById(R.id.constraintLayout1);
        프로필수정으로이동버튼 = findViewById(R.id.button20);

        application = (MyApplication) getApplication();
        userInfo = new UserInfo(application.getUserService(),application.getSharedPreferencesManager());
        validationUtil = new ValidationUtil();
        userInfoViewModel = new UserInfoViewModel(userInfo,validationUtil);
        userInfoViewModel.getUserInfodata();

//        SharedPreferencesManager sharedPreferencesManager = application.getSharedPreferencesManager();
//        String type = sharedPreferencesManager.getLoginType();
//
//        Log.i("getLoginType result", type);

        userInfoViewModel.getUserInfoResult().observe(MyInfoActivity.this, userInfo -> {
            if (userInfo != null) {
                // 데이터 사용, 예: userInfo.getName(), userInfo.getProfileImageUrl()
                닉네임텍스트뷰.setText(userInfo.getName());
                if(userInfo.hasProfileImage()){

                    ImageLoader.loadProfileImage(
                            this, // 현재 Context
                            application.getBaseURL() +userInfo.getProfileImageUrl(), // 로드할 이미지 URL
                            프로필이미지뷰, // 이미지를 로드할 ImageView
                            프로필이미지뷰.getDrawable() // 플레이스홀더 이미지
                    );
                }
                else{
                    프로필이미지뷰.setImageDrawable(getDrawable(R.drawable.person));
                }

            }
            else{
                Toast.makeText(application, "프로필 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });


        application.getChangeProfileImageResult().observe(MyInfoActivity.this, profileImageUrl -> {

            if(profileImageUrl != null ){
                ImageLoader.loadProfileImage(
                        this, // 현재 Context
                        application.getBaseURL() +profileImageUrl, // 로드할 이미지 URL
                        프로필이미지뷰, // 이미지를 로드할 ImageView
                        프로필이미지뷰.getDrawable() // 플레이스홀더 이미지
                );
            }
            else{
                프로필이미지뷰.setImageDrawable(getDrawable(R.drawable.person));
            }

        });
        application.getChangeProfileNameResult().observe(MyInfoActivity.this, name -> {

            닉네임텍스트뷰.setText(name);

        });


        프로필수정으로이동레이아웃.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EditProfileActivity.class);
            }
        });

        프로필수정으로이동버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EditProfileActivity.class);
            }
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView2);
        navigation.setSelectedItemId(R.id.navigation_person); // 선택 상태 업데이트

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_challenger) {

                    startActivity(ChatListViewActivity.class);
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_person) {
                    return true;
                }
                return false;
            }
        });

        // Toolbar 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        툴바뒤로가기설정(toolbar);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 메모리 누수 방지
        // 옵저버를 제거하지 않으면, 엑티비티가 계속 살아있을 수 있음.
        // 강한 참조를 해서, 가비지 컬렉터가 엑티비티를 메모리에서 제거하지 못하여
        application.getChangeProfileNameResult().removeObservers(MyInfoActivity.this);
        application.getChangeProfileImageResult().removeObservers(MyInfoActivity.this);
        userInfoViewModel.getUserInfoResult().removeObservers(MyInfoActivity.this);

    }


    public<T> void startActivity(Class<T> tClass){
//        new Intent(MainActivity.this,MyInfoActivity.class)
        Intent intent = new Intent(MyInfoActivity.this, tClass);
        startActivity(intent);
    }

}

