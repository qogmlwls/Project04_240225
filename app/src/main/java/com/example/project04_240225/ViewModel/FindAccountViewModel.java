package com.example.project04_240225.ViewModel;

import android.graphics.Color;

import androidx.lifecycle.MutableLiveData;

import com.example.project04_240225.Model.UserRepository;
import com.example.project04_240225.Result;
import com.example.project04_240225.Utility.ValidationUtil;
import com.example.project04_240225.View.SignUpActivity;

public class FindAccountViewModel {

    ValidationUtil validationUtil;
    UserRepository userRepository;
    String AUTHTYPE_EMAIL = "email", AUTHTYPE_PHONE = "phone";

    public FindAccountViewModel(UserRepository userRepository) {
        validationUtil = new ValidationUtil();
        this.userRepository = userRepository;
    }

    private MutableLiveData<Result> verificationCodeValidationResult = new MutableLiveData<>();

    private MutableLiveData<Result> idCheckResult = new MutableLiveData<>();

    private MutableLiveData<Result> verificationCodeSendResult = new MutableLiveData<>();


    // 인증 코드 확인을 위한 MutableLiveData 객체 추가
    private MutableLiveData<Result> verificationCodeCheckResult = new MutableLiveData<>();
    private MutableLiveData<Result> findAccountIdResult = new MutableLiveData<>();

    private MutableLiveData<Result> resetAccountPwResult = new MutableLiveData<>();
    private MutableLiveData<Result> ImageResult = new MutableLiveData<>();

    public MutableLiveData<Result> getVerificationCodeValidationResult() {
        return verificationCodeValidationResult;
    }
    public MutableLiveData<Result> getVerificationCodeSendResult() {
        return verificationCodeSendResult;
    }
    public MutableLiveData<Result> getIdCheckResult() {
        return idCheckResult;
    }


    public MutableLiveData<Result> getVerificationCodeCheckResult() {
        return verificationCodeCheckResult;
    }
    public MutableLiveData<Result> getFindAccountIdResultResult() {
        return findAccountIdResult;
    }

    public MutableLiveData<Result> getResetAccountPwResultResult() {
        return resetAccountPwResult;
    }
    public MutableLiveData<Result> getImageResult() {
        return ImageResult;
    }

    public void 이미지가져오기(){


        userRepository.Request().observeForever(result -> {
            ImageResult.setValue(result);
        });

    }



    public void 비밀번호재설정(String user_id, String user_pw, int auth_type, String auth_data, String auth_code, String auth_captchaCode){

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

        userRepository.resetPwRequest(user_id, user_pw,authType,auth_data,auth_code,auth_captchaCode).observeForever(result -> {
            resetAccountPwResult.setValue(result);
        });

    }


    public void 아이디유효성검사(String value){


        if(value.length() == 0){
            idCheckResult.setValue(new Result(false,"아이디를 입력해 주세요", Color.RED));
        }
        else{
            userRepository.isUserIdAvailable(value,userRepository.존재하는지).observeForever(result -> {
                idCheckResult.setValue(result);
            });
        }

    }
    public void 인증데이터유효성검사(int type,String value){

        인증데이터형식검사(type,value);

    }

    public boolean 인증데이터형식검사(int type,String authData){

        String 필수정보요청메세지 = null,잘못된데이터알림메세지 = null;

        if(type == SignUpActivity.EMAIL){
            필수정보요청메세지 = "이메일: 필수 정보입니다.";
            잘못된데이터알림메세지 = "이메일: 이메일이 정확한지 확인해 주세요.";
//            중복알림메세지 = "이메일: 이미 가입된 이메일 주소입니다.";
        }
        else if(type == SignUpActivity.PHONE){
            필수정보요청메세지 = "휴대전화번호: 필수 정보입니다.";
            잘못된데이터알림메세지 = "휴대전화번호: 휴대전화번호가 정확한지 확인해 주세요.";
//            중복알림메세지 = "휴대전화번호: 이미 가입된 휴대전화번호입니다.";
        }
        else{
            return false;
        }


        if(authData.length() == 0){
            verificationCodeValidationResult.setValue(new Result(false,필수정보요청메세지, Color.RED));
            return false;
        }
        else{
            boolean 형식검사결과 = validationUtil.authData_validate(type,authData);
            if(!형식검사결과){
                verificationCodeValidationResult.setValue(new Result(false,잘못된데이터알림메세지, Color.RED));
                return false;
            }
            else{
                verificationCodeValidationResult.setValue(new Result(true,"", Color.TRANSPARENT));
                return true;
            }
        }
    }

    public void 인증번호발송요청(int type, String value,String user_id){

        String auth_type;

        if(type == SignUpActivity.EMAIL){

            auth_type = AUTHTYPE_EMAIL;
        }
        else if(type == SignUpActivity.PHONE){

            auth_type = AUTHTYPE_PHONE;
        }
        else{
            return;
        }
        userRepository.sendAuthenticationRequest(auth_type,value,userRepository.존재하는지,user_id).observeForever(result -> {
            verificationCodeSendResult.setValue(result);
        });

    }

    public void 인증번호발송요청(int tyype2,int type, String value,String user_id){

        String auth_type;

        if(type == SignUpActivity.EMAIL){

            auth_type = AUTHTYPE_EMAIL;
        }
        else if(type == SignUpActivity.PHONE){

            auth_type = AUTHTYPE_PHONE;
        }
        else{
            return;
        }

//        userRepository.ㅈ
        userRepository.sendAuthenticationRequest(auth_type,value,UserRepository.중복인지,user_id).observeForever(result -> {
            verificationCodeSendResult.setValue(result);
        });

    }

    public void 인증번호발송요청(int type, String value,String code,String user_id){

        String auth_type;

        if(type == SignUpActivity.EMAIL){

            auth_type = AUTHTYPE_EMAIL;
        }
        else if(type == SignUpActivity.PHONE){

            auth_type = AUTHTYPE_PHONE;
        }
        else{
            return;
        }
        userRepository.sendAuthenticationRequest(auth_type,value,code,userRepository.존재하는지,user_id).observeForever(result -> {
            verificationCodeSendResult.setValue(result);
        });

    }
    public void 인증번호발송요청(int type2,int type, String value,String code,String user_id){

        String auth_type;

        if(type == SignUpActivity.EMAIL){

            auth_type = AUTHTYPE_EMAIL;
        }
        else if(type == SignUpActivity.PHONE){

            auth_type = AUTHTYPE_PHONE;
        }
        else{
            return;
        }
        userRepository.sendAuthenticationRequest(auth_type,value,code,UserRepository.중복인지,user_id).observeForever(result -> {
            verificationCodeSendResult.setValue(result);
        });

    }
    public void 인증번호유효성검사(int type,String data,String value){

        if(인증번호형식검사(value)){

            userRepository.checkAuthenticationRequest(type,data,value).observeForever(result -> {
                verificationCodeCheckResult.setValue(result);
            });


//            verificationCodeCheckResult.setValue(new Result(true,"", Color.BLUE));
            // 인증번호를 정확하게 다시 입력해 주세요.
            // 인증 완료.
        }

    }

    public boolean 인증번호형식검사(String data){

        if(data.length() == 0){
            verificationCodeCheckResult.setValue(new Result(false,"인증이 필요합니다.", Color.RED));
            // result = new Result(false,"아이디: 필수 정보입니다.", Color.RED);
            return false;
        }
        else{
            // 인증번호를 정확하게 다시 입력해 주세요.
            // 인증 완료.
            verificationCodeCheckResult.setValue(new Result(true,"", Color.TRANSPARENT));
            return true;
        }
    }


    public void 아이디찾기요청( int authType, String data, String code ){

        if(인증데이터형식검사(authType,data)&&인증번호형식검사(code)){
            // 서버에 요청

            String auth_type;

            if(authType == SignUpActivity.EMAIL){

                auth_type = AUTHTYPE_EMAIL;
            }
            else if(authType == SignUpActivity.PHONE){

                auth_type = AUTHTYPE_PHONE;
            }
            else{
                return;
            }

            userRepository.findAccountIdRequest(auth_type,data,code).observeForever(result -> {
                findAccountIdResult.setValue(result);
            });

        }


    }

}
