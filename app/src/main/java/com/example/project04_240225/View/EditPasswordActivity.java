package com.example.project04_240225.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project04_240225.Model.UserInfo;
import com.example.project04_240225.MyApplication;
import com.example.project04_240225.R;
import com.example.project04_240225.Utility.ValidationUtil;
import com.example.project04_240225.ViewModel.UserInfoViewModel;

public class EditPasswordActivity extends ToolBarCommonActivity {



    EditText 현재비밀번호입력창, 새비밀번호입력창, 새비밀번호확인입력창,자동입력방지문자입력창;
    TextView 현재비밀번호상태메세지창, 새비밀번호상태메세지창, 새비밀번호확인상태메세지창, 자동입력방지문자상태메세지창;
    Button 확인버튼, 취소버튼;

    ValidationUtil validationUtil;
    UserInfoViewModel userInfoViewModel;
    UserInfo userInfo;
    MyApplication application;

    ImageView 자동입력방지이미지;

    Toolbar 툴바;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);


        현재비밀번호입력창 = findViewById(R.id.editTextTextPassword5);
        새비밀번호입력창  = findViewById(R.id.editTextTextPassword6);
        새비밀번호확인입력창  = findViewById(R.id.editTextTextPassword7);
        자동입력방지문자입력창  = findViewById(R.id.editTextText11);


        현재비밀번호상태메세지창 = findViewById(R.id.textView52);
        새비밀번호상태메세지창 = findViewById(R.id.textView53);
        새비밀번호확인상태메세지창 = findViewById(R.id.textView54);
        자동입력방지문자상태메세지창 = findViewById(R.id.textView56);


        툴바 = findViewById(R.id.toolbar7);
        자동입력방지이미지 = findViewById(R.id.imageView7);

        확인버튼 = findViewById(R.id.button23);
        취소버튼 = findViewById(R.id.button24);


        application = (MyApplication) getApplication();
        userInfo = new UserInfo(application.getUserService(),application.getSharedPreferencesManager());
        validationUtil = new ValidationUtil();
        userInfoViewModel = new UserInfoViewModel(userInfo,validationUtil);

        userInfoViewModel.이미지가져오기();

        툴바뒤로가기설정(툴바);

        userInfoViewModel.getImageResult().observe(this, result -> {
            if (result != null) {
                String encodedImage = result.결과메세지();
                // Base64 문자열을 Bitmap으로 디코딩하여 ImageView에 설정
                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                자동입력방지이미지.setImageBitmap(decodedByte);
            } else {
                // 에러 처리 또는 기본 이미지 설정
                자동입력방지이미지.setImageResource(R.drawable.ic_launcher_foreground);
//                자동입력방지이미지.setImageResource(R.drawable.default_image);
            }
        });


        userInfoViewModel.getSetPwResult().observe(EditPasswordActivity.this,result -> {

            if(result.result){

                if(result.response != null){
                    if(result.response.has("result")){

                        String status = result.response.get("result").getAsString();
                        if("success".equals(status)){

                            Toast.makeText(application, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        else if("fail".equals(status)){

                            Toast.makeText(application, "현재 비밀번호 또는 자동입력방지문자를 확인해주세요", Toast.LENGTH_SHORT).show();
                            // 자동입력방지문자 실패시, 자동입력방지문자 이미지 다시 가져오기
                        }
                        else{
                            Toast.makeText(application, "Error1", Toast.LENGTH_SHORT).show();

                        }

                    }
                    else{


                        Toast.makeText(application, "Error2", Toast.LENGTH_SHORT).show();

                    }


                }
                else{
                    Toast.makeText(application, "Error3", Toast.LENGTH_SHORT).show();

                }

            }
            else{
                Toast.makeText(application, "Error4", Toast.LENGTH_SHORT).show();

            }


        });


        확인버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // 형식검사 통과시
                // 서버에 비밀번호 변경 요청
                String 현재비밀번호 = 현재비밀번호입력창.getText().toString();
                String 새비밀번호 = 새비밀번호입력창.getText().toString();
                String 새비밀번호확인 = 새비밀번호확인입력창.getText().toString();
                String 자동입력방지문자 = 자동입력방지문자입력창.getText().toString();
                ValidationUtil validationUtil = new ValidationUtil();

                현재비밀번호상태메세지창.setVisibility(View.GONE);
                새비밀번호상태메세지창.setVisibility(View.GONE);
                새비밀번호확인상태메세지창.setVisibility(View.GONE);
                자동입력방지문자상태메세지창.setVisibility(View.GONE);

                boolean result = true;

                if(현재비밀번호.equals("")){
                    현재비밀번호상태메세지창.setText("현재 비밀번호를 입력해주세요.");
                    현재비밀번호상태메세지창.setVisibility(View.VISIBLE);
                    result = false;
                }


                if(새비밀번호.equals("")){
                    새비밀번호상태메세지창.setText("새 비밀번호를 입력해주세요.");
                    새비밀번호상태메세지창.setVisibility(View.VISIBLE);
                    result = false;
                }
                else if(!validationUtil.pw_validate(새비밀번호)){
                    // 형식검사
//                    비밀번호는 8~16자의 영문 대소문자와 숫자, 특수문자를 사용할 수 있습니다.
//                    passwordValidationResult.setValue(new Result(false,"비밀번호: 8~16자의 영문 대/소문자, 숫자, 특수문자만 사용 가능합니다.", Color.RED));
                    새비밀번호상태메세지창.setText(" 8~16자의 영문 대/소문자, 숫자, 특수문자만 사용 가능합니다.");
                    새비밀번호상태메세지창.setVisibility(View.VISIBLE);
                    result = false;
                }


                if(새비밀번호확인.equals("")){
                    새비밀번호확인상태메세지창.setText("새 비밀번호 확인을 입력해주세요.");
                    새비밀번호확인상태메세지창.setVisibility(View.VISIBLE);
                    result = false;
                }
                else if(!새비밀번호.equals(새비밀번호확인)){
                    새비밀번호확인상태메세지창.setText("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                    새비밀번호확인상태메세지창.setVisibility(View.VISIBLE);
                    result = false;
                }


                if(자동입력방지문자.equals("")){
                    자동입력방지문자상태메세지창.setText("자동입력방지문자를 입력해주세요.");
                    자동입력방지문자상태메세지창.setVisibility(View.VISIBLE);
                    result = false;
                }


                if(result){
                    // 비밀번호 변경 요청
                    // 아이디와 새로운 비밀번호 보냄.
//                    Toast.makeText(FindAccountPwActivity.this, "비밀번호 변경 요청", Toast.LENGTH_SHORT).show();
//                    ResetPasswordController
                    // 아이디, 비밀번호, 인증종류, 인증데이터, 인증번호, 자동입력인증문자 데이터 서버에 전송하면서
                    // 비밀번호 재설정 요청.
                    userInfoViewModel.비밀번호재설정(현재비밀번호,새비밀번호,자동입력방지문자);

                }




            }
        });


        취소버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }
}