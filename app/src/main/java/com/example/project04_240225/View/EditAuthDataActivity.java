package com.example.project04_240225.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.project04_240225.AppSignature;
import com.example.project04_240225.Model.UserInfo;
import com.example.project04_240225.Model.UserRepository;
import com.example.project04_240225.MyApplication;
import com.example.project04_240225.R;
import com.example.project04_240225.Result;
import com.example.project04_240225.Utility.ValidationUtil;
import com.example.project04_240225.ViewModel.FindAccountViewModel;
import com.example.project04_240225.ViewModel.UserInfoViewModel;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class EditAuthDataActivity extends ToolBarCommonActivity {


    static final int EMAIL = 100, PHONE = 101;

    RadioGroup 인증선택그룹;
  //  TextView 휴대전화번호인증설명메세지, 이메일인증설명메세지;

    EditText 인증데이터입력창, 인증번호입력창;

    Button 인증번호받기버튼, 다음버튼;//, 로그인화면으로이동버튼, 비밀번호찾기화면으로이동버튼;

    TextView 인증데이터상태메세지, 인증상태메세지;//, 인증안내메세지;//,아이디보여주는뷰;

    FindAccountViewModel findAccountIdViewModel;
    UserRepository userRepository;

    private BroadcastReceiver mReceiver;
    Drawable 빨간색테두리배경색, 회색테두리배경색, 초록색테두리배경색;
    final String 미검사 = "미검사", 미통과 = "미통과", 통과 = "통과";
    int authType;
    final int 인증데이터 = 3, 인증코드 = 4;
    Toolbar 툴바;

//    ConstraintLayout 인증창;
//    LinearLayout 아이디보여주기창;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_auth_data);

        authType = EMAIL;

        툴바 = findViewById(R.id.toolbar8);
        인증선택그룹 = findViewById(R.id.radioGroup4);

        인증데이터입력창 = findViewById(R.id.editText2);
        인증번호입력창 = findViewById(R.id.editTextNumber3);
        인증번호받기버튼 = findViewById(R.id.button26);
        다음버튼 = findViewById(R.id.button25);

        인증데이터상태메세지 = findViewById(R.id.textView57);
        인증상태메세지 = findViewById(R.id.textView58);

        userRepository = new UserRepository(getApplicationContext());
        findAccountIdViewModel = new FindAccountViewModel(userRepository);

        인증데이터입력창.setTag(미검사);
        인증번호입력창.setTag(미검사);

        빨간색테두리배경색 = getDrawable(R.drawable.edit_text_border_red);
        회색테두리배경색 = getDrawable(R.drawable.edit_text_border_gray);
        초록색테두리배경색 = getDrawable(R.drawable.edit_text_border_green);


        // BroadcastReceiver 초기화 및 등록
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("CustomAction".equals(action)) {
                    String code = intent.getStringExtra("EXTRA_CODE");
                    Log.i("Received Code", code);
                    인증번호입력창.setText(code);

                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("CustomAction"));


        findAccountIdViewModel.getVerificationCodeValidationResult().observe(this, result ->{

            handleValidationResult(인증데이터입력창,인증데이터상태메세지, result);
        });


        findAccountIdViewModel.getVerificationCodeSendResult().observe(this, result ->{

            handleValidationResult(인증데이터입력창,인증상태메세지, result);

        });

        findAccountIdViewModel.getVerificationCodeCheckResult().observe(this, result -> {
            인증번호입력창.setBackground(회색테두리배경색);
            handleValidationResult(인증상태메세지, result);
        });
        findAccountIdViewModel.getFindAccountIdResultResult().observe(this, result -> {

            if(result.isSuccess()){
//                인증창.setVisibility(View.GONE);
//                아이디보여주기창.setVisibility(View.VISIBLE);
//                아이디보여주는뷰.setText(result.결과메세지());
            }
            else{
                // 에러메세지
                handleValidationResult(인증데이터입력창,인증상태메세지, result);
            }

        });

        인증데이터입력창.setOnFocusChangeListener(new FocusChangeListener(인증데이터));
        인증번호입력창.setOnFocusChangeListener(new FocusChangeListener(인증코드));

        인증선택그룹.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (checkedId == R.id.radioButton6) {
//                    휴대전화번호인증설명메세지.setVisibility(View.VISIBLE);
//                    이메일인증설명메세지.setVisibility(View.GONE);
                    인증데이터입력창.setInputType(InputType.TYPE_CLASS_PHONE);

                    인증데이터입력창.setHint("휴대전화번호");
                    authType = PHONE;
                } else if (checkedId == R.id.radioButton5) {
//                    휴대전화번호인증설명메세지.setVisibility(View.GONE);
//                    이메일인증설명메세지.setVisibility(View.VISIBLE);
                    인증데이터입력창.setInputType(InputType.TYPE_CLASS_TEXT);

                    인증데이터입력창.setHint("이메일");
                    authType = EMAIL;
                }

                인증데이터상태메세지.setVisibility(View.GONE);
                인증상태메세지.setVisibility(View.GONE);

                인증데이터입력창.setTag(미검사);

                if(인증데이터입력창.hasFocus()){
                    인증데이터입력창.setBackground(초록색테두리배경색);
                }
                else{
                    인증데이터입력창.setBackground(회색테두리배경색);
                }
                인증데이터입력창.setTextColor(Color.BLACK);
                인증데이터입력창.setText("");
                인증번호입력창.setText("");

            }
        });

        인증번호받기버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getCurrentFocus() != null) {
                    getCurrentFocus().clearFocus();
                }

                if(findAccountIdViewModel.인증데이터형식검사(authType,getText(인증데이터입력창))){

                    if(authType==EMAIL){
                        findAccountIdViewModel.인증번호발송요청(UserRepository.중복인지, authType,getText(인증데이터입력창),null);
                    }
                    else if(authType == PHONE){
                        getSMS();
                    }

                }

            }
        });
        MyApplication application = (MyApplication) getApplication();
        UserInfo userInfo = new UserInfo(application.getUserService(),application.getSharedPreferencesManager());
        ValidationUtil validationUtil = new ValidationUtil();
        UserInfoViewModel userInfoViewModel = new UserInfoViewModel(userInfo,validationUtil);


        다음버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getCurrentFocus() != null) {
                    getCurrentFocus().clearFocus();
                }
                // 서버에 연락처 정보 변경 요청
//                findAccountIdViewModel.아이디찾기요청();


                Log.i("test",Integer.toString(authType));
                Log.i("test",getText(인증데이터입력창));
                Log.i("test",getText(인증번호입력창));

                userInfoViewModel.연락처정보변경요청(authType,getText(인증데이터입력창),getText(인증번호입력창));

//                $auth_type = $data['auth_type'];
//                $auth_data = $data['auth_data'];
//                $auth_code = $data['auth_code'];

//                finish();
            }
        });

        userInfoViewModel.get연락처Result().observe(this, result -> {

            if(result.isSuccess()){
                EditProfileActivity.연락처정보 = result.결과메세지();
//                인증창.setVisibility(View.GONE);
//                아이디보여주기창.setVisibility(View.VISIBLE);
//                아이디보여주는뷰.setText(result.결과메세지());
                finish();
            }
            else{
                // 에러메세지
                handleValidationResult(인증데이터입력창,인증상태메세지, result);
            }

        });
//        get연락처Result

        툴바뒤로가기설정(툴바);

    }


    public void getSMS() {
//        SMS 검색 작업은 앱을 식별하는 고유한 문자열이 포함된 SMS 메시지를 최대 5분 동안 리슨합니다.
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
// SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);

// Starts SmsRetriever, which waits for ONE matching SMS message until timeout
// (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
// action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();

// Listen for success/failure of the start Task. If in a background thread, this
// can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
                AppSignature appSignature = new AppSignature();
                List<String> code = appSignature.getAppSignatures(getApplicationContext());
                Log.i("", code.get(0).toString());
                findAccountIdViewModel.인증번호발송요청(UserRepository.중복인지,authType,getText(인증데이터입력창),code.get(0).toString(),null);
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
            }
        });
    }

    private String getText(EditText editText) {
        return editText.getText().toString();
    }

    // ValidationResult 타입의 결과를 처리하는 메서드
    private void handleValidationResult(TextView messageView, Result result) {
        messageView.setText(result.결과메세지());
        messageView.setTextColor(result.메세지색상());
        messageView.setVisibility(result.메세지색상() == Color.TRANSPARENT ? View.GONE : View.VISIBLE);

    }

    private void handleValidationResult(EditText editText, TextView messageView, Result result) {

        handleValidationResult(messageView, result);
        editText.setBackground(회색테두리배경색);
        editText.setTextColor(Color.BLACK);

        if (result.isSuccess()) {
            editText.setTag(통과);
        } else {
//            editText.setBackground(빨간색테두리배경색);
//            if (result.메세지색상() != Color.TRANSPARENT)
//                editText.setTextColor(result.메세지색상());
            editText.setTag(미통과);
        }
        messageView.setText(result.결과메세지());
        messageView.setTextColor(result.메세지색상());
        messageView.setVisibility(result.메세지색상() == Color.TRANSPARENT ? View.GONE : View.VISIBLE);

    }


    class FocusChangeListener implements View.OnFocusChangeListener {

        int type;

        FocusChangeListener(int type) {
            this.type = type;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (type) {
                    case 인증데이터:
                        findAccountIdViewModel.인증데이터유효성검사(authType, getText(인증데이터입력창));
                        break;
                    case 인증코드:
                        findAccountIdViewModel.인증번호형식검사(getText(인증번호입력창));
                        break;
                }
            } else {
                switch (type) {
                    case 인증데이터:
                        if (통과.equals(((String) 인증데이터입력창.getTag())) || 미검사.equals(((String) 인증데이터입력창.getTag()))) {
                            인증데이터입력창.setBackground(초록색테두리배경색);
                        }
                        break;
                    case 인증코드:
                        if (통과.equals(((String) 인증번호입력창.getTag())) || 미검사.equals(((String) 인증번호입력창.getTag()))) {
                            인증번호입력창.setBackground(초록색테두리배경색);
                        }
                        break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 액티비티가 파괴될 때 리시버 등록 해제
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

}