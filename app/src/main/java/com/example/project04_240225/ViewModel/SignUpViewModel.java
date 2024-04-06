package com.example.project04_240225.ViewModel;

import android.graphics.Color;

import androidx.lifecycle.MutableLiveData;

import com.example.project04_240225.View.SignUpActivity;
import com.example.project04_240225.Result;
import com.example.project04_240225.Model.UserRepository;
import com.example.project04_240225.Utility.ValidationUtil;

public class SignUpViewModel {

//    역할: 뷰(View)와 모델(Model) 사이의 상호작용을 관리하는 뷰모델(ViewModel)입니다. 데이터의 상태와 비즈니스 로직을 처리하여 뷰에 데이터를 제공합니다.
//    기능: 사용자 입력을 검증하고, UserRepository를 통해 회원가입 요청을 수행합니다. 데이터 상태 변화를 관찰하고, UI 업데이트를 위해 변화를 액티비티에 알립니다.
 //   SignUpViewModel의 주요 책임은 UI로부터 사용자 입력을 받아 이를 검증하고, 처리 결과를 UI로 다시 전달하는 것입니다.
//

    ValidationUtil validationUtil;
    UserRepository userRepository;

    public SignUpViewModel(UserRepository userRepository) {
        validationUtil = new ValidationUtil();
        this.userRepository = userRepository;
    }

    // 각 유효성 검사 결과를 위한 MutableLiveData 객체
    private MutableLiveData<Result> userIdValidationResult = new MutableLiveData<>();
    private MutableLiveData<Result> passwordValidationResult = new MutableLiveData<>();
    private MutableLiveData<Result> nicknameValidationResult = new MutableLiveData<>();
    private MutableLiveData<Result> verificationCodeValidationResult = new MutableLiveData<>();


    private MutableLiveData<Result> verificationCodeSendResult = new MutableLiveData<>();


    // 인증 코드 확인을 위한 MutableLiveData 객체 추가
    private MutableLiveData<Result> verificationCodeCheckResult = new MutableLiveData<>();
    private MutableLiveData<Result> signUpResult = new MutableLiveData<>();

    // Getters
    public MutableLiveData<Result> getUserIdValidationResult() {
        return userIdValidationResult;
    }

    public MutableLiveData<Result> getPasswordValidationResult() {
        return passwordValidationResult;
    }

    public MutableLiveData<Result> getNicknameValidationResult() {
        return nicknameValidationResult;
    }

    public MutableLiveData<Result> getVerificationCodeValidationResult() {
        return verificationCodeValidationResult;
    }
    public MutableLiveData<Result> getVerificationCodeSendResult() {
        return verificationCodeSendResult;
    }


    public MutableLiveData<Result> getVerificationCodeCheckResult() {
        return verificationCodeCheckResult;
    }
    public MutableLiveData<Result> getSignUpResult() {
        return signUpResult;
    }

    public void 아이디유효성검사(String id){

        if(아이디형식검사(id)){
            userRepository.isUserIdAvailable(id).observeForever(result -> {
                userIdValidationResult.setValue(result);
            });
        }

    }

    public boolean 인증코드형식검사(String data){

        if(data.length() == 0){
            verificationCodeCheckResult.setValue(new Result(false,"인증이 필요합니다.", Color.RED));
            // result = new Result(false,"아이디: 필수 정보입니다.", Color.RED);
            return false;
        }
        else{
            // 인증번호를 정확하게 다시 입력해 주세요.
            // 인증 완료.
//            verificationCodeCheckResult.setValue(new Result(true,"", Color.BLUE));
            return true;
        }
    }
    public void 인증코드유효성검사(int type,String data,String value){

        if(인증코드형식검사(value)){

            userRepository.checkAuthenticationRequest(type,data,value).observeForever(result -> {
                verificationCodeCheckResult.setValue(result);
            });


//            verificationCodeCheckResult.setValue(new Result(true,"", Color.BLUE));
            // 인증번호를 정확하게 다시 입력해 주세요.
            // 인증 완료.
        }

    }
    private boolean 아이디형식검사(String id){

        if(id.length() == 0){
            userIdValidationResult.setValue(new Result(false,"아이디: 필수 정보입니다.", Color.RED));
            // result = new Result(false,"아이디: 필수 정보입니다.", Color.RED);
            return false;
        }
        else {
            boolean 형식검사결과 = validationUtil.id_validate(id);

            if (!형식검사결과) {
                userIdValidationResult.setValue(new Result(false, "아이디: 6~12자의 영문 대/소문자, 숫자만 사용 가능합니다.", Color.RED));

                //아이디: 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.
                //     result = new Result(false,"아이디: 6~12자의 영문 대/소문자, 숫자만 사용 가능합니다.", Color.RED);
                return false;
            } else {

                return true;
            }
        }
    }

