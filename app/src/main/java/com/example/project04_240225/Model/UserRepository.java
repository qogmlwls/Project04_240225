package com.example.project04_240225.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project04_240225.Result;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;


public class UserRepository {
    public interface UserService {
//      @GET("project04/public/index.php?")
//        Call<Boolean> checkUserIdAvailability(@Query("action") String action,@Query("userId") String userId);
        //     @POST("index.php") ///{userId}
        ///{userId}/index.php?action=checkUsername  action={action}//@Path("userId") String userId
//        @POST("project04/src/Controller/LoginController.php") ///{userId}/index.php?action=checkUsername  action={action}
//            //     @POST("index.php") ///{userId}            //     @POST("index.php") ///{userId}
//        Call<String> requestPOST(@Body JsonObject userId);//@Path("userId") String userId

        @POST("project04/src/Controller/{path}.php") ///{userId}/index.php?action=checkUsername  action={action}
        Call<Data> requestPOST(@Path("path") String path,@Body JsonObject userId);//@Path("userId") String userId


        @POST("project04/src/Controller/{path}.php") ///{userId}/index.php?action=checkUsername  action={action}
        Call<ResponseBody> requestPOST2(@Path("path") String path, @Body JsonObject userId);//@Path("userId") String userId

    }

    class Data{
        String result;

        String error;

        String error_description;
        String resultCode;

        String user_id;


    }

    public static int 존재하는지=1, 중복인지=2;
    Context applicationContext;
    SharedPreferencesManager sharedPreferencesManager;
    public UserRepository(Context context){

        applicationContext=context;
        this.sharedPreferencesManager = new SharedPreferencesManager(context);

    }

    class SharedPreferencesManager {
        private static final String PREF_NAME = "MyAppPrefs";
        private static final String KEY_SESSION_COOKIE = "sessionCookie";

        private SharedPreferences sharedPreferences;

        public SharedPreferencesManager(Context context) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }

        public void saveSessionCookie(String cookieValue) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_SESSION_COOKIE, cookieValue);
            editor.apply();
        }

        public String getSessionCookie() {
            return sharedPreferences.getString(KEY_SESSION_COOKIE, null);
        }
    }
    class CookieInterceptor implements Interceptor {

        private SharedPreferencesManager sharedPreferencesManager;

        public CookieInterceptor(Context context) {
            this.sharedPreferencesManager = new SharedPreferencesManager(context);
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request.Builder requestBuilder = chain.request().newBuilder();

            // 여기에서 저장된 쿠키를 가져와 설정
            String savedCookie = sharedPreferencesManager.getSessionCookie();

            if (savedCookie != null) {
                requestBuilder.addHeader("Cookie", savedCookie);
            }

            return chain.proceed(requestBuilder.build());
        }
    }

    private UserService getUserService(){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CookieInterceptor(applicationContext))
                .build();

