package com.example.project04_240225.ViewModel;

import android.graphics.Color;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.project04_240225.Model.UserInfo;
import com.example.project04_240225.Model.UserRepository;
import com.example.project04_240225.Network.DTO.Data;
import com.example.project04_240225.Network.DTO.Error;
import com.example.project04_240225.Network.DTO.UserInfoData;
import com.example.project04_240225.Result;
import com.example.project04_240225.Utility.ValidationUtil;
import com.example.project04_240225.View.SignUpActivity;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;

public class UserInfoViewModel {

    UserInfo userInfo;
    ValidationUtil validationUtil;

    private MutableLiveData<Data> logoutResult = new MutableLiveData<>();
    public MutableLiveData<Data> getLogoutResult() {
        return logoutResult;
    }


    private MutableLiveData<Data> profileImageChangeResult = new MutableLiveData<>();
    public MutableLiveData<Data> getProfileImageChangeResult() {
        return profileImageChangeResult;
    }

    public UserInfoViewModel(UserInfo userInfo,ValidationUtil validationUtil){
        this.userInfo = userInfo;
        this.validationUtil = validationUtil;
    }

    private MutableLiveData<Data> infoResult = new MutableLiveData<>();

    public MutableLiveData<Data> getInfo() {
        return infoResult;
    }

    private MutableLiveData<UserInfoData> userInfoResult = new MutableLiveData<>();

    public MutableLiveData<UserInfoData> getUserInfoResult() {
        return userInfoResult;
    }

    private MutableLiveData<Error> errorLiveData = new MutableLiveData<>();

    public MutableLiveData<Error> getErrorLiveData() {
        return errorLiveData;
    }



    private MutableLiveData<Data> setNameResult = new MutableLiveData<>();

    //    public MutableLiveData<Data> getLogoutResult() {
//        return logoutResult;
//    }
    public MutableLiveData<Data> getSetNameResult() {
        return setNameResult;
    }

    private MutableLiveData<Result> nicknameValidationResult = new MutableLiveData<>();
    public MutableLiveData<Result> getNicknameValidationResult() {
        return nicknameValidationResult;
    }
    private MutableLiveData<Result> 연락처Result = new MutableLiveData<>();
    public MutableLiveData<Result> get연락처Result() {
        return 연락처Result;
    }

    private MutableLiveData<Data> setPwResult = new MutableLiveData<>();

    public MutableLiveData<Data> getSetPwResult() {
        return setPwResult;
    }

    public void 비밀번호재설정(String 현재비밀번호,String 새비밀번호,String 자동입력방지문자){

        userInfo.비밀번호재설정(현재비밀번호,새비밀번호, 자동입력방지문자).observeForever(result -> {

//            logoutResult.setValue(result);
            setPwResult.setValue(result);

        });


    }


    public void 회원탈퇴요청(){


        userInfo.회원탈퇴요청().observeForever(result -> {

//            logoutResult.setValue(result);
//            setPwResult.setValue(result);

        });



    }


    String AUTHTYPE_EMAIL = "email", AUTHTYPE_PHONE = "phone";
    public void 연락처정보변경요청(int auth_type,String 인증데이터,String 인증번호){

        String authType;

        if(auth_type == SignUpActivity.EMAIL){

            authType = AUTHTYPE_EMAIL;
        }
        else if(auth_type == SignUpActivity.PHONE){

            authType = AUTHTYPE_PHONE;
        }
        else{
            return;
        }

        Log.i("test",authType);
        Log.i("test",인증데이터);
        Log.i("test",인증번호);

        userInfo.연락처정보변경요청(authType,인증데이터, 인증번호).observeForever(result -> {

//            logoutResult.setValue(result);
//            setPwResult.setValue(result);

            boolean status2;

            String status = result.response.get("result").getAsString();
            if("success".equals(status)){
                status2=true;
            }
            else{
                status2 =false;
            }

            String message = result.response.get("auth_data").getAsString();

            Result result1 = new Result(status2,message,Color.TRANSPARENT);
            연락처Result.setValue(result1);
            
            

        });


    }

