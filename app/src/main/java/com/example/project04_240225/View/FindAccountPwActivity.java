package com.example.project04_240225.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.project04_240225.AppSignature;
import com.example.project04_240225.Model.UserRepository;
import com.example.project04_240225.R;
import com.example.project04_240225.Result;
import com.example.project04_240225.Utility.ValidationUtil;
import com.example.project04_240225.ViewModel.FindAccountViewModel;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.recaptcha.Recaptcha;
import com.google.android.recaptcha.RecaptchaAction;
import com.google.android.recaptcha.RecaptchaTasksClient;

import java.util.List;

public class FindAccountPwActivity extends AppCompatActivity {

    ConstraintLayout 인증창,비밀번호재설정창;
    LinearLayout 아이디입력레이아웃;
    Button 인증화면으로이동버튼, 아이디찾기로이동버튼;
    EditText 아이디입력창;

    TextView 아이디입력상태메세지;


    FindAccountViewModel findAccountViewModel;
    UserRepository userRepository;


    private BroadcastReceiver mReceiver;
    Drawable 빨간색테두리배경색, 회색테두리배경색, 초록색테두리배경색;
    final String 미검사 = "미검사", 미통과 = "미통과", 통과 = "통과";
    int authType;
    final int 인증데이터 = 3, 인증코드 = 4;


    static final int EMAIL = 100, PHONE = 101;

    RadioGroup 인증선택그룹;
    TextView 휴대전화번호인증설명메세지, 이메일인증설명메세지;

    EditText 인증데이터입력창, 인증번호입력창;

    Button 인증번호받기버튼, 다음버튼;

    TextView 인증데이터상태메세지, 인증상태메세지;


    TextView 비밀번호상태메세지, 비밀번호확인상태메세지, 자동입력방지문자상태메세지;
    Button 비밀번호변경확인;
    EditText 새비밀번호입력창, 새비밀번호확인입력창, 자동입력방지문자입력창;
    TextView 아이디메세지;


    String 아이디;



//6LfFXYspAAAAAIToTIepCN45fma-v7DSzAZMrEy-
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account_pw);

        아이디입력레이아웃 = findViewById(R.id.linearLayout1);
        아이디입력창 = findViewById(R.id.editTextText6);

        인증화면으로이동버튼 = findViewById(R.id.button6);
        아이디찾기로이동버튼 = findViewById(R.id.button11);
        아이디입력상태메세지 = findViewById(R.id.textView24);
        인증창 = findViewById(R.id.layout2);
        비밀번호재설정창 = findViewById(R.id.layout3);


        authType = EMAIL;

        인증선택그룹 = findViewById(R.id.radioGroup3);
        휴대전화번호인증설명메세지 = findViewById(R.id.textView26);
        이메일인증설명메세지 = findViewById(R.id.textView25);

        인증데이터입력창 = findViewById(R.id.editTextText7);
        인증번호입력창 = findViewById(R.id.editTextNumber2);
        인증번호받기버튼 = findViewById(R.id.button17);
        다음버튼 = findViewById(R.id.button16);

        인증데이터상태메세지 = findViewById(R.id.textView27);
        인증상태메세지 = findViewById(R.id.textView28);


        비밀번호상태메세지 = findViewById(R.id.textView32);
        비밀번호확인상태메세지 = findViewById(R.id.textView31);
        자동입력방지문자상태메세지 = findViewById(R.id.textView34);

        비밀번호변경확인 = findViewById(R.id.button18);
        새비밀번호입력창 = findViewById(R.id.editTextTextPassword3);
        새비밀번호확인입력창 = findViewById(R.id.editTextTextPassword4);
        자동입력방지문자입력창 = findViewById(R.id.editTextText8);
        아이디메세지 = findViewById(R.id.textView30);


        인증데이터입력창.setTag(미검사);
        인증번호입력창.setTag(미검사);


        userRepository = new UserRepository(getApplicationContext());
        findAccountViewModel = new FindAccountViewModel(userRepository);

        빨간색테두리배경색 = getDrawable(R.drawable.edit_text_border_red);
        회색테두리배경색 = getDrawable(R.drawable.edit_text_border_gray);
        초록색테두리배경색 = getDrawable(R.drawable.edit_text_border_green);

        인증화면으로이동버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAccountViewModel.아이디유효성검사(아이디입력창.getText().toString());
            }
        });

        아이디찾기로이동버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FindAccountPwActivity.this, FindAccountIdActivity.class);
                startActivity(intent);