//        http://49.247.30.164/project04/src/Controller/getKey.php
        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl("http://49.247.30.164/") // 서버 URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                //  .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory 추가
                .build();

        UserService userService = retrofit.create(UserService.class);

        return userService;

    }
    public LiveData<Result> RequestSNSJoinRequest(String 회원번호,String 가입종류, String 닉네임) {


        Log.i("RequestSNSJoinRequest",회원번호);
        Log.i("RequestSNSJoinRequest",가입종류);
        Log.i("RequestSNSJoinRequest",닉네임);



        MutableLiveData<Result> liveData = new MutableLiveData<>();
        UserService userService = getUserService();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("회원번호",회원번호);
        jsonObject.addProperty("가입종류",가입종류);
        jsonObject.addProperty("닉네임",닉네임);

        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST("SNSJoinController", jsonObject).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.i("SNSJoinController","onResponse");

                if (response.isSuccessful() && response.body() != null) {
                    Log.i("onResponse","");
                    Data available = response.body();
                    Log.i("SNSJoinController",available.result);
//                    Log.i("checkSNSLoginRequest",available.user_id);

                    if(available.result.equals("success")){
                        String sessionCookie = response.headers().get("Set-Cookie");
                        if (sessionCookie != null) {
                            sharedPreferencesManager.saveSessionCookie(sessionCookie);
                        }
                        liveData.setValue(new Result(true, "", Color.TRANSPARENT));
                    }
                    else if(available.result.equals("fail")){
                        liveData.setValue(new Result(false, "", Color.TRANSPARENT));
                    }
                    else if(available.result.equals("error")){
                        liveData.setValue(new Result(false, available.error + " : "+ available.error_description, Color.RED));
                    }


                } else {
                    // 요청 실패 처리
                    Log.i("SNSJoinController",response.message());

                    liveData.setValue(new Result(false, "요청 처리 실패, 네트워크 오류가 발생했습니다.", Color.RED));
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // 네트워크 요청 실패 처리
                Log.i("SNSJoinController",t.getMessage());

                liveData.setValue(new Result(false, t.getMessage()+" : "+"네트워크 오류가 발생했습니다.", Color.RED));
            }
        });

        return liveData;


    }

    public LiveData<Result> checkSNSLoginRequest(String snsType, String 회원번호) {

        Log.i("checkSNSLoginRequest",snsType);
        Log.i("checkSNSLoginRequest",회원번호);
//        Log.i("checkSNSLoginRequest","");
//        Log.i("checkSNSLoginRequest","");


        MutableLiveData<Result> liveData = new MutableLiveData<>();
        UserService userService = getUserService();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id",회원번호);
        jsonObject.addProperty("sns_type",snsType);

        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST("SNSLoginController", jsonObject).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {

                if (response.isSuccessful() && response.body() != null) {
                    Log.i("onResponse","");
                    Data available = response.body();
                    Log.i("checkSNSLoginRequest",available.result);
//                    Log.i("checkSNSLoginRequest",available.user_id);

                    if(available.result.equals("success")){
                        String sessionCookie = response.headers().get("Set-Cookie");
                        if (sessionCookie != null) {
                            sharedPreferencesManager.saveSessionCookie(sessionCookie);
                        }
                        liveData.setValue(new Result(true, "", Color.TRANSPARENT));
                    }
                    else if(available.result.equals("fail")){
                        liveData.setValue(new Result(false, "", Color.TRANSPARENT));
                    }
                    else if(available.result.equals("error")){
                        liveData.setValue(new Result(false, available.error + " : "+ available.error_description, Color.RED));
                    }


                } else {
                    // 요청 실패 처리
                    liveData.setValue(new Result(false, "요청 처리 실패, 네트워크 오류가 발생했습니다.", Color.RED));
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // 네트워크 요청 실패 처리
                liveData.setValue(new Result(false, t.getMessage()+" : "+"네트워크 오류가 발생했습니다.", Color.RED));
            }
        });

        return liveData;

    }
    public LiveData<Result> Request() {


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CookieInterceptor(applicationContext))
                .build();