    public void 로그아웃(){

        userInfo.logoutRequest().observeForever(result -> {
            logoutResult.setValue(result);
        });

    }

    public void getUserInfo(){

        userInfo.getInfo().observeForever(result -> {

            infoResult.setValue(result);
        });

    }

    public void getUserInfodata(){

        userInfo.getInfo().observeForever(result -> {

            if(result.result){

                if(result.response.has("result")){
                    String status = result.response.get("result").getAsString();

                    if("success".equals(status)){
                        UserInfoData infoData = new UserInfoData();

                        JsonObject userInfo = result.response.get("data").getAsJsonObject();
                        String name = userInfo.get("name").getAsString();
                        infoData.setName(name);

                        if(userInfo.has("profile")) {
                            String profile = userInfo.get("profile").getAsString();
                            infoData.setProfileImageUrl(profile);

                        }

                        if(userInfo.has("auth_data") && userInfo.get("auth_data") != JsonNull.INSTANCE) {
//                            userInfo.get("auth_data").getAsJsonNull()
                            String authData = userInfo.get("auth_data").getAsString();
                            infoData.setAuthData(authData);
                        }

                        userInfoResult.setValue(infoData);
                    }
                    else{
                        Log.i("getUserInfodata() response.toString",result.response.toString());
                        userInfoResult.setValue(null);
                    }
                }
                else{
                    Log.i("getUserInfodata() response.toString",result.response.toString());
                    userInfoResult.setValue(null);
                }

            }
            else{

                Log.i("getUserInfodata() error_reason",result.error_reason);
                userInfoResult.setValue(null);
//                Error error = new Error();
//                error.setMessage(result.error_reason);
////                error.setCode(result.error_code);
////                "error_code" => "400",
////                        "error_description" => "request data Json decode fail."
//                errorLiveData.setValue(error);

            }


        });

    }



    public void deleteProfileImage(){

        userInfo.deleteProfileImage().observeForever(result -> {


        });

    }




    public void setProfile(MultipartBody.Part imageTypeBodyData){

        userInfo.uploadImage("UploadImage", imageTypeBodyData).observeForever(result -> {

//            result.result

//            result.response

            profileImageChangeResult.setValue(result);



//            profileImageU
//            rl

//            Log.i()
//            result.result
    //            infoResult.setValue(result);

        });

    }

    public void 닉네임변경(String nicknameValue){
        userInfo.setNickname(nicknameValue).observeForever(result -> {

            setNameResult.setValue(result);
//            Log.i()
//            result.result
            //            infoResult.setValue(result);

        });
    }


    public boolean 닉네임유효성검사(String nicknameValue){

//        Result result = new Result(false,"닉네임을 입력해주세요", Color.RED);
//        return result;
        if(nicknameValue.length() == 0){
            nicknameValidationResult.setValue(new Result(false,"닉네임: 필수 정보입니다.", Color.RED));
            return false;

        }
        else{
            boolean 형식검사결과 = validationUtil.name_validate(nicknameValue);

            if(!형식검사결과){
                nicknameValidationResult.setValue(new Result(false,"닉네임: 한글, 영문 대/소문자를 사용해 주세요. (특수기호, 공백 사용 불가)", Color.RED));
                return false;

            }
            else{
                nicknameValidationResult.setValue(new Result(true,"", Color.TRANSPARENT));
                return true;

                //  result = new Result(true,"", Color.TRANSPARENT);

            }
        }

    }

    public MutableLiveData<Result> getImageResult() {
        return ImageResult;
    }
    private MutableLiveData<Result> ImageResult = new MutableLiveData<>();
    public void 이미지가져오기(){


        userInfo.Request().observeForever(result -> {
            ImageResult.setValue(result);
        });

    }



}
