package com.example.project04_240225.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project04_240225.Model.UserInfo;
import com.example.project04_240225.Model.UserRepository;
import com.example.project04_240225.MyApplication;
import com.example.project04_240225.R;
import com.example.project04_240225.Result;
import com.example.project04_240225.Utility.ValidationUtil;
import com.example.project04_240225.ViewModel.SignUpViewModel;
import com.example.project04_240225.ViewModel.UserInfoViewModel;

public class EditNameActivity extends ToolBarCommonActivity {

    EditText 닉네임입력창;

    Toolbar 툴바;
    TextView 닉네임상태메세지뷰;
    Button 변경버튼;

    String 기존닉네임;

    ImageButton 입력값삭제버튼;

    Drawable 빨간색테두리배경색,회색테두리배경색,초록색테두리배경색;

    ValidationUtil validationUtil;
    UserInfoViewModel userInfoViewModel;
    UserInfo userInfo;
    MyApplication application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        닉네임입력창 = findViewById(R.id.editTextText9);
        툴바 = findViewById(R.id.toolbar6);
        닉네임상태메세지뷰 = findViewById(R.id.textView50);
        변경버튼 = findViewById(R.id.button19);
        입력값삭제버튼 = findViewById(R.id.imageButton6);

        기존닉네임 = getIntent().getStringExtra("기존닉네임");

        툴바뒤로가기설정(툴바);
        닉네임입력창.setHint(기존닉네임);

        application = (MyApplication) getApplication();
        userInfo = new UserInfo(application.getUserService(),application.getSharedPreferencesManager());
        validationUtil = new ValidationUtil();
        userInfoViewModel = new UserInfoViewModel(userInfo,validationUtil);


        입력값삭제버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                닉네임입력창.setText("");
            }
        });

        닉네임입력창.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 사용자가 텍스트를 입력하기 전에 조치를 취합니다.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 텍스트가 변경될 때마다 호출됩니다.

                String inputText = s.toString();
                if("".equals(inputText)){
                    입력값삭제버튼.setVisibility(View.GONE);
                }
                else{
                    입력값삭제버튼.setVisibility(View.VISIBLE);
                }
                userInfoViewModel.닉네임유효성검사(getText(닉네임입력창));

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 텍스트 입력이 완료된 후에 호출됩니다.

            }
        });



        userInfoViewModel.getNicknameValidationResult().observe(this, result ->{

            handleValidationResult(닉네임입력창,닉네임상태메세지뷰, result);

        });

        userInfoViewModel.getSetNameResult().observe(this, result ->{

//            handleValidationResult(닉네임입력창,닉네임상태메세지뷰, result);
            if(result.result && result.response.has("result") && (result.response.get("result").getAsString()).equals("success")){
                application.setChangeProfileNameResult(getText(닉네임입력창));
                finish();
            }

//            Toast.makeText(application, result.response.toString(), Toast.LENGTH_SHORT).show();

        });


        빨간색테두리배경색 = getDrawable(R.drawable.nickname_border_red);
        회색테두리배경색= getDrawable(R.drawable.nickname_border_gray);
        초록색테두리배경색= getDrawable(R.drawable.edit_text_border_green);

        변경버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userInfoViewModel.닉네임유효성검사(getText(닉네임입력창))){
                    // 서버에게 닉네임 변경 요청

                    userInfoViewModel.닉네임변경(getText(닉네임입력창));

                }


            }
        });

    }

    private String getText(EditText editText){
        return editText.getText().toString();
    }
    private void handleValidationResult(TextView messageView, Result result) {
        messageView.setText(result.결과메세지());
        messageView.setTextColor(result.메세지색상());
        messageView.setVisibility(result.메세지색상() == Color.TRANSPARENT ? View.GONE : View.VISIBLE);

    }
    private void handleValidationResult(EditText editText, TextView messageView, Result result) {

        handleValidationResult(messageView,result);
        if (result.isSuccess()) {
            editText.setBackground(회색테두리배경색);
            editText.setTextColor(Color.BLACK);
//            editText.setTag(통과);
        } else {
            editText.setBackground(빨간색테두리배경색);
            if(result.메세지색상() != Color.TRANSPARENT)
                editText.setTextColor(result.메세지색상());
//            editText.setTag(미통과);
        }
        messageView.setText(result.결과메세지());
        messageView.setTextColor(result.메세지색상());
        messageView.setVisibility(result.메세지색상() == Color.TRANSPARENT ? View.GONE : View.VISIBLE);

    }
}