//        http://49.247.30.164/project04/src/Controller/getKey.php
        MutableLiveData<Result> liveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl("http://49.247.30.164/") // 서버 URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                //  .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory 추가
                .build();
        UserService userService = retrofit.create(UserService.class);
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
                        Toast.makeText(applicationContext, "요청 실패 처리", Toast.LENGTH_SHORT).show();
                        // 요청 실패 처리
                        liveData.postValue(null);
                    }

                } else {
                    Toast.makeText(applicationContext, "요청 실패 처리2", Toast.LENGTH_SHORT).show();
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


    public LiveData<Result> checkLoginRequest() {


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CookieInterceptor(applicationContext))
                .build();

        MutableLiveData<Result> liveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl("http://49.247.30.164/") // 서버 URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                //  .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory 추가
                .build();
        UserService userService = retrofit.create(UserService.class);
        JsonObject jsonObject = new JsonObject();
        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST("CheckLoginController", jsonObject).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Data available = response.body();
                    if(available.result.equals("success")){
                        String sessionCookie = response.headers().get("Set-Cookie");
                        if (sessionCookie != null) {
                            sharedPreferencesManager.saveSessionCookie(sessionCookie);
                        }
                        liveData.setValue(new Result(true, "", Color.TRANSPARENT));
                    }
                    else if(available.result.equals("fail")){
                        liveData.setValue(new Result(false, "", Color.TRANSPARENT));
                    }
                    else if(available.result.equals("error")){
                        liveData.setValue(new Result(false, available.error + " : "+ available.error_description, Color.RED));
                    }


                } else {
                    // 요청 실패 처리
                    liveData.setValue(new Result(false, "요청 처리 실패, 네트워크 오류가 발생했습니다.", Color.RED));
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // 네트워크 요청 실패 처리
                liveData.setValue(new Result(false, t.getMessage()+" : "+"네트워크 오류가 발생했습니다.", Color.RED));
            }
        });

        return liveData;
    }

    public LiveData<Result> resetPwRequest(String user_id, String user_pw, String auth_type, String auth_data, String auth_code, String auth_captchaCode){


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CookieInterceptor(applicationContext))
                .build();

        MutableLiveData<Result> liveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl("http://49.247.30.164/") // 서버 URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                //  .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory 추가
                .build();
        UserService userService = retrofit.create(UserService.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id",user_id);
        jsonObject.addProperty("user_pw",user_pw);
        jsonObject.addProperty("auth_type",auth_type);
        jsonObject.addProperty("auth_data",auth_data);
        jsonObject.addProperty("auth_code",auth_code);
        jsonObject.addProperty("auth_captchaCode",auth_captchaCode);

        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST("ResetPasswordController", jsonObject).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Data available = response.body();
                    if(available.result.equals("success")){
                        String sessionCookie = response.headers().get("Set-Cookie");
                        if (sessionCookie != null) {
                            sharedPreferencesManager.saveSessionCookie(sessionCookie);
                        }
                        liveData.setValue(new Result(true, "", Color.TRANSPARENT));
                    }
                    else if(available.result.equals("fail")){

                        if(available.resultCode.equals("201")){
                            // 중복검사 미통과
                            liveData.setValue(new Result(false, "", Color.RED));
                        }
                        else if(available.resultCode.equals("202")){
                            // 중복검사 미통과
                            liveData.setValue(new Result(false, "", Color.RED));
                        }
                        else{
                            liveData.setValue(new Result(false, "", Color.RED));

                        }

                    }
                    else if(available.result.equals("error")){
                        liveData.setValue(new Result(false, available.error + " : "+ available.error_description, Color.RED));
                    }

                } else {
                    // 요청 실패 처리
                    liveData.setValue(new Result(false, "요청 처리 실패, 네트워크 오류가 발생했습니다.", Color.RED));
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // 네트워크 요청 실패 처리
                liveData.setValue(new Result(false, t.getMessage()+" : "+"네트워크 오류가 발생했습니다.", Color.RED));
            }
        });

        return liveData;



    }

    public LiveData<Result> loginRequest(String id, String pw) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CookieInterceptor(applicationContext))
                .build();

        MutableLiveData<Result> liveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl("http://49.247.30.164/") // 서버 URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                //  .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory 추가
                .build();
        UserService userService = retrofit.create(UserService.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id",id);
        jsonObject.addProperty("user_pw",pw);

        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST("LoginController", jsonObject).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Data available = response.body();
                    if(available.result.equals("success")){
                        String sessionCookie = response.headers().get("Set-Cookie");
                        if (sessionCookie != null) {
                            sharedPreferencesManager.saveSessionCookie(sessionCookie);
                        }
                        liveData.setValue(new Result(true, "", Color.TRANSPARENT));
                    }
                    else if(available.result.equals("fail")){
                        liveData.setValue(new Result(false, "아이디(로그인 전용 아이디) 또는 비밀번호를 잘못 입력했습니다.\n" +
                                "입력하신 내용을 다시 확인해주세요.", Color.RED));
                    }
                    else if(available.result.equals("error")){
                        liveData.setValue(new Result(false, available.error + " : "+ available.error_description, Color.RED));
                    }

//
//                    if (available) {
//                        liveData.setValue(new Result(true, "로그인 성공", Color.TRANSPARENT));
//                    } else {
//                        liveData.setValue(new Result(false, " 아이디(로그인 전용 아이디) 또는 비밀번호를 잘못 입력했습니다.\n" +
//                                "입력하신 내용을 다시 확인해주세요.", Color.RED));
//                    }

                } else {
                    // 요청 실패 처리
                    liveData.setValue(new Result(false, "요청 처리 실패, 네트워크 오류가 발생했습니다.", Color.RED));
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // 네트워크 요청 실패 처리
                liveData.setValue(new Result(false, t.getMessage()+" : "+"네트워크 오류가 발생했습니다.", Color.RED));
            }
        });

        return liveData;

    }


    public LiveData<Result> isUserIdAvailable(String userId) {
        return isUserIdAvailable(userId,중복인지);
    }


    public LiveData<Result> isUserIdAvailable(String userId,int 검사타입) {


        MutableLiveData<Result> liveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl("http://49.247.30.164/") // 서버 URL
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                //  .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory 추가
                .build();
        UserService userService = retrofit.create(UserService.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id",userId);
        jsonObject.addProperty("check_type",검사타입);

        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST("CheckUsernameController", jsonObject).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Data available = response.body();
                    if(available.result.equals("success")){
                        liveData.setValue(new Result(true, "", Color.TRANSPARENT));//사용 가능한 아이디입니다.
                    }
                    else if(available.result.equals("fail")){

                        if(available.resultCode.equals("201")){
                            // 중복검사 미통과
                            liveData.setValue(new Result(false, "아이디가 이미 사용 중입니다.", Color.RED));
                        }
                        else if(available.resultCode.equals("202")){
                            // 중복검사 미통과
                            liveData.setValue(new Result(false, "입력하신 아이디를 찾을 수 없습니다.", Color.RED));
                        }
                        else{
                            liveData.setValue(new Result(false, "", Color.RED));

                        }


                    }
                    else if(available.result.equals("error")){
                        liveData.setValue(new Result(false, available.error + " : "+ available.error_description, Color.RED));
                    }

                } else {
                    // 요청 실패 처리
                    liveData.setValue(new Result(false, "요청 처리 실패, 네트워크 오류가 발생했습니다.", Color.RED));
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // 네트워크 요청 실패 처리
                liveData.setValue(new Result(false, t.getMessage()+" : "+"네트워크 오류가 발생했습니다.", Color.RED));
            }
        });

        return liveData;

    }



    public LiveData<Result> isAuthenticationAvailable(String authType, String authData) {

        MutableLiveData<Result> liveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl("http://49.247.30.164/") // 서버 URL
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                //  .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory 추가
                .build();
        UserService userService = retrofit.create(UserService.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auth_type",authType);
        jsonObject.addProperty("auth_data",authData);

        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST("CheckAuthController", jsonObject).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Data available = response.body();
                    if(available.result.equals("success")){
                        liveData.setValue(new Result(true, "", Color.TRANSPARENT));//사용 가능한 아이디입니다.
                    }
                    else if(available.result.equals("fail")){
                        if(authType.equals("email")){
                            liveData.setValue(new Result(false, "이메일: 이미 가입된 이메일 주소입니다.", Color.RED));
                        }
                        else if(authType.equals("phone")){
                            liveData.setValue(new Result(false, "휴대전화번호: 이미 가입된 휴대전화번호입니다.", Color.RED));
                        }
                        else{
                            liveData.setValue(new Result(false, "", Color.RED));
                        }
                    }
                    else if(available.result.equals("error")){
                        liveData.setValue(new Result(false, available.error + " : "+ available.error_description, Color.RED));
                    }

                } else {
                    // 요청 실패 처리
                    liveData.setValue(new Result(false, response.errorBody().toString()+"요청 처리 실패, 네트워크 오류가 발생했습니다.", Color.RED));
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // 네트워크 요청 실패 처리
                liveData.setValue(new Result(false, t.getMessage()+" : "+"네트워크 오류가 발생했습니다.", Color.RED));
            }
        });

        return liveData;

    }
    public LiveData<Result> sendAuthenticationRequest(String authType, String authData) {

        return sendAuthenticationRequest(authType,authData,중복인지,null);
    }
    public LiveData<Result> sendAuthenticationRequest(String authType, String authData, String hashCode) {

        return sendAuthenticationRequest(authType,authData,hashCode,중복인지,null);
    }

    public LiveData<Result> sendAuthenticationRequest(String authType, String authData, int 검사타입  ,String user_id) {


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CookieInterceptor(applicationContext))
                .build();

        MutableLiveData<Result> liveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl("http://49.247.30.164/") // 서버 URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                //  .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory 추가
                .build();
        UserService userService = retrofit.create(UserService.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auth_data",authData);
        jsonObject.addProperty("auth_type",authType);
        jsonObject.addProperty("check_type",검사타입);
        if(user_id != null)
            jsonObject.addProperty("user_id",user_id);
        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST("SendCodeController", jsonObject).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Data available = response.body();
                    if(available.result.equals("success")){

                        String sessionCookie = response.headers().get("Set-Cookie");
                        if (sessionCookie != null) {
                            sharedPreferencesManager.saveSessionCookie(sessionCookie);
                        }

                        liveData.setValue(new Result(true, "인증코드를 발송했습니다",Color.parseColor("#FF4CAF50")));//사용 가능한 아이디입니다.
                    }
                    else if(available.result.equals("fail")){

                        if(available.resultCode.equals("201")){
                            // 중복검사 미통과
                            liveData.setValue(new Result(false, "", Color.TRANSPARENT));
                        }
                        else if(available.resultCode.equals("202")){
                            // 중복검사 미통과
                            liveData.setValue(new Result(false, "입력하신 정보가 회원정보와 일치하는지 확인해 주세요.", Color.RED));
                        }
                        else{
                            liveData.setValue(new Result(false, "인증코드 발송 실패", Color.RED));

                        }

                    }
                    else if(available.result.equals("error")){
                        liveData.setValue(new Result(false, "인증코드 발송 실패 "+available.error + " : "+ available.error_description, Color.RED));
                    }

                } else {
                    // 요청 실패 처리
                    liveData.setValue(new Result(false, response.errorBody().toString()+"요청 처리 실패, 네트워크 오류가 발생했습니다.", Color.RED));
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // 네트워크 요청 실패 처리
                liveData.setValue(new Result(false, t.getMessage()+" : "+"네트워크 오류가 발생했습니다.", Color.RED));
            }
        });

        return liveData;

    }
    public LiveData<Result> sendAuthenticationRequest(String authType, String authData , String hashCode, int 검사타입,String user_id ) {


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CookieInterceptor(applicationContext))
                .connectTimeout(3000, TimeUnit.SECONDS) // 연결 타임아웃 설정
                .readTimeout(3000, TimeUnit.SECONDS) // 읽기 타임아웃 설정
                .writeTimeout(3000, TimeUnit.SECONDS) // 쓰기 타임아웃 설정
                .build();

        MutableLiveData<Result> liveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl("http://49.247.30.164/") // 서버 URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                //  .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory 추가
                .build();
        UserService userService = retrofit.create(UserService.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auth_data",authData);
        jsonObject.addProperty("auth_type",authType);
        jsonObject.addProperty("hashCode",hashCode);
        jsonObject.addProperty("check_type",검사타입);
        if(user_id != null)
            jsonObject.addProperty("user_id",user_id);
        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST("SendCodeController", jsonObject).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {


                if (response.isSuccessful() && response.body() != null) {

                    Data available = response.body();
                    if(available.result.equals("success")){

                        String sessionCookie = response.headers().get("Set-Cookie");
                        if (sessionCookie != null) {
                            sharedPreferencesManager.saveSessionCookie(sessionCookie);
                        }

                        liveData.setValue(new Result(true, "인증코드를 발송했습니다",Color.parseColor("#FF4CAF50")));//사용 가능한 아이디입니다.
                    }
                    else if(available.result.equals("fail")){

                        if(available.resultCode.equals("201")){
                            // 중복검사 미통과
                            liveData.setValue(new Result(false, "", Color.TRANSPARENT));
                        }
                        else if(available.resultCode.equals("202")){
                            // 중복검사 미통과
                            liveData.setValue(new Result(false, "입력하신 정보가 회원정보와 일치하는지 확인해 주세요.", Color.RED));
                        }
                        else{
                            liveData.setValue(new Result(false, "인증코드 발송 실패", Color.RED));

                        }

                    }
                    else if(available.result.equals("error")){
                        liveData.setValue(new Result(false, "인증코드 발송 실패 "+available.error + " : "+ available.error_description, Color.RED));
                    }

                } else {
                    // 요청 실패 처리
                    liveData.setValue(new Result(false, response.message()+"요청 처리 실패, 네트워크 오류가 발생했습니다.", Color.RED));
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // 네트워크 요청 실패 처리
                if("unexpected end of stream".equals(t.getMessage())){

                }
                else{
                    liveData.setValue(new Result(false, t.getMessage()+" : "+"네트워크 오류가 발생했습니다.", Color.RED));
                }
            }
        });

        return liveData;

    }

    public LiveData<Result> checkAuthenticationRequest(int type, String authData, String code ) {


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CookieInterceptor(applicationContext))
                .build();


        MutableLiveData<Result> liveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl("http://49.247.30.164/") // 서버 URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                //  .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory 추가
                .build();
        UserService userService = retrofit.create(UserService.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auth_data",authData);
        jsonObject.addProperty("type",type);
        jsonObject.addProperty("code",code);

        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST("CheckAuthCodeController", jsonObject).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Data available = response.body();
                    if(available.result.equals("success")){

                        String sessionCookie = response.headers().get("Set-Cookie");
                        if (sessionCookie != null) {
                            sharedPreferencesManager.saveSessionCookie(sessionCookie);
                        }

                        liveData.setValue(new Result(true, "인증 성공",Color.parseColor("#FF4CAF50")));//사용 가능한 아이디입니다.
                    }
                    else if(available.result.equals("fail")){
                        liveData.setValue(new Result(false, "인증번호를 다시 확인해주세요.", Color.RED));

                    }
                    else if(available.result.equals("error")){
                        liveData.setValue(new Result(false, available.error + " : "+ available.error_description, Color.RED));
                    }

                } else {
                    // 요청 실패 처리
                    liveData.setValue(new Result(false, "요청 처리 실패, 네트워크 오류가 발생했습니다.", Color.RED));
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // 네트워크 요청 실패 처리
                liveData.setValue(new Result(false, t.getMessage()+" : "+"네트워크 오류가 발생했습니다.", Color.RED));
            }
        });

        return liveData;

    }
    public LiveData<Result> signUpRequest( String user_id, String user_pw, String user_name, String auth_type, String auth_data, String code ) {


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CookieInterceptor(applicationContext))
                .build();


        MutableLiveData<Result> liveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl("http://49.247.30.164/") // 서버 URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                //  .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory 추가
                .build();
        UserService userService = retrofit.create(UserService.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id",user_id);
        jsonObject.addProperty("user_pw",user_pw);
        jsonObject.addProperty("user_name",user_name);
        jsonObject.addProperty("auth_type",auth_type);
        jsonObject.addProperty("auth_data",auth_data);
        jsonObject.addProperty("code",code);


        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST("SignUpController", jsonObject).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Data available = response.body();
                    if(available.result.equals("success")){
                        String sessionCookie = response.headers().get("Set-Cookie");
                        if (sessionCookie != null) {
                            sharedPreferencesManager.saveSessionCookie(sessionCookie);
                        }
                        liveData.setValue(new Result(true, "회원가입 성공", Color.BLUE));//사용 가능한 아이디입니다.
                    }
                    else if(available.result.equals("fail")){
                        liveData.setValue(new Result(false, "회원가입 실패.", Color.RED));

                    }
                    else if(available.result.equals("error")){
                        liveData.setValue(new Result(false, available.error + " : "+ available.error_description, Color.RED));
                    }

                } else {
                    // 요청 실패 처리
                    liveData.setValue(new Result(false, "요청 처리 실패, 네트워크 오류가 발생했습니다.", Color.RED));
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // 네트워크 요청 실패 처리
                liveData.setValue(new Result(false, t.getMessage()+" : "+"네트워크 오류가 발생했습니다.", Color.RED));
            }
        });

        return liveData;

    }

    public LiveData<Result> logoutRequest() {


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CookieInterceptor(applicationContext))
                .build();


        MutableLiveData<Result> liveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl("http://49.247.30.164/") // 서버 URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                //  .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory 추가
                .build();
        UserService userService = retrofit.create(UserService.class);

        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("auth_data",authData);
