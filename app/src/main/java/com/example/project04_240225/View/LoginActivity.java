package com.example.project04_240225.View;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project04_240225.Network.SharedPreferencesManager;
import com.example.project04_240225.R;
import com.example.project04_240225.Model.UserRepository;
import com.example.project04_240225.ViewModel.LoginViewModel;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.auth.model.Prompt;
import com.kakao.sdk.common.model.ClientError;
import com.kakao.sdk.common.model.ClientErrorCause;
import com.kakao.sdk.user.UserApiClient;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.NidOAuthLogin;
import com.navercorp.nid.oauth.NidOAuthLoginState;
import com.navercorp.nid.oauth.OAuthLoginCallback;
import com.navercorp.nid.profile.NidProfileCallback;
import com.navercorp.nid.profile.data.NidProfileMap;

import java.util.Arrays;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    Button 회원가입화면으로이동버튼;

    UserRepository userRepository;

    LoginViewModel loginViewModel;
    SharedPreferencesManager sharedPreferencesManager;
    TextView 로그인상태메세지창;
    EditText 아이디입력창, 비밀번호입력창;
    Button 로그인버튼,아이디찾기버튼, 비밀번호찾기버튼;

    Button 네이버로시작하기버튼;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


//        if(NaverIdLoginSDK.INSTANCE.getRefreshToken() == null){
//            Log.i("NaverIdLoginSDK.INSTANCE.getRefreshToken()","null");
//
//        }
//        else{
//            Log.i("NaverIdLoginSDK.INSTANCE.getRefreshToken()",NaverIdLoginSDK.INSTANCE.getRefreshToken());
//
//        }

        로그인버튼 = findViewById(R.id.button4);
        아이디입력창 = findViewById(R.id.editTextText);
        비밀번호입력창 = findViewById(R.id.editTextTextPassword);
        로그인상태메세지창 = findViewById(R.id.textView4);
        회원가입화면으로이동버튼 = findViewById(R.id.button);
        아이디찾기버튼 = findViewById(R.id.button3);
        비밀번호찾기버튼 = findViewById(R.id.button2);


        userRepository = new UserRepository(getApplicationContext());
        loginViewModel = new LoginViewModel(userRepository);
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());


        회원가입화면으로이동버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        loginViewModel.getLoginResult().observe(LoginActivity.this, result -> {
            if(result.isSuccess()){

                sharedPreferencesManager.saveLoginType("default");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
            else{
                로그인상태메세지창.setVisibility(View.VISIBLE);
                로그인상태메세지창.setText(result.결과메세지());
                로그인상태메세지창.setTextColor(result.메세지색상());
            }
        });

        loginViewModel.getCheckSNSLoginResult().observe(LoginActivity.this, result -> {
            if(result.isSuccess()){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Log.i("LoginActivity!","");


                if(result.결과메세지().equals("KAKAO")){
                    sharedPreferencesManager.removeLoginType2();
                }


                if("카카오톡계정".equals(카카오톡로그인타입)){
                    sharedPreferencesManager.saveLoginType("KAKAOACCOUNT");
                }else{
                    sharedPreferencesManager.saveLoginType(result.결과메세지());
                }


                Log.i("sharedPreferencesManager",result.결과메세지());
                Log.i("sharedPreferencesManager",sharedPreferencesManager.getLoginType());
                String type = sharedPreferencesManager.getLoginType();
                if(type == null){
                    Toast.makeText(this, "type null", Toast.LENGTH_SHORT).show();
                }
                else if(!sharedPreferencesManager.getLoginType().equals("KAKAO")){
//                    Toast.makeText(this, "type value wrong", Toast.LENGTH_SHORT).show();
                    Log.i("type value",type);
                }
                startActivity(intent);

                finish();
            }
            else{
                Intent intent = new Intent(LoginActivity.this, SNSSignUpActivity.class);
                intent.putExtra("회원번호",회원번호);
                intent.putExtra("닉네임",닉네임);
                intent.putExtra("가입종류",가입종류);
//                회원번호, 닉네임,가입종류
                startActivity(intent);
//                로그인상태메세지창.setVisibility(View.VISIBLE);
//                로그인상태메세지창.setText(result.결과메세지());
//                로그인상태메세지창.setTextColor(result.메세지색상());
            }

        });


        로그인버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.로그인(아이디입력창.getText().toString(), 비밀번호입력창.getText().toString());
            }
        });


        아이디찾기버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindAccountIdActivity.class);
                startActivity(intent);
            }
        });

        비밀번호찾기버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindAccountPwActivity.class);
                startActivity(intent);
            }
        });


        ImageButton 카카오톡로그인버튼;
        카카오톡로그인버튼 = findViewById(R.id.imageButton);
        카카오톡로그인버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginExample(LoginActivity.this);

            }
        });

        네이버로시작하기버튼 = findViewById(R.id.button29);
        네이버로시작하기버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                NaverIdLoginSDK.INSTANCE.

