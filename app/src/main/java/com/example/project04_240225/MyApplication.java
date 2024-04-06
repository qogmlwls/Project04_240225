package com.example.project04_240225;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.example.project04_240225.Model.UserInfo;
import com.example.project04_240225.Model.UserRepository;
import com.example.project04_240225.Network.CookieInterceptor;
import com.example.project04_240225.Network.SharedPreferencesManager;
import com.example.project04_240225.Network.UserService;
import com.example.project04_240225.Utility.ValidationUtil;
import com.example.project04_240225.ViewModel.UserInfoViewModel;
import com.kakao.sdk.common.KakaoSdk;
import com.navercorp.nid.NaverIdLoginSDK;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {

    UserService userService;

    SharedPreferencesManager sharedPreferencesManager;

    String baseURL = "http://49.247.30.164/";

    ValidationUtil validationUtil;
    UserInfoViewModel userInfoViewModel;
    UserInfo userInfo;



    private MutableLiveData<String> changeProfileImageResult = new MutableLiveData<>();
    private MutableLiveData<String> changeProfileNameResult = new MutableLiveData<>();

    public MutableLiveData<String> getChangeProfileImageResult() {
        return changeProfileImageResult;
    }
    public MutableLiveData<String> getChangeProfileNameResult() {
        return changeProfileNameResult;
    }

    public void setChangeProfileImageResult(String profileImageUrl){
        changeProfileImageResult.setValue(profileImageUrl);
    }

    public void setChangeProfileNameResult(String profileName){
        changeProfileNameResult.setValue(profileName);
    }

    public String getBaseURL() {
        return baseURL;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CookieInterceptor(this))
                .build();

        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl(baseURL) // 서버 URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                .build();

        userService = retrofit.create(UserService.class);
        sharedPreferencesManager = new SharedPreferencesManager(this);

        userInfo = new UserInfo(userService,sharedPreferencesManager);

        validationUtil = new ValidationUtil();
        userInfoViewModel = new UserInfoViewModel(userInfo,validationUtil);

        // Kakao SDK 초기화
        KakaoSdk.init(this, "5bca12ef41e8ed7d2a695d850285515b");
//        NaverIdLoginSDK
//        NaverIdLoginSDK.INSTANCE.initialize();
        NaverIdLoginSDK.INSTANCE.initialize(getApplicationContext(),
                "QMIZBcu520HWbx_BsN0t",
                "dsrdVnaEWY",
                "project4");

    }


    public UserInfoViewModel getUserInfoViewModel() {
        return userInfoViewModel;
    }

    public SharedPreferencesManager getSharedPreferencesManager() {
        return sharedPreferencesManager;
    }

    public UserService getUserService() {
        return userService;
    }


}
