package com.example.project04_240225.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project04_240225.BottomSheet;
import com.example.project04_240225.Model.UserInfo;
import com.example.project04_240225.MyApplication;
import com.example.project04_240225.Network.DTO.UserInfoData;
import com.example.project04_240225.Network.SharedPreferencesManager;
import com.example.project04_240225.R;
import com.example.project04_240225.Utility.ImageLoader;
import com.example.project04_240225.Utility.ValidationUtil;
import com.example.project04_240225.ViewModel.UserInfoViewModel;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.cert.model.SessionInfo;
import com.kakao.sdk.user.UserApiClient;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.NidOAuthLogin;
import com.navercorp.nid.oauth.OAuthLoginCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class EditProfileActivity extends ToolBarCommonActivity {

    BottomSheet bottomSheet;

    ImageView 프로필이미지뷰;

    Button 로그아웃버튼, 회원탈퇴버튼;

    LinearLayout 닉네임수정화면으로이동, 연락처정보수정화면으로이동,비밀번호변경으로이동;//휴대전화번호추가수정화면으로이동,

    UserInfoViewModel userInfoViewModel;
    MyApplication application;
    UserInfo userInfo;
    ValidationUtil validationUtil;

    TextView 닉네임텍스트뷰, 연락처정보텍스트뷰;

    TextView 프로필이미지삭제;

    UserInfoData userInfoData;


    private static final int REQUEST_PERMISSION_CODE = 101;

//    WithdrawController

    public static String 연락처정보;

    SharedPreferencesManager sharedPreferencesManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        }

        연락처정보 = null;


        닉네임수정화면으로이동 = findViewById(R.id.linearLayout1);
        연락처정보수정화면으로이동 = findViewById(R.id.linearLayout2);
//        휴대전화번호추가수정화면으로이동 = findViewById(R.id.linearLayout3);
        비밀번호변경으로이동 = findViewById(R.id.linearLayout4);

        로그아웃버튼 = findViewById(R.id.button21);
        회원탈퇴버튼 = findViewById(R.id.button22);
        닉네임텍스트뷰 = findViewById(R.id.textView42);
        연락처정보텍스트뷰 = findViewById(R.id.textView44);

        userInfoData = null;

        // Toolbar 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar5);
        툴바뒤로가기설정(toolbar);

        application = (MyApplication) getApplication();
        userInfo = new UserInfo(application.getUserService(),application.getSharedPreferencesManager());
        validationUtil = new ValidationUtil();
        userInfoViewModel = new UserInfoViewModel(userInfo,validationUtil);
        userInfoViewModel.getUserInfodata();
        sharedPreferencesManager = application.getSharedPreferencesManager();

//        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());

//        String result_type = sharedPreferencesManager.getLoginType();
//
//        Log.i("getLoginType result", result_type);

        userInfoViewModel.getUserInfoResult().observe(EditProfileActivity.this, userInfo -> {
            if (userInfo != null) {

                userInfoData = userInfo;

                // 데이터 사용, 예: userInfo.getName(), userInfo.getProfileImageUrl()
                닉네임텍스트뷰.setText(userInfo.getName());
                if(userInfo.hasProfileImage()){

                    ImageLoader.loadProfileImage(
                            this, // 현재 Context
                            application.getBaseURL() +userInfo.getProfileImageUrl(), // 로드할 이미지 URL
                            프로필이미지뷰, // 이미지를 로드할 ImageView
                            프로필이미지뷰.getDrawable() // 플레이스홀더 이미지
                    );
                    프로필이미지삭제.setVisibility(View.VISIBLE);

                }
                else{
                    프로필이미지뷰.setImageDrawable(getDrawable(R.drawable.person));
                }
                연락처정보텍스트뷰.setText(userInfo.getAuthData());

            }
            else{
                Toast.makeText(application, "프로필 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });

//        application.getChangeProfileImageResult().observe(EditProfileActivity.this, profileImageUrl -> {
//
//            if(profileImageUrl != null ){
//                ImageLoader.loadProfileImage(
//                        this, // 현재 Context
//                        application.getBaseURL() +profileImageUrl, // 로드할 이미지 URL
//                        프로필이미지뷰, // 이미지를 로드할 ImageView
//                        프로필이미지뷰.getDrawable() // 플레이스홀더 이미지
//                );
//            }
//            else{
//                프로필이미지뷰.setImageDrawable(getDrawable(R.drawable.person));
//            }
//
//        });
        application.getChangeProfileNameResult().observe(EditProfileActivity.this, name -> {

            닉네임텍스트뷰.setText(name);

        });


        프로필이미지뷰 = findViewById(R.id.imageView2);
        프로필이미지뷰.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheet = new BottomSheet();
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
//                UPDATE user
//                SET update_date = NULL WHERE user_pk=1;
//                EditProfileImgaeController.php


            }
        });

        연락처정보수정화면으로이동.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EditProfileActivity.this, EditAuthDataActivity.class);