//        jsonObject.addProperty("type",type);
//        jsonObject.addProperty("code",code);

        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST("LogoutController", jsonObject).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Data available = response.body();
                    if(available.result.equals("success")){

//                        String sessionCookie = response.headers().get("Set-Cookie");
//                        if (sessionCookie != null) {
//                            sharedPreferencesManager.saveSessionCookie(sessionCookie);
//                        }
                        liveData.setValue(new Result(true, "" , Color.TRANSPARENT));

//                        liveData.setValue(new Result(true, "인증 성공",Color.parseColor("#FF4CAF50")));//사용 가능한 아이디입니다.
                    }
                    else if(available.result.equals("fail")){
                        liveData.setValue(new Result(false, "" , Color.TRANSPARENT));

//                        liveData.setValue(new Result(false, "ss.", Color.RED));

                    }
                    else if(available.result.equals("error")){
                        liveData.setValue(new Result(false, available.error + " : "+ available.error_description, Color.RED));
                    }

                } else {
                    // 요청 실패 처리
                    liveData.setValue(new Result(false, "요청 처리 실패, 네트워크 오류가 발생했습니다.", Color.RED));
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // 네트워크 요청 실패 처리
                liveData.setValue(new Result(false, t.getMessage()+" : "+"네트워크 오류가 발생했습니다.", Color.RED));
            }
        });

        return liveData;

    }


    public LiveData<String> nameRequest(){


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CookieInterceptor(applicationContext))
                .build();

        MutableLiveData<String> liveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl("http://49.247.30.164/") // 서버 URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                //  .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory 추가
                .build();
        UserService userService = retrofit.create(UserService.class);

        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("auth_data",authData);