    public boolean 비밀번호유효성검사(String passwordValue){

        if(passwordValue.length() == 0){
            passwordValidationResult.setValue(new Result(false,"비밀번호: 필수 정보입니다.", Color.RED));
            return false;

        }
        else{
            boolean 형식검사결과 = validationUtil.pw_validate(passwordValue);

            if(!형식검사결과){
                passwordValidationResult.setValue(new Result(false,"비밀번호: 8~16자의 영문 대/소문자, 숫자, 특수문자만 사용 가능합니다.", Color.RED));
                return false;

            }
            else{
                passwordValidationResult.setValue(new Result(true,"", Color.TRANSPARENT));
                return true;

            }
        }
    }

    public void 인증종류데이터인증코드발송요청(int type, String value){

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
        userRepository.sendAuthenticationRequest(auth_type,value).observeForever(result -> {
            verificationCodeSendResult.setValue(result);
        });

    }
    public void 인증종류데이터인증코드발송요청(int type, String value,String code){

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
        userRepository.sendAuthenticationRequest(auth_type,value,code ).observeForever(result -> {
            verificationCodeSendResult.setValue(result);
        });

    }

    public void 회원가입요청(String user_id, String user_pw, String username, int auth_type, String auth_data, String code){

        // 유효성검사
            // value 가 0인지
        //

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


        userRepository.signUpRequest(user_id,user_pw,username,authType,auth_data,code).observeForever(result -> {
            signUpResult.setValue(result);
        });

//        if(value.length() == 0){
//            verificationCodeCheckResult.setValue(new Result(false,"인증: 인증코드를 입력해주세요.", Color.RED));
//        }
//        else{
//
//            userRepository.checkAuthenticationRequest(type,data,value).observeForever(result -> {
//                verificationCodeCheckResult.setValue(result);
//            });
//
//        }

    }
    public void 인증코드확인요청(int type,String data,String value){

        if(value.length() == 0){
            verificationCodeCheckResult.setValue(new Result(false,"인증: 인증코드를 입력해주세요.", Color.RED));
        }
        else{

            userRepository.checkAuthenticationRequest(type,data,value).observeForever(result -> {
                verificationCodeCheckResult.setValue(result);
            });

        }

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
//        Result result = null;
//
//        if(nicknameValue.length() == 0){
//            result = new Result(false,"닉네임: 필수 정보입니다.", Color.RED);
//        }
//        else{
//            boolean 형식검사결과 = validationUtil.name_validate(nicknameValue);
//
//            if(!형식검사결과){
//                //아이디: 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.
//                result = new Result(false,"닉네임: 한글, 영문 대/소문자를 사용해 주세요. (특수기호, 공백 사용 불가)", Color.RED);
//
//            }
//            else{
//
//                // 중복검사
//                result = new Result(true,"", Color.TRANSPARENT);
//
//                // 아이디: 사용할 수 없는 아이디입니다. 다른 아이디를 입력해 주세요.
////                result = new Result(false,"아이디: 사용할 수 없는 아이디입니다. 다른 아이디를 입력해 주세요.", Color.RED);
//
//            }
//        }
//        return result;

    }

    public boolean 데이터형식검사(String 아이디, String 비밀번호, String 닉네임, int type, String authData) {

        boolean 아이디형식검사결과 = 아이디형식검사(아이디);
        boolean 비밀번호형식검사 = 비밀번호유효성검사(비밀번호);
        boolean 닉네임형식검사 = 닉네임유효성검사(닉네임);
        boolean 인증데이터형식검사 = 인증데이터형식검사(type,authData);

        return 아이디형식검사결과&&비밀번호형식검사&&닉네임형식검사&&인증데이터형식검사;

    }

    private boolean 인증데이터형식검사(int type,String authData){

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
                return true;
            }
        }
    }

    String AUTHTYPE_EMAIL = "email", AUTHTYPE_PHONE = "phone";
    public void 인증데이터유효성검사(int type,String value){


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

        if(인증데이터형식검사(type,value)){
            userRepository.isAuthenticationAvailable(auth_type,value).observeForever(result -> {
                verificationCodeValidationResult.setValue(result);
            });
        }

    }


}
