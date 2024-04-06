package com.example.project04_240225.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project04_240225.Model.UserRepository;
import com.example.project04_240225.Network.SharedPreferencesManager;
import com.example.project04_240225.R;
import com.example.project04_240225.Result;
import com.example.project04_240225.ViewModel.LoginViewModel;

public class SNSSignUpActivity extends AppCompatActivity {


    UserRepository userRepository;

    LoginViewModel loginViewModel;
    SharedPreferencesManager sharedPreferencesManager;


    String 회원번호, 닉네임,가입종류;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snssign_up);

        TextView 약관설명메세지창;
        약관설명메세지창 = findViewById(R.id.textView63);


        회원번호 = getIntent().getStringExtra("회원번호");
        닉네임 = getIntent().getStringExtra("닉네임");
        가입종류 = getIntent().getStringExtra("가입종류");


        String text = getText(약관설명메세지창); // 약관 설명 메시지를 가져옵니다.
        SpannableString spannableString = new SpannableString(text);
        String[] termsArray = {"서비스 이용약관", "개인정보 수집 및 이용 동의 약관"};

        for (int i = 0; i < termsArray.length; i++) {
            int startIdx = text.indexOf(termsArray[i]);
            if (startIdx != -1) { // 텍스트가 존재하면
                int endIdx = startIdx + termsArray[i].length();
                int finalI = i;

                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {

                        Intent intent = new Intent(SNSSignUpActivity.this, TermActivity.class)
                                .putExtra("type", TermActivity.types[finalI]);
                        startActivity(intent);

                    }
                }, startIdx, endIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        약관설명메세지창.setText(spannableString);
        약관설명메세지창.setLinkTextColor(getColor(R.color.green));
        약관설명메세지창.setMovementMethod(LinkMovementMethod.getInstance()); // 클릭 이벤트 활성화



        userRepository = new UserRepository(getApplicationContext());
        loginViewModel = new LoginViewModel(userRepository);
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());


        Button 가입하기버튼;
        가입하기버튼 = findViewById(R.id.button28);

        가입하기버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(SNSSignUpActivity.this, "가입하기", Toast.LENGTH_SHORT).show();
//                finish();
//                SNSJoinController
                loginViewModel.RequestSNSJoin(닉네임, 가입종류, 회원번호);

//                $userId = $data['user_id'];
//                $type = $data['sns_type'];
//                $username = $data['sns_name'];

            }
        });

        loginViewModel.getSNSJoinResult().observe(SNSSignUpActivity.this, result -> {
            if(result.isSuccess()){

                Intent intent = new Intent(SNSSignUpActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                sharedPreferencesManager.saveLoginType(가입종류);
                if(가입종류.equals("KAKAO")){
                    sharedPreferencesManager.removeLoginType2();
                }
                Log.i("sharedPreferencesManager",sharedPreferencesManager.getLoginType());
//                String type = sharedPreferencesManager.getLoginType();
//                if(type == null){
//                    Toast.makeText(this, "type null", Toast.LENGTH_SHORT).show();
//                }
//                else if(!sharedPreferencesManager.getLoginType().equals("KAKAO")){
//                    Toast.makeText(this, "type value wrong", Toast.LENGTH_SHORT).show();
//                    Log.i("type value",type);
//                }

                startActivity(intent);

            }
            else{

            }

        });

//
//        회원번호 = getIntent().getStringExtra("회원번호");
//        닉네임 = getIntent().getStringExtra("닉네임");
//        가입종류 = getIntent().getStringExtra("가입종류");




    }


    private String getText(TextView textView){
        return textView.getText().toString();
    }


}