//        jsonObject.addProperty("type",type);
//        jsonObject.addProperty("code",code);

        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST("GetUserInfo", jsonObject).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Data available = response.body();
                    liveData.setValue(available.result);

                } else {
                    // 요청 실패 처리
                    liveData.setValue("");
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // 네트워크 요청 실패 처리
                liveData.setValue(t.getMessage());
            }
        });

        return liveData;

    }
    public LiveData<Result> findAccountIdRequest(String authType, String authData, String code ){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CookieInterceptor(applicationContext))
                .build();

        MutableLiveData<Result> liveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()//POST", "/index.php?action=checkUsername
                .baseUrl("http://49.247.30.164/") // 서버 URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터'
                //  .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory 추가
                .build();
        UserService userService = retrofit.create(UserService.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auth_type",authType);
        jsonObject.addProperty("auth_data",authData);
        jsonObject.addProperty("auth_code",code);

        // Retrofit을 사용한 비동기 네트워크 요청 userId
        userService.requestPOST("FindAccountIdController", jsonObject).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Data available = response.body();
                    if(available.result.equals("success")){

                        String sessionCookie = response.headers().get("Set-Cookie");
                        if (sessionCookie != null) {
                            sharedPreferencesManager.saveSessionCookie(sessionCookie);
                        }
                        String user_id = available.user_id;
                        liveData.setValue(new Result(true, user_id,Color.parseColor("#FF4CAF50")));//사용 가능한 아이디입니다.
                    }
                    else if(available.result.equals("fail")){

                        if(available.resultCode.equals("201")){
                            // 중복검사 미통과
                            liveData.setValue(new Result(false, "입력하신 정보를 다시 확인해주세요.", Color.RED));
                        }
                        else if(available.resultCode.equals("202")){
                            // 중복검사 미통과
                            liveData.setValue(new Result(false, "입력하신 정보를 다시 확인해주세요.", Color.RED));
                        }
                        else{
                            liveData.setValue(new Result(false, "실패", Color.RED));

                        }

                    }
                    else if(available.result.equals("error")){
                        liveData.setValue(new Result(false, available.error + " : "+ available.error_description, Color.RED));
                    }

                } else {
                    // 요청 실패 처리
                    liveData.setValue(new Result(false, "요청 처리 실패, 네트워크 오류가 발생했습니다.", Color.RED));


                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // 네트워크 요청 실패 처리
                liveData.setValue(new Result(false, t.getMessage()+" : "+"네트워크 오류가 발생했습니다.", Color.RED));
            }
        });

        return liveData;

    }


}