//                NidOAuthLoginState.NEED_REFRESH_TOKEN
//                NidOAuthLoginState.NEED_INIT
//                NidOAuthLoginState.OK
//                NidOAuthLoginState.NEED_LOGIN

                if(NidOAuthLoginState.NEED_LOGIN.equals(NaverIdLoginSDK.INSTANCE.getState())){

                }
//                if(NaverIdLoginSDK.INSTANCE.getState())
                Log.i("NaverIdLoginSDK.INSTANCE.getState()",NaverIdLoginSDK.INSTANCE.getState().name());

                OAuthLoginCallback oauthLoginCallback = new OAuthLoginCallback() {
                    @Override
                    public void onSuccess() {

                        // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                        // 회원가입 된 유저인지 확인
//                        Toast.makeText(LoginActivity.this, "네이버 로그인", Toast.LENGTH_SHORT).show();
                        Log.i("네이버 로그인","onSuccess");

//                        NaverIdLoginSDK.INSTANCE.getAccessToken().
//                        NidOAuthLogin.Companion.
//                        Log.i("네이버 로그인", NaverIdLoginSDK.INSTANCE.)

//                        new NidOAuthLogin().callRefreshAccessTokenApi(new OAuthLoginCallback() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onFailure(int i, @NonNull String s) {
//
//                            }
//
//                            @Override
//                            public void onError(int i, @NonNull String s) {
//
//                            }
//                        });

                        new NidOAuthLogin().getProfileMap(new NidProfileCallback<NidProfileMap>() {
                            @Override
                            public void onSuccess(NidProfileMap nidProfileMap) {

                                String id = nidProfileMap.getProfile().get("id").toString();
                                Log.i("네이버 로그인 id",id);

                                String name = nidProfileMap.getProfile().get("name").toString();
                                Log.i("네이버 로그인 name",name);
                                회원번호 = id;
                                닉네임 = name;
                                가입종류 ="NAVER";
                                loginViewModel.CheckSNSLogin(가입종류,회원번호);
//                                LkszZ1kp39WdZ4q-rXGk2obXdahfovr1XUJR1o3z2Fk

                            }

                            @Override
                            public void onFailure(int i, @NonNull String s) {


//                                new NidOAuthLogin().
//                                String errorCode = NaverIdLoginSDK.INSTANCE.getLastErrorCode().getCode();
//                                String errorDescription = NaverIdLoginSDK.INSTANCE.getLastErrorDescription();
//                                Toast.makeText(getBaseContext(), "errorCode:" + errorCode + ", errorDesc:" + errorDescription, Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(int i, @NonNull String s) {

                            }
                        });


//                        binding.tvAccessToken.setText(NaverIdLoginSDK.INSTANCE.getAccessToken());
//                        binding.tvRefreshToken.setText(NaverIdLoginSDK.INSTANCE.getRefreshToken());
//                        binding.tvExpires.setText(NaverIdLoginSDK.INSTANCE.getExpiresAt());
//                        binding.tvType.setText(NaverIdLoginSDK.INSTANCE.getTokenType());
//                        binding.tvState.setText(NaverIdLoginSDK.INSTANCE.getState().toString());
                    }

                    @Override
                    public void onFailure(int httpStatus, String message) {
                        String errorCode = NaverIdLoginSDK.INSTANCE.getLastErrorCode().getCode();
                        String errorDescription = NaverIdLoginSDK.INSTANCE.getLastErrorDescription();
                        Toast.makeText(getBaseContext(), "errorCode:" + errorCode + ", errorDesc:" + errorDescription, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int errorCode, String message) {
                        onFailure(errorCode, message);
                    }
                };


                NaverIdLoginSDK.INSTANCE.authenticate(LoginActivity.this,oauthLoginCallback);
//                NaverIdLoginSDK.authenticate(context, oauthLoginCallback)

            }
        });


        Button 애플로시작하기버튼 = findViewById(R.id.button30);
        애플로시작하기버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                new AppleSignInDialog(getBaseContext(), this).show();
//                if (p0 != null) {
//                    switch (p0.getId()) {
//                        case R.id.iv_apple_login:
//                            Context context = getContext();
//                            if (context != null) {
//                              new AppleSignInDialog(getBaseContext(), this).show();
//                            }
//                            break;
//                    }
//                }

            }
        });
//        Utility.INSTANCE.getKeyHash(this);
//        String keyHash = Utility.INSTANCE.getKeyHash(LoginActivity.this);
//        Log.i("keyHashkeyHashkeyHashkeyHash", keyHash);
//        Toast.makeText(this, "Hash"+keyHash, Toast.LENGTH_SHORT).show();

    }

    // 카카오계정으로 로그인 공통 callback 구성
    Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
        @Override
        public Unit invoke(OAuthToken token, Throwable error) {

            Log.i(TAG,"Function2<OAuthToken, Throwable, Unit> callback 실행.");

            if (error != null) {
//                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "카카오계정으로 로그인 실패", error);
//                String keyHash = Utility.INSTANCE.getKeyHash(LoginActivity.this);
//                Log.i("keyHashkeyHashkeyHashkeyHash", keyHash);
//                String keyHash = Utility.getKeyHash(this);
//                Log.e(TAG, "해시 키 값 : ${keyHash}");
            } else if (token != null) {
//                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "카카오계정으로 로그인 성공 " + token.getAccessToken());
                카카오톡로그인타입 = "카카오톡계정";
                사용자정보가져오기();
                // 여기서 발급받은 토큰으로 사용자 정보 조회
                // 서비스 회원 정보 확인 또는 가입 처리

                // 가입된 계정이 아니라면, 회원가입 화면으로 이동
                // 중복된 계정이라면 알려주기

            }else if(token == null){
                Log.i(TAG,"token null");
            }
            return null;
        }
    };