//                finish();
            }
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

                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("CustomAction"));


        findAccountViewModel.getVerificationCodeValidationResult().observe(this, result ->{

            handleValidationResult(인증데이터입력창,인증데이터상태메세지, result);
        });

        findAccountViewModel.getResetAccountPwResultResult().observe(this, result ->{

            if(result.isSuccess()){
                // 성공
                Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                finish();

            }
            else{
                // 실패
//                handleValidationResult(인증데이터입력창,인증데이터상태메세지, result);
            }
            
        });


        findAccountViewModel.getVerificationCodeSendResult().observe(this, result ->{

            handleValidationResult(인증데이터입력창,인증상태메세지, result);

        });

        findAccountViewModel.getVerificationCodeCheckResult().observe(this, result -> {
            인증번호입력창.setBackground(회색테두리배경색);
            handleValidationResult(인증상태메세지, result);
        });
        findAccountViewModel.getFindAccountIdResultResult().observe(this, result -> {

            if(result.isSuccess()){
                인증창.setVisibility(View.GONE);
                비밀번호재설정창.setVisibility(View.VISIBLE);
                아이디메세지.setText("아이디 : "+getText(아이디입력창));
                아이디 = getText(아이디입력창);
//                아이디보여주기창.setVisibility(View.VISIBLE);
//                아이디보여주는뷰.setText(result.결과메세지());
            }
            else{
                // 에러메세지
                handleValidationResult(인증데이터입력창,인증상태메세지, result);
            }

        });


        findAccountViewModel.getIdCheckResult().observe(this, result ->{
            if(result.isSuccess()){
                아이디입력레이아웃.setVisibility(View.GONE);
                인증창.setVisibility(View.VISIBLE);
                아이디메세지.setText("아이디 : "+result.결과메세지());
                아이디 = result.결과메세지();

//                ImageView 자동입력방지이미지;
//                자동입력방지이미지 = findViewById(R.id.imageView);
//                String imageUrl = "http://49.247.30.164/project04/src/Controller/getKey.php"; // 로드할 이미지의 URL
//
//                Glide.with(this)
//                        .load(imageUrl)
//                        .into(자동입력방지이미지); // 이미지 로드 및 ImageView에 표시
//                // 자동입력방지 캡챠 이미지요청

            }
            else{
                handleValidationResult(아이디입력상태메세지,result);
            }
        });

        인증데이터입력창.setOnFocusChangeListener(new FindAccountPwActivity.SignUpFocusChangeListener(인증데이터));
        인증번호입력창.setOnFocusChangeListener(new FindAccountPwActivity.SignUpFocusChangeListener(인증코드));

        인증선택그룹.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioButton4) {
                    휴대전화번호인증설명메세지.setVisibility(View.VISIBLE);
                    이메일인증설명메세지.setVisibility(View.GONE);
                    인증데이터입력창.setInputType(InputType.TYPE_CLASS_PHONE);

                    인증데이터입력창.setHint("휴대전화번호");
                    authType = PHONE;
                } else if (checkedId == R.id.radioButton3) {
                    휴대전화번호인증설명메세지.setVisibility(View.GONE);
                    이메일인증설명메세지.setVisibility(View.VISIBLE);
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

//                다음버튼.setEnabled(false);

            }
        });

        인증번호받기버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getCurrentFocus() != null) {
                    getCurrentFocus().clearFocus();
                }

                if(findAccountViewModel.인증데이터형식검사(authType,getText(인증데이터입력창))){

                    if(authType==EMAIL){
                        findAccountViewModel.인증번호발송요청(authType,getText(인증데이터입력창),getText(아이디입력창));
                    }
                    else if(authType == PHONE){
                        getSMS();
                    }

                }

            }
        });


        다음버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getCurrentFocus() != null) {
                    getCurrentFocus().clearFocus();
                }

                findAccountViewModel.아이디찾기요청(authType, getText(인증데이터입력창), getText(인증번호입력창));

//                if(findAccountIdViewModel.인증데이터형식검사(authType,getText(인증데이터입력창)) && findAccountIdViewModel.인증번호형식검사(getText(인증번호입력창))){
                // 서버에 요청
                // 성공시 화면 변경.
