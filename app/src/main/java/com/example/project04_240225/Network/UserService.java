package com.example.project04_240225.Network;

import com.example.project04_240225.Model.UserRepository;
import com.example.project04_240225.Network.DTO.Data;
import com.example.project04_240225.Network.DTO.ResponseData;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {
//      @GET("project04/public/index.php?")
//        Call<Boolean> checkUserIdAvailability(@Query("action") String action,@Query("userId") String userId);
    //     @POST("index.php") ///{userId}
    ///{userId}/index.php?action=checkUsername  action={action}//@Path("userId") String userId
//        @POST("project04/src/Controller/LoginController.php") ///{userId}/index.php?action=checkUsername  action={action}
//            //     @POST("index.php") ///{userId}            //     @POST("index.php") ///{userId}
//        Call<String> requestPOST(@Body JsonObject userId);//@Path("userId") String userId

    @Multipart
    @POST("project04/src/Controller/{path}.php")  // 이 부분에 서버의 PHP 엔드포인트 URL을 입력합니다.
    Call<ResponseData> uploadImage(
            @Path("path") String path,
            @Part MultipartBody.Part imgFile
    );



    @POST("project04/src/Controller/{path}.php") ///{userId}/index.php?action=checkUsername  action={action}
    Call<ResponseData> requestPOST(@Path("path") String path, @Body JsonObject body);//@Path("userId") String userId


    @POST("project04/src/Controller/{path}.php") ///{userId}/index.php?action=checkUsername  action={action}
    Call<ResponseData> requestPOST(@Path("path") String path);//@Path("userId") String userId

//    @POST("project04/src/Controller/{path}.php") ///{userId}/index.php?action=checkUsername  action={action}
//    Call<ResponseData> requestPOST3(@Path("path") String path, @Body JsonObject userId);//@Path("userId") String userId


    @POST("project04/src/Controller/{path}.php") ///{userId}/index.php?action=checkUsername  action={action}
    Call<ResponseData> requestGET(@Path("path") String path);//@Path("userId") String userId



    @POST("project04/src/Controller/{path}.php") ///{userId}/index.php?action=checkUsername  action={action}
    Call<ResponseBody> requestPOST2(@Path("path") String path, @Body JsonObject userId);//@Path("userId") String userId

}