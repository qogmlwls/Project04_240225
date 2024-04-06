package com.example.project04_240225.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;

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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat {



    UserService userService;
    SharedPreferencesManager sharedPreferencesManager;

    public Chat(UserService userService, SharedPreferencesManager sharedPreferencesManager){
        this.userService = userService;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }


    public LiveData<Data> 채팅목록가져오기(){

        return get("getChatListController");

    }

    public LiveData<Data> 채팅내용가져오기(int roomId){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("roomId",Integer.toString(roomId));

        return post("getChatsController",jsonObject);

    }


    public LiveData<Data> logoutRequest() {

        return post("LogoutController");


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


}
