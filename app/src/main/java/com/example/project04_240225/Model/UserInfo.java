package com.example.project04_240225.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project04_240225.Network.DTO.Data;
import com.example.project04_240225.Network.DTO.ResponseData;
import com.example.project04_240225.Network.SharedPreferencesManager;
import com.example.project04_240225.Network.UserService;
import com.example.project04_240225.Result;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInfo {


    UserService userService;
    SharedPreferencesManager sharedPreferencesManager;

    public UserInfo(UserService userService, SharedPreferencesManager sharedPreferencesManager){
        this.userService = userService;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }


    public LiveData<Data> getInfo(){

        return get("GetUserInfo");

    }


    public LiveData<Data> logoutRequest() {

        return post("LogoutController");


    }
    public LiveData<Data> setNickname(String nicknameValue) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name",nicknameValue);

        return post("setNameController",jsonObject);


    }

    public LiveData<Data> 비밀번호재설정(String 현재비밀번호,String 새비밀번호,String 자동입력방지문자) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pw",현재비밀번호);
        jsonObject.addProperty("newPw",새비밀번호);
        jsonObject.addProperty("code",자동입력방지문자);

        return post("setPasswordController",jsonObject);

    }



    public LiveData<Data> 회원탈퇴요청(){

        return post("WithdrawController");

    }

    public LiveData<Data> deleteProfileImage(){

        return post("deleteImageController");

    }
    public LiveData<Data> 연락처정보변경요청(String authType, String 인증데이터, String 인증번호){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auth_type",authType);
        jsonObject.addProperty("auth_data",인증데이터);
        jsonObject.addProperty("auth_code",인증번호);

        return post("setAuthData",jsonObject);

    }


    public LiveData<Data> sendImage(MultipartBody.Part imageTypeBodyData){

        return uploadImage("",imageTypeBodyData);

    }

    public LiveData<Data> get(String path) {

        Log.i("get","LiveData<Data> get(String path)");

        MutableLiveData<Data> liveData = new MutableLiveData<>();

        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestGET(path).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                Log.i("code",Integer.toString(response.code()));

                if (response.isSuccessful() && response.body() != null) {

                    ResponseData available = response.body();

                    String sessionCookie = response.headers().get("Set-Cookie");
                    if (sessionCookie != null) {
                        sharedPreferencesManager.saveSessionCookie(sessionCookie);
                    }

                    liveData.setValue(new Data(true,available.getJsonObject()));
//                    Log.i("",available.error_description);

                } else {

                    // 요청 실패 처리
                    liveData.setValue(new Data(false,Integer.toString(response.code()) + ":" +response.message()));

                }

            }

            @Override
             public void onFailure(Call<ResponseData> call, Throwable t) {
                // 네트워크 요청 실패 처리
                Log.i("onFailure",t.getMessage());
                liveData.setValue(new Data(false, t.getMessage()));

            }

        });

        return liveData;

    }


    public LiveData<Data> post(String path, JsonObject jsonObject) {

        MutableLiveData<Data> liveData = new MutableLiveData<>();

        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST(path, jsonObject).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful() && response.body() != null) {

                    ResponseData available = response.body();

                    String sessionCookie = response.headers().get("Set-Cookie");
                    if (sessionCookie != null) {
                        sharedPreferencesManager.saveSessionCookie(sessionCookie);
                    }
//                    liveData.postValue();
                    liveData.postValue(new Data(true,available.getJsonObject()));
//                    Log.i("post",available.getJsonObject().toString());
//                    2024-03-14 19:34:38.216  9307-9307  post                    com.example.project04_240225         I  {"result":"success"}
//                    Log.i("post",available.getJsonObject().get("result").getAsString());

                } else {

                    // 요청 실패 처리
                    liveData.postValue(new Data(false,response.message()));
                    Log.i("onFailure2",response.message());

                }

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                // 네트워크 요청 실패 처리
                liveData.postValue(new Data(false, t.getMessage()));
                Log.i("onFailure",t.getMessage());

            }
        });

        return liveData;

    }

    public LiveData<Data> post(String path) {

        MutableLiveData<Data> liveData = new MutableLiveData<>();

        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST(path).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful() && response.body() != null) {

                    ResponseData available = response.body();

                    String sessionCookie = response.headers().get("Set-Cookie");
                    if (sessionCookie != null) {
                        sharedPreferencesManager.saveSessionCookie(sessionCookie);
                    }

                    liveData.postValue(new Data(true,available.getJsonObject()));


                } else {

                    // 요청 실패 처리
                    liveData.setValue(new Data(false,response.message()));

                }

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                // 네트워크 요청 실패 처리
                liveData.setValue(new Data(false, t.getMessage()));

            }
        });

        return liveData;

    }

    public LiveData<Data> uploadImage(String path, MultipartBody.Part imageTypeBodyData) {

        MutableLiveData<Data> liveData = new MutableLiveData<>();

        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.uploadImage(path, imageTypeBodyData).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if (response.isSuccessful() && response.body() != null) {

                    ResponseData available = response.body();

                    String sessionCookie = response.headers().get("Set-Cookie");
                    if (sessionCookie != null) {
                        sharedPreferencesManager.saveSessionCookie(sessionCookie);
                    }

                    Log.i("onResponse",available.getJsonObject().toString());
                    liveData.postValue(new Data(true,available.getJsonObject()));

                } else {

                    // 요청 실패 처리
                    Log.i("onResponse",response.message());
                    liveData.postValue(new Data(false,response.message()));

                }

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                // 네트워크 요청 실패 처리
                Log.i("oonFailure",t.getMessage());
                liveData.postValue(new Data(false, t.getMessage()));

            }
        });

        return liveData;

    }




    public LiveData<Result> Request() {

        MutableLiveData<Result> liveData = new MutableLiveData<>();

        JsonObject jsonObject = new JsonObject();
        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST2("getKey", jsonObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (response.isSuccessful() && response.body() != null) {
                        // InputStream을 통해 Bitmap 변환
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        // LiveData에 Bitmap을 Base64 문자열로 변환하여 저장
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                        Toast.makeText(applicationContext, Integer.toString(encodedImage.length()), Toast.LENGTH_SHORT).show();
                        // 요청 실패 처리

                        liveData.postValue(new Result(true,encodedImage,Color.TRANSPARENT));
                    } else {
//                        Toast.makeText(applicationContext, "요청 실패 처리", Toast.LENGTH_SHORT).show();
                        // 요청 실패 처리
                        liveData.postValue(null);
                    }

                } else {
//                    Toast.makeText(applicationContext, "요청 실패 처리2", Toast.LENGTH_SHORT).show();
                    // 요청 실패 처리
                    liveData.postValue(null);
                    // 요청 실패 처리
//                    liveData.setValue(new Result(false, "요청 처리 실패, 네트워크 오류가 발생했습니다.", Color.RED));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 네트워크 요청 실패 처리
//                liveData.setValue(new Result(false, t.getMessage()+" : "+"네트워크 오류가 발생했습니다.", Color.RED));
            }
        });

        return liveData;
    }

}
