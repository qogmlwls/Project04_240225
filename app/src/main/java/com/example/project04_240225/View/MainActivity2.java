package com.example.project04_240225.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.project04_240225.Model.UserInfo;
import com.example.project04_240225.MyApplication;
import com.example.project04_240225.Network.SharedPreferencesManager;
import com.example.project04_240225.R;
import com.example.project04_240225.Utility.ValidationUtil;
import com.example.project04_240225.ViewModel.UserInfoViewModel;
import com.navercorp.nid.NaverIdLoginSDK;

public class MainActivity2 extends AppCompatActivity {


    UserInfoViewModel userInfoViewModel;
    MyApplication application;
    UserInfo userInfo;
    ValidationUtil validationUtil;
    SharedPreferencesManager sharedPreferencesManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        application = (MyApplication) getApplication();
        userInfo = new UserInfo(application.getUserService(),application.getSharedPreferencesManager());
        validationUtil = new ValidationUtil();
        userInfoViewModel = new UserInfoViewModel(userInfo,validationUtil);
        sharedPreferencesManager = application.getSharedPreferencesManager();


        sharedPreferencesManager.removeLoginType();
        userInfoViewModel.로그아웃();

        userInfoViewModel.getLogoutResult().observe(MainActivity2.this, result -> {
            Log.i("getLogoutResult","");

            Intent intent = new Intent(MainActivity2.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

//            String type = sharedPreferencesManager.getLoginType();
//            Log.i("getLogoutResult",sharedPreferencesManager.getLoginType());
//            Log.i("getLogoutResult(2)",type);
//
//
//            if(type != null && type.equals("KAKAO")) {
//
////                SessionInfo info = new SessionInfo();
////                info.
////                AuthApiClient.getInstance().
////                UserApiClient.getInstance().();
////                CookieManager.getInstance().removeAllCookies(null);
////                CookieManager.getInstance().flush();
////                webView.clearCache(true);
//
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kauth.kakao.com/oauth/logout?client_id=170d84240324d6db8825883836afb314&logout_redirect_uri=https://test123456.com/logout"));
//                startActivity(browserIntent);
//
//                // 로그아웃
////                main();
////                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
////                    @Override
////                    public Unit invoke(Throwable throwable) {
////
////                        if (throwable != null) {
////                            Log.e("TAG", "로그아웃 실패. SDK에서 토큰 삭제됨" +  throwable.getMessage());
////                        }
////                        else {
////                            Log.i("TAG", "로그아웃 성공. SDK에서 토큰 삭제됨");
////                        }
////                        Log.i("main()","main()");
//////                        main();
////                        return null;
////                    }
////                });
//            }
//            else if(type != null && type.equals("NAVER")){
//                //  클라이언트에 저장된 토큰이 삭제
//                NaverIdLoginSDK.INSTANCE.logout();
//                Log.i("logout",NaverIdLoginSDK.INSTANCE.getState().name());
//            }
//            else if(type == null){
//                Log.i("token null","");
//
//            }
//            else{
//                Log.i("token",type);
//            }
//            sharedPreferencesManager.removeLoginType();
//            startActivity(intent);

        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 메모리 누수 방지
        // 옵저버를 제거하지 않으면, 엑티비티가 계속 살아있을 수 있음.
        // 강한 참조를 해서, 가비지 컬렉터가 엑티비티를 메모리에서 제거하지 못하여
        userInfoViewModel.getLogoutResult().removeObservers(MainActivity2.this);

    }

}