//                intent.putExtra("기존닉네임",닉네임텍스트뷰.getText().toString());
//                intent.putExtra("기존닉네임",닉네임텍스트뷰.getText().toString());
                startActivity(intent);

            }
        });

        프로필이미지삭제 = findViewById(R.id.textView40);
        프로필이미지삭제.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userInfoViewModel.deleteProfileImage();
                프로필이미지뷰.setImageDrawable(getDrawable(R.drawable.person));
                프로필이미지삭제.setVisibility(View.INVISIBLE);
                application.setChangeProfileImageResult(null);

            }
        });

        로그아웃버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String type = sharedPreferencesManager.getLoginType();
                Log.i("getLogoutResult",sharedPreferencesManager.getLoginType());
                Log.i("getLogoutResult(2)",type);


                if(type != null && type.equals("KAKAOACCOUNT")) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kauth.kakao.com/oauth/logout?client_id=170d84240324d6db8825883836afb314&logout_redirect_uri=https://test123456.com/logout"));
                    startActivity(browserIntent);
                    return;
                }
                else if(type != null && type.equals("KAKAO")) {

                    UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                        @Override
                        public Unit invoke(Throwable throwable) {

                            if (throwable != null) {
                                Log.e("TAG", "로그아웃 실패. SDK에서 토큰 삭제됨" +  throwable.getMessage());
                            }
                            else {
                                Log.i("TAG", "로그아웃 성공. SDK에서 토큰 삭제됨");
                            }
                            Log.i("main()","main()");
                            sharedPreferencesManager.removeLoginType();
                            userInfoViewModel.로그아웃();
//                        main();
                            return null;
                        }
                    });
                    return;

                }
                else if(type != null && type.equals("NAVER")){
                    //  클라이언트에 저장된 토큰이 삭제
                    NaverIdLoginSDK.INSTANCE.logout();
                    Log.i("logout",NaverIdLoginSDK.INSTANCE.getState().name());
                }
                else if(type == null){
                    Log.i("token null","");

                }
                else{
                    Log.i("token",type);
                }
                sharedPreferencesManager.removeLoginType();

                userInfoViewModel.로그아웃();

            }
        });

        회원탈퇴버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EditProfileActivity.this, WithdrawActivity.class);
                startActivity(intent);
