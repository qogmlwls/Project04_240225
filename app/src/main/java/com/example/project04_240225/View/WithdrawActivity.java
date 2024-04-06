package com.example.project04_240225.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.project04_240225.Model.UserInfo;
import com.example.project04_240225.MyApplication;
import com.example.project04_240225.Network.SharedPreferencesManager;
import com.example.project04_240225.R;
import com.example.project04_240225.Utility.ValidationUtil;
import com.example.project04_240225.ViewModel.UserInfoViewModel;
import com.kakao.sdk.user.UserApiClient;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.NidOAuthLogin;
import com.navercorp.nid.oauth.OAuthLoginCallback;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class WithdrawActivity extends ToolBarCommonActivity {


    Button 회원탈퇴버튼;

    CheckBox checkBox;

    Toolbar toolbar;

    MyApplication application;
    UserInfo userInfo;
    ValidationUtil validationUtil;
    UserInfoViewModel userInfoViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        회원탈퇴버튼 = findViewById(R.id.button27);
        checkBox = findViewById(R.id.checkBox);
        toolbar = findViewById(R.id.toolbar9);

        application = (MyApplication) getApplication();
        userInfo = new UserInfo(application.getUserService(),application.getSharedPreferencesManager());
        validationUtil = new ValidationUtil();
        userInfoViewModel = new UserInfoViewModel(userInfo,validationUtil);

        툴바뒤로가기설정(toolbar);



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // 체크 박스가 체크된 경우의 동작
//                    Toast.makeText(WithdrawActivity.this, "체크 박스가 체크된 경우의 동작", Toast.LENGTH_SHORT).show();
                    회원탈퇴버튼.setEnabled(true);
                } else {
                    // 체크 박스가 체크되지 않은 경우의 동작
//                    Toast.makeText(WithdrawActivity.this, "체크 박스가 체크되지 않은 경우의 동작", Toast.LENGTH_SHORT).show();
                    회원탈퇴버튼.setEnabled(false);
                }
    
            }
        });
        SharedPreferencesManager sharedPreferencesManager;
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());



//        회원탈퇴 명령어 전송
        회원탈퇴버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 서버에 요청

                String type = sharedPreferencesManager.getLoginType();

                if(type == null){
                    Log.i("회원탈퇴버튼", "null...");
                }
                else{
                    Log.i("회원탈퇴버튼", sharedPreferencesManager.getLoginType());
                }


                if(type != null && ( type.equals("KAKAO") ||  type.equals("KAKAOACCOUNT"))) {
                    // 로그아웃
                    UserApiClient.getInstance().unlink(new Function1<Throwable, Unit>() {
                        @Override
                        public Unit invoke(Throwable throwable) {

                            if (throwable != null) {
                                Log.e("TAG", "연결끊기 실패." + throwable.getMessage());
                            } else {
                                Log.i("TAG", "연결끊기 성공. SDK에서 토큰 삭제됨");

                                userInfoViewModel.회원탈퇴요청();
                                sharedPreferencesManager.removeLoginType();
                                // 회원탈퇴했다.표시
                                sharedPreferencesManager.saveLoginType2("withdraw");

//                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kauth.kakao.com/oauth/logout?client_id=170d84240324d6db8825883836afb314&logout_redirect_uri=https://test123456.com/logout"));
//                                startActivity(browserIntent);


                                Intent intent = new Intent(WithdrawActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                            return null;
                        }
                    });

                }
                else if(type != null && type.equals("NAVER")){


                    // 토큰이 유효한 토큰일 경우 정상적으로 연동 해제가 되며
                    // 토큰이 유효하지 않을 경우에도 결과가 'succes'반환됨.
//                    토큰이 유효한지 먼저 검증한 다음 유효한 토큰으로 갱신하여 연동해제 처리를 하시면 됩니다.
//                    연동해제를 확인하려면 delete token 이후,
//                    기존 발급 refresh token을 이용하여 더이상 token refresh를 할 수 없을 경우
//                    정상 연동해지가 되었다고 판단하시는 방법이 있습니다.


                    if(NaverIdLoginSDK.INSTANCE.getRefreshToken() != null) {

                        new NidOAuthLogin().callDeleteTokenApi(new OAuthLoginCallback() {
                            @Override
                            public void onSuccess() {
                                //서버에서 토큰 삭제에 성공한 상태입니다.
                                Log.i("token", "네이버 서버 토큰 삭제");
                                userInfoViewModel.회원탈퇴요청();
                                sharedPreferencesManager.removeLoginType();

                                Intent intent = new Intent(WithdrawActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(int i, @NonNull String s) {
                                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.

//                                Log.d(TAG, "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
//                                Log.d(TAG, "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")

                            }

                            @Override
                            public void onError(int i, @NonNull String s) {
                                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
//                                onFailure(errorCode, message)
                            }
                        });
                    }
                    else{

                        sharedPreferencesManager.removeLoginType();
                        Toast.makeText(application, "로그인을 해주세요", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(WithdrawActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }

                }
                else{
                    Log.i("??","");
                    userInfoViewModel.회원탈퇴요청();
                    sharedPreferencesManager.removeLoginType();

                    Intent intent = new Intent(WithdrawActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }



            }
        });

    }
}