//                }

            }
        });

        비밀번호변경확인.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String 새비밀번호 = 새비밀번호입력창.getText().toString();
                String 새비밀번호확인 = 새비밀번호확인입력창.getText().toString();
                String 자동입력방지문자 = 자동입력방지문자입력창.getText().toString();
                ValidationUtil validationUtil = new ValidationUtil();

                비밀번호상태메세지.setVisibility(View.GONE);
                비밀번호확인상태메세지.setVisibility(View.GONE);
                자동입력방지문자상태메세지.setVisibility(View.GONE);

                boolean result = true;
                if(새비밀번호.equals("")){
                    비밀번호상태메세지.setText("새 비밀번호를 입력해주세요.");
                    비밀번호상태메세지.setVisibility(View.VISIBLE);
                    result = false;
                }
                else if(!validationUtil.pw_validate(새비밀번호)){
                    // 형식검사
//                    비밀번호는 8~16자의 영문 대소문자와 숫자, 특수문자를 사용할 수 있습니다.
//                    passwordValidationResult.setValue(new Result(false,"비밀번호: 8~16자의 영문 대/소문자, 숫자, 특수문자만 사용 가능합니다.", Color.RED));
                    비밀번호상태메세지.setText(" 8~16자의 영문 대/소문자, 숫자, 특수문자만 사용 가능합니다.");
                    비밀번호상태메세지.setVisibility(View.VISIBLE);
                    result = false;
                }


                if(새비밀번호확인.equals("")){
                    비밀번호확인상태메세지.setText("새 비밀번호 확인을 입력해주세요.");
                    비밀번호확인상태메세지.setVisibility(View.VISIBLE);
                    result = false;
                }
                else if(!새비밀번호.equals(새비밀번호확인)){
                    비밀번호확인상태메세지.setText("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                    비밀번호확인상태메세지.setVisibility(View.VISIBLE);
                    result = false;
                }


                if(자동입력방지문자.equals("")){
                    자동입력방지문자상태메세지.setText("자동입력방지문자를 입력해주세요.");
                    자동입력방지문자상태메세지.setVisibility(View.VISIBLE);
                    result = false;
                }


                if(result){
                    // 비밀번호 변경 요청
                    // 아이디와 새로운 비밀번호 보냄.
//                    Toast.makeText(FindAccountPwActivity.this, "비밀번호 변경 요청", Toast.LENGTH_SHORT).show();
//                    ResetPasswordController
                    // 아이디, 비밀번호, 인증종류, 인증데이터, 인증번호, 자동입력인증문자 데이터 서버에 전송하면서
                    // 비밀번호 재설정 요청.
                    findAccountViewModel.비밀번호재설정(아이디,새비밀번호, authType, getText(인증데이터입력창),getText(인증번호입력창),자동입력방지문자);

                }


            }
        });
        initializeRecaptchaClient();
        ImageView 자동입력방지이미지;
        자동입력방지이미지 = findViewById(R.id.imageView);
        findAccountViewModel.이미지가져오기();
//        Request();
        findAccountViewModel.getImageResult().observe(this, result -> {
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
//        String imageUrl = "http://49.247.30.164/project04/src/Controller/getKey.php"; // 로드할 이미지의 URL
//
//        Glide.with(this)
//                .load(imageUrl)
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
////                        Log.e("GlideError", "Image load failed", e);
//                        Log.e("GlideError",  e.getMessage());
////                        Log.e("GlideError",  );
//                        e.logRootCauses("")
//                        Toast.makeText(FindAccountPwActivity.this, "오류", Toast.LENGTH_SHORT).show();
//                        return false; // false를 반환하여 Glide가 오류 placeholder를 설정하도록 함
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        return false;
//                    }
//                })
//                .into(자동입력방지이미지); // 이미지 로드 및 ImageView에 표시


    }


    @Nullable
    private RecaptchaTasksClient recaptchaTasksClient = null;

    private void initializeRecaptchaClient() {
        Recaptcha
                .getTasksClient(getApplication(), "6LfFXYspAAAAAIToTIepCN45fma-v7DSzAZMrEy-")
                .addOnSuccessListener(
                        this,
                        new OnSuccessListener<RecaptchaTasksClient>() {
                            @Override
                            public void onSuccess(RecaptchaTasksClient client) {
                                FindAccountPwActivity.this.recaptchaTasksClient = client;
                            }
                        })
                .addOnFailureListener(
                        this,
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle communication errors ...
                                // See "Handle communication errors" section
                            }
                        });
    }

    private void executeLoginAction(View v) {
        assert recaptchaTasksClient != null;
        recaptchaTasksClient
                .executeTask(RecaptchaAction.LOGIN)
                .addOnSuccessListener(
                        this,
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String token) {
                                // Handle success ...
                                // See "What's next" section for instructions
                                // about handling tokens.


                            }
                        })
                .addOnFailureListener(
                        this,
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle communication errors ...
                                // See "Handle communication errors" section
                            }
                        });
    }
    private void executeRedeemAction(View v) {
        assert recaptchaTasksClient != null;
        recaptchaTasksClient
                .executeTask(RecaptchaAction.custom("redeem"))
                .addOnSuccessListener(
                        this,
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String token) {
                                // Handle success ...
                                // See "What's next" section for instructions
                                // about handling tokens.
                            }
                        })
                .addOnFailureListener(
                        this,
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle communication errors ...
                                // See "Handle communication errors" section
                            }
                        });
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
                findAccountViewModel.인증번호발송요청(authType,getText(인증데이터입력창),code.get(0).toString(),getText(아이디입력창));
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


//    final String 미검사 = "미검사", 미통과 = "미통과", 통과 = "통과";
//    Drawable 빨간색테두리배경색, 회색테두리배경색, 초록색테두리배경색;

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



    class SignUpFocusChangeListener implements View.OnFocusChangeListener {

        int type;

        SignUpFocusChangeListener(int type) {
            this.type = type;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (type) {
                    case 인증데이터:
                        findAccountViewModel.인증데이터유효성검사(authType, getText(인증데이터입력창));
                        break;
                    case 인증코드:
                        findAccountViewModel.인증번호형식검사(getText(인증번호입력창));
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