//                // 다이얼로그 보여지기
//                userInfoViewModel.회원탈퇴();

            }
        });


        application.getChangeProfileNameResult().observe(EditProfileActivity.this, name -> {

            닉네임텍스트뷰.setText(name);

        });


        userInfoViewModel.getLogoutResult().observe(EditProfileActivity.this, result -> {
            Log.i("getLogoutResult","");

            Intent intent = new Intent(EditProfileActivity.this,LoginActivity.class);
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
//                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
//                    @Override
//                    public Unit invoke(Throwable throwable) {
//
//                        if (throwable != null) {
//                            Log.e("TAG", "로그아웃 실패. SDK에서 토큰 삭제됨" +  throwable.getMessage());
//                        }
//                        else {
//                            Log.i("TAG", "로그아웃 성공. SDK에서 토큰 삭제됨");
//                        }
//                        Log.i("main()","main()");
////                        main();
//                        return null;
//                    }
//                });
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


        닉네임수정화면으로이동.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, EditNameActivity.class);
                intent.putExtra("기존닉네임",닉네임텍스트뷰.getText().toString());
                startActivity(intent);
            }
        });

        비밀번호변경으로이동.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, EditPasswordActivity.class);
                startActivity(intent);
            }
        });

        userInfoViewModel.getProfileImageChangeResult().observe(EditProfileActivity.this,result -> {
            if(result.result){

                if(result.response.has("profileImageUrl")){
                    String profile_url = result.response.get("profileImageUrl").getAsString();
                    // Glide를 사용하여 인터넷에서 이미지를 로드하고 ImageView에 표시합니다.
                    Glide.with(this)
                            .load(application.getBaseURL()+profile_url)
//                            .dontAnimate() // 애니메이션 비활성화
                            .placeholder(프로필이미지뷰.getDrawable()) // 현재 이미지 뷰에 있는 드로어블을 placeholder로 사용
                            .into(프로필이미지뷰);
                    프로필이미지삭제.setVisibility(View.VISIBLE);
                    application.setChangeProfileImageResult(profile_url);
                }
                else{
                    프로필이미지뷰.setImageDrawable(getDrawable(R.drawable.person));
                    application.setChangeProfileImageResult(null);
                }

            }
            else{
//                Toast.makeText(application, "데이터 불러오기 실패." +result.error_reason, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(연락처정보 != null){
            연락처정보텍스트뷰.setText(연락처정보);
            연락처정보 = null;
        }

    }

    private Bitmap getBitmapFromUri(Uri uri) {
        Bitmap bitmap = null;
        try {
            InputStream imageStream = getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(imageStream);
            imageStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
//        YOUR_REQUEST_CODE

        Log.i("onActivityResult", Integer.toString(requestCode));
        Log.i("onActivityResult", Integer.toString(resultCode));
        Log.i("onActivityResult", Integer.toString(RESULT_OK));
//        Log.i("onActivityResult",Integer.toString());requestCode == 100 &&

        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

//            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
            Bitmap bitmap = getBitmapFromUri(selectedImageUri);
            프로필이미지뷰.setImageBitmap(bitmap);
            프로필이미지삭제.setVisibility(View.VISIBLE);
            byte[] 이미지 = convertBitmapToByteArray(bitmap);

            MultipartBody.Part imageTypeBodyData = null;
            if (이미지 != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), 이미지);
                imageTypeBodyData = MultipartBody.Part.createFormData("imgFile", "imgFile", requestFile);
                userInfoViewModel.setProfile(imageTypeBodyData);
            }
            프로필이미지삭제.setVisibility(View.VISIBLE);
            Log.i("", " 프로필 편집 요청.");

        } else if (requestCode == 200 && resultCode == RESULT_OK) {

            Log.i("EditRecordActivity", "REQUEST_IMAGE_CAPTURE");

            if (data == null) {
                Log.i("EditRecordActivity", "(data == null)");

            } else if (data.getData() == null) {
                Log.i("EditRecordActivity", "(data.getData == null)");

            }

            Log.i("EditRecordActivity", 임시경로);
            File imgFile = new File(임시경로);
            if (imgFile.exists()) {
                Log.i("EditRecordActivity", "임시경로.exists()");
//                imageView.setImageURI(Uri.fromFile(imgFile));
            } else {

                Log.i("EditRecordActivity", "임시경로 not exists()");

            }

            String 경로 = imgFile.getAbsolutePath();
            프로필이미지뷰.setImageURI(Uri.parse(경로));

//            Bitmap bitmap = getBitmapFromUri(Uri.parse(경로));
//            프로필이미지뷰.setImageBitmap(bitmap);
//            프로필이미지삭제.setVisibility(View.VISIBLE);
//            byte[] 이미지 = convertBitmapToByteArray(bitmap);

            MultipartBody.Part imageTypeBodyData = null;
            if (imgFile != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imgFile);
                imageTypeBodyData = MultipartBody.Part.createFormData("imgFile", "imgFile", requestFile);
                userInfoViewModel.setProfile(imageTypeBodyData);
            }
            프로필이미지삭제.setVisibility(View.VISIBLE);

        }
    }

    private File createFIle() {
        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

//        String fileName = "profileSample";
        File imageDir = getApplicationContext().getDir("profileImages", MODE_PRIVATE);

        return new File(imageDir, fileName+".png");
    }

    public void 갤러리(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
//                startActivityForResult(intent, YOUR_REQUEST_CODE);
    }
    String 임시경로;
    public void 카메라(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = createFIle();
            Uri providerFileUri2 = Uri.fromFile(photoFile);

            임시경로 = photoFile.getAbsolutePath();
            Log.i("uri : ",providerFileUri2.toString());

            File imgFile = new File(임시경로);
            Uri providerFileUri = FileProvider.getUriForFile(this,
                    "com.example.project04_240225.fileprovider",
                    photoFile);
//                    Log.i("사진 저장 uri : ",providerFileUri.toString());
//                    takePictureIntent.putExtra("임시경로",임시경로);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFileUri);
            startActivityForResult(takePictureIntent, 200);
////                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }
//    interface KakaoAPI {
//        @GET("oauth/logout")
//        Call<Void> logout(
//                @Query("client_id") String clientId,
//                @Query("logout_redirect_uri") String logoutRedirectUri);
//    }
//    private static final String BASE_URL = "https://kauth.kakao.com/";
//
//
//    public void main() {
//        // OkHttpClient 인스턴스 생성 (필요한 경우 설정을 추가할 수 있음)
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//
//        // Retrofit 인스턴스 생성
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        // KakaoAPI 인터페이스의 구현 생성
//        KakaoAPI kakaoAPI = retrofit.create(KakaoAPI.class);
//
//
//
//        // 로그아웃 API 호출을 위한 client_id와 logout_redirect_uri 설정
//        String clientId = "170d84240324d6db8825883836afb314";
//        String logoutRedirectUri = "https://test123456.com/logout";
//
////        AuthApiClient.getInstance().getTokenManagerProvider().getManager().getToken().
//
//        // 로그아웃 API 호출
//        Call<Void> call = kakaoAPI.logout(clientId, logoutRedirectUri);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    System.out.println("Logout successful");
//                } else {
////                    System.out.println("Logout failed: " + response.);
//                    System.out.println("Logout failed: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//
//    }


}