//    회원번호: 3387804771
//    닉네임: 배희진
//    프로필사진: https://k.kakaocdn.net/dn/clULvd/btsFsqjTeU5/tFJXUJduApABGEBb9Dj7ek/img_110x110.jpg

    public  void 사용자정보가져오기(){
        UserApiClient.getInstance().me((user, error) -> {

            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error);
            } else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공" +
                        "\n회원번호: " + user.getId() +
//                        "\n이메일: " + (user.getKakaoAccount() != null ? user.getKakaoAccount().getEmail() : null) +
                        "\n닉네임: " + (user.getKakaoAccount() != null && user.getKakaoAccount().getProfile() != null ? user.getKakaoAccount().getProfile().getNickname() : null) +
                        "\n프로필사진: " + (user.getKakaoAccount() != null && user.getKakaoAccount().getProfile() != null ? user.getKakaoAccount().getProfile().getThumbnailImageUrl() : null));
                회원번호 = Long.toString(user.getId());
                닉네임 = user.getKakaoAccount().getProfile().getNickname();
                가입종류 = "KAKAO";

                // 사용자 계정 물어보기.
                // 서버에 회원정보 / SNS 종류:카카오 전송 , 회원가입 유무 확인 요청
                // 회원가입 되어있으면 로그인 처리
                // 미회원가입시, 약관 동의 창으로 이동,
                // 동의하면 회원가입 진행.
                //
                loginViewModel.CheckSNSLogin(가입종류,Long.toString(user.getId()));


            }

            return null;
        });
    }

    String 회원번호, 닉네임,가입종류;
    String 카카오톡로그인타입;
    public void loginExample(Context context) {


//        카카오계정으로로그인(context);
//        return;
//
        // 인증 요청 : 로그인 (아이디/비밀번호 입력) , 동의 요청 : 동의화면의 동의
        // isKakaoTalkLoginAvailable() : 카카오톡으로 로그인 가능(설치) 여부 검사.
        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(context)) {

            // 로그인 상태인 경우 (. 카카오톡에 연결된 카카오계정이 있는 경우)
            // 비로그인 상태인 경우 (. 카카오톡에 연결된 카카오계정이 없는 경우)

            // 카카오톡으로 로그인 (앱으로)
            // 중요한 것 :  결과에 따라 필요한 동작과 예외 처리를 정의하는 것.
            UserApiClient.getInstance().loginWithKakaoTalk(context, new Function2<OAuthToken, Throwable, Unit>() {
                @Override
                public Unit invoke(OAuthToken token, Throwable error) {

                    // 예제 그대로!
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error);

                        // *******************
                        // 동의 화면에서 뒤로가면?

//                        AuthError(statusCode=401, reason=Misconfigured, response=AuthErrorResponse(error=misconfigured, errorDescription=invalid android_key_hash or ios_bundle_id or web_site_url))
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리
                        if (error instanceof ClientError && ((ClientError) error).getReason() == ClientErrorCause.Cancelled) {
                            return null;
                        }
                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        카카오계정으로로그인(context);

                    } else if (token != null) {
                        Log.i(TAG, "카카오톡으로 로그인 성공 " + token.getAccessToken());
                        카카오톡로그인타입 = "카카오톡";
                        사용자정보가져오기();
                    }
                    return null;

                }
            });
        }
        // 카카오 계정으로 로그인 시작
        else {
            카카오계정으로로그인(context);
        }
    }

    private void 카카오계정으로로그인(Context context){
        
        UserApiClient.getInstance().loginWithKakaoAccount(context,  callback);

//        // 카카오 계정으로 로그인
//        String loginTyep = sharedPreferencesManager.getLoginType2();
//        if(loginTyep == null){
//            UserApiClient.getInstance().loginWithKakaoAccount(context,  callback);
//        }
//        else{
//            Toast.makeText(context, "탈퇴해서...", Toast.LENGTH_SHORT).show();
//            // 무조건 로그인 화면으로 이동.
//            UserApiClient.getInstance().loginWithKakaoAccount(context, Arrays.asList(Prompt.LOGIN), callback);
//        }

//            UserApiClient.getInstance().loginWithKakaoAccount(context, Arrays.asList(Prompt.SELECT_ACCOUNT), callback);
//            UserApiClient.getInstance().loginWithKakaoAccount(context,  Arrays.asList(Prompt.SELECT_ACCOUNT), callback);
//            UserApiClient.getInstance().loginWithKakaoAccount(context,  Arrays.asList(Prompt.LOGIN), callback);

    }

    private static final String TAG = "YourClassTag";

}