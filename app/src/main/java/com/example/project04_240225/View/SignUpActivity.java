package com.example.project04_240225.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project04_240225.AppSignature;
import com.example.project04_240225.R;
import com.example.project04_240225.Result;
import com.example.project04_240225.Model.UserRepository;
import com.example.project04_240225.ViewModel.SignUpViewModel;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

// 데이터를 표시하는 역할 (View) (MVVM 패턴 / Model , View, ViewModel )
public class SignUpActivity extends AppCompatActivity {


    public static final int EMAIL = 100, PHONE = 101;
    final int 아이디 = 0,비밀번호 = 1,닉네임 = 2,인증데이터 = 3,인증코드 = 4;
    final int 비밀번호보이게타입 = InputType.TYPE_CLASS_TEXT, 비밀번호안보이게타입 = (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    final int 비밀번호보이게이미지ID = R.drawable.baseline_visibility_24, 비밀번호안보이게이미지ID = R.drawable.baseline_visibility_off_24;

    final String 미검사 = "미검사", 미통과 = "미통과", 통과 ="통과";
    Drawable 빨간색테두리배경색,회색테두리배경색,초록색테두리배경색;

    int authType;


    private BroadcastReceiver mReceiver;

    TextView 아이디메세지창, 비밀번호메세지창, 닉네임메세지창, 인증데이터메세지창, 인증메세지창, 약관설명메세지창;

    ImageButton 비밀번호상태변경버튼;
    Button 인증종류변경버튼, 인증번호재요청버튼, 인증요청_회원가입버튼;
    EditText 비밀번호입력창,인증종류인증데이터입력창, 아이디입력창, 닉네임입력창, 인증번호입력창;
    SignUpViewModel signUpViewModel;
    UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authType = EMAIL;

        아이디입력창 = findViewById(R.id.editTextText2);
        비밀번호입력창 = findViewById(R.id.editTextTextPassword2);
        닉네임입력창 = findViewById(R.id.editTextText3);


        아이디메세지창 = findViewById(R.id.textView5);
        비밀번호메세지창 = findViewById(R.id.textView8);
        닉네임메세지창 = findViewById(R.id.textView9);
        인증데이터메세지창 = findViewById(R.id.textView12);
        인증메세지창 = findViewById(R.id.textView13);

        약관설명메세지창 = findViewById(R.id.textView3);

        인증종류인증데이터입력창 = findViewById(R.id.editTextText4);
        인증번호입력창 = findViewById(R.id.editTextText5);

        비밀번호상태변경버튼 = findViewById(R.id.imageButton4);
        인증종류변경버튼 = findViewById(R.id.button8);
        인증번호재요청버튼 = findViewById(R.id.button7);
        인증요청_회원가입버튼 = findViewById(R.id.button5);




        userRepository = new UserRepository(getApplicationContext());
        signUpViewModel = new SignUpViewModel(userRepository);

        아이디입력창.setTag(미검사);
        비밀번호입력창.setTag(미검사);
        닉네임입력창.setTag(미검사);
        인증종류인증데이터입력창.setTag(미검사);
        인증번호입력창.setTag(미검사);


        빨간색테두리배경색 = getDrawable(R.drawable.edit_text_border_red);
        회색테두리배경색= getDrawable(R.drawable.edit_text_border_gray);
        초록색테두리배경색= getDrawable(R.drawable.edit_text_border_green);



        signUpViewModel.getUserIdValidationResult().observe(this, result ->
                handleValidationResult(아이디입력창,아이디메세지창, result));

        signUpViewModel.getPasswordValidationResult().observe(this, result ->
                handleValidationResult(비밀번호입력창,비밀번호메세지창, result));

        signUpViewModel.getNicknameValidationResult().observe(this, result ->
                handleValidationResult(닉네임입력창,닉네임메세지창, result));


        signUpViewModel.getVerificationCodeValidationResult().observe(this, result ->{

            if(result.isSuccess()){
                인증메세지창.setVisibility(View.GONE);
                인증번호입력창.setText("");
                인증번호입력창.setVisibility(View.GONE);
                인증번호재요청버튼.setVisibility(View.GONE);
                인증요청_회원가입버튼.setText("인증요청");
                인증종류변경버튼.setVisibility(View.VISIBLE);
            }
            handleValidationResult(인증종류인증데이터입력창,인증데이터메세지창, result);
        });

        signUpViewModel.getVerificationCodeSendResult().observe(this, result ->{

            if(result.isSuccess()){
                인증번호입력창.setVisibility(View.VISIBLE);
                인증번호재요청버튼.setVisibility(View.VISIBLE);
                인증종류변경버튼.setVisibility(View.INVISIBLE);
                인증요청_회원가입버튼.setText("가입하기");
            }
            handleValidationResult(인증종류인증데이터입력창,인증메세지창, result);

        });

        signUpViewModel.getVerificationCodeCheckResult().observe(this, result -> {
            인증번호입력창.setBackground(회색테두리배경색);
            handleValidationResult(인증메세지창, result);
        });

        signUpViewModel.getSignUpResult().observe(this, result ->{
            if(result.isSuccess()){
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                finish();
            }
            handleValidationResult(인증메세지창, result);
        });
        // BroadcastReceiver 초기화 및 등록
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("CustomAction".equals(action)) {
                    String code = intent.getStringExtra("EXTRA_CODE");
                    Log.i("Received Code", code);
                    인증번호입력창.setText(code);
                    인증요청_회원가입버튼.performClick();
                    // 코드 사용...
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("CustomAction"));


        아이디입력창.setOnFocusChangeListener(new SignUpFocusChangeListener(아이디));
        비밀번호입력창.setOnFocusChangeListener(new SignUpFocusChangeListener(비밀번호));
        닉네임입력창.setOnFocusChangeListener(new SignUpFocusChangeListener(닉네임));
        인증종류인증데이터입력창.setOnFocusChangeListener(new SignUpFocusChangeListener(인증데이터));
        인증번호입력창.setOnFocusChangeListener(new SignUpFocusChangeListener(인증코드));

        비밀번호상태변경버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 포커스를 가지고 있는 뷰에서 포커스 해제
//                if(getCurrentFocus() != null) {
//                    getCurrentFocus().clearFocus();
//                }

                if(비밀번호입력창.getInputType() == 비밀번호보이게타입){
                    비밀번호입력창.setInputType(비밀번호안보이게타입);
                    비밀번호상태변경버튼.setImageDrawable(getDrawable(비밀번호안보이게이미지ID));
                }
                else if(비밀번호입력창.getInputType() == 비밀번호안보이게타입){
                    비밀번호입력창.setInputType(비밀번호보이게타입);
                    비밀번호상태변경버튼.setImageDrawable(getDrawable(비밀번호보이게이미지ID));
                }
            }
        });


        인증종류변경버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // EMAIL = 100, PHONE = 101;
                if(EMAIL == authType){
                    인증종류인증데이터입력창.setHint("휴대전화번호");
                    인증종류인증데이터입력창.setInputType(InputType.TYPE_CLASS_PHONE);

                    인증종류변경버튼.setText("이메일으로 인증");
                    authType = PHONE;
                }
                else if(PHONE == authType){
                    인증종류인증데이터입력창.setHint("이메일");
                    인증종류인증데이터입력창.setInputType(InputType.TYPE_CLASS_TEXT);

                    인증종류변경버튼.setText("휴대전화번호로 인증");
                    authType = EMAIL;
                }
                인증데이터메세지창.setVisibility(View.GONE);
                인증메세지창.setVisibility(View.GONE);
                인증종류인증데이터입력창.setTag(미검사);
                인증종류인증데이터입력창.setBackground(회색테두리배경색);
                인증종류인증데이터입력창.setTextColor(Color.BLACK);
                인증종류인증데이터입력창.setText("");
                인증번호입력창.setText("");
                인증번호입력창.setVisibility(View.GONE);
                인증번호재요청버튼.setVisibility(View.GONE);
            }
        });


        인증번호재요청버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(authType==EMAIL){
                    signUpViewModel.인증종류데이터인증코드발송요청(authType,getText(인증종류인증데이터입력창));
                }
                else if(authType == PHONE){
                    getSMS();
//                            AppSignature appSignature = new AppSignature();
//                            List<String> code = appSignature.getAppSignatures(getApplicationContext());
//                            Log.i("",code.get(0).toString());
//                            signUpViewModel.인증종류데이터인증코드발송요청(authType,getText(인증종류인증데이터입력창),code.get(0).toString());
                }


//                signUpViewModel.인증종류데이터인증코드발송요청(authType,getText(인증종류인증데이터입력창));
            }
        });

        인증요청_회원가입버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 입력값 형식이 맞는지 확인 다 하고
                // 입력값에 문제 없으면 인증번호 요청 진행 요청 진행

                // 현재 포커스를 가지고 있는 뷰에서 포커스 해제
                if(getCurrentFocus() != null) {
                    getCurrentFocus().clearFocus();
                }
                
                if("인증요청".equals(인증요청_회원가입버튼.getText().toString())){
                    boolean 데이터형식검사결과 = signUpViewModel.데이터형식검사(getText(아이디입력창),getText(비밀번호입력창),getText(닉네임입력창),authType,getText(인증종류인증데이터입력창));
                    if(데이터형식검사결과){

                        if(authType==EMAIL){
                            signUpViewModel.인증종류데이터인증코드발송요청(authType,getText(인증종류인증데이터입력창));
                        }
                        else if(authType == PHONE){
                            getSMS();
//                            AppSignature appSignature = new AppSignature();
//                            List<String> code = appSignature.getAppSignatures(getApplicationContext());
//                            Log.i("",code.get(0).toString());
//                            signUpViewModel.인증종류데이터인증코드발송요청(authType,getText(인증종류인증데이터입력창),code.get(0).toString());
                        }

                    }
                }
                else if("가입하기".equals(인증요청_회원가입버튼.getText().toString())){
//                    public void 회원가입요청(String user_id, String user_pw, String username, int auth_type, String auth_data, String code){

                    boolean 데이터형식검사결과 = signUpViewModel.데이터형식검사(getText(아이디입력창),getText(비밀번호입력창),getText(닉네임입력창),authType,getText(인증종류인증데이터입력창))
                            &&signUpViewModel.인증코드형식검사(getText(인증번호입력창));
                    if(데이터형식검사결과) {
                        signUpViewModel.회원가입요청(getText(아이디입력창),getText(비밀번호입력창),getText(닉네임입력창),authType,getText(인증종류인증데이터입력창),getText(인증번호입력창));
                    }
                }
                //                인증번호를 발송했습니다. 인증 문자가 오지 않으면 도움말을 확인해 보세요.
//                signUpViewModel.회원가입요청();



            }
        });



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

                        Intent intent = new Intent(SignUpActivity.this, TermActivity.class)
                                .putExtra("type", TermActivity.types[finalI]);
                        startActivity(intent);

                    }
                }, startIdx, endIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        약관설명메세지창.setText(spannableString);
        약관설명메세지창.setLinkTextColor(getColor(R.color.green));
        약관설명메세지창.setMovementMethod(LinkMovementMethod.getInstance()); // 클릭 이벤트 활성화

    }

    public void getSMS(){
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
                Log.i("",code.get(0).toString());
                signUpViewModel.인증종류데이터인증코드발송요청(authType,getText(인증종류인증데이터입력창),code.get(0).toString());
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

    private String getText(EditText editText){
        return editText.getText().toString();
    }
    private String getText(TextView textView){
        return textView.getText().toString();
    }
    // ValidationResult 타입의 결과를 처리하는 메서드
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
            editText.setTag(통과);
        } else {
            editText.setBackground(빨간색테두리배경색);
            if(result.메세지색상() != Color.TRANSPARENT)
                editText.setTextColor(result.메세지색상());
            editText.setTag(미통과);
        }
        messageView.setText(result.결과메세지());
        messageView.setTextColor(result.메세지색상());
        messageView.setVisibility(result.메세지색상() == Color.TRANSPARENT ? View.GONE : View.VISIBLE);

    }



    class SignUpFocusChangeListener implements View.OnFocusChangeListener{

        int type;
        String authData;
        SignUpFocusChangeListener(int type){
            this.type = type;
            authData = null;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                switch (type) {
                    case 아이디:
                        signUpViewModel.아이디유효성검사(getText(아이디입력창));
                        break;
                    case 비밀번호:
                        signUpViewModel.비밀번호유효성검사(getText(비밀번호입력창));
                        break;
                    case 닉네임:
                        signUpViewModel.닉네임유효성검사(getText(닉네임입력창));
                        break;
                    case 인증데이터:
                        if(!authData.equals(getText(인증종류인증데이터입력창))){
                            인증종류변경버튼.setVisibility(View.VISIBLE);
                            인증데이터메세지창.setVisibility(View.GONE);
                            인증메세지창.setVisibility(View.GONE);
                            인증번호입력창.setVisibility(View.GONE);
                            인증번호재요청버튼.setVisibility(View.GONE);
                            인증번호입력창.setText("");
                            인증요청_회원가입버튼.setText("인증요청");
                        }
                        signUpViewModel.인증데이터유효성검사(authType,getText(인증종류인증데이터입력창));
                        break;
                    case 인증코드:
                        signUpViewModel.인증코드유효성검사(authType,getText(인증종류인증데이터입력창),getText(인증번호입력창));
                        break;
                }
            }
            else{
                switch (type) {
                    case 아이디:
                        if(통과.equals(((String)아이디입력창.getTag()))||미검사.equals(((String)아이디입력창.getTag()))){
                            아이디입력창.setBackground(초록색테두리배경색);
                        }
                        break;
                    case 비밀번호:
                        if(통과.equals(((String)비밀번호입력창.getTag()))||미검사.equals(((String)비밀번호입력창.getTag()))){
                            비밀번호입력창.setBackground(초록색테두리배경색);
                        }
                        break;
                    case 닉네임:
                        if(통과.equals(((String)닉네임입력창.getTag()))||미검사.equals(((String)닉네임입력창.getTag()))){
                            닉네임입력창.setBackground(초록색테두리배경색);
                        }
                        break;
                    case 인증데이터:
                        authData = getText(인증종류인증데이터입력창);
                        if(통과.equals(((String)인증종류인증데이터입력창.getTag()))||미검사.equals(((String)인증종류인증데이터입력창.getTag()))){
                            인증종류인증데이터입력창.setBackground(초록색테두리배경색);
                        }
                        break;
                    case 인증코드:
                        if(통과.equals(((String)인증번호입력창.getTag()))||미검사.equals(((String)인증번호입력창.getTag()))){
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