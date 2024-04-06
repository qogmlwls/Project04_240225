package com.example.project04_240225.ViewModel;

import android.graphics.Color;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.project04_240225.Result;
import com.example.project04_240225.Model.UserRepository;

public class LoginViewModel {

    UserRepository userRepository;

    public LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 각 유효성 검사 결과를 위한 MutableLiveData 객체
    private MutableLiveData<Result> loginResult = new MutableLiveData<>();
    private MutableLiveData<Result> checkLoginResult = new MutableLiveData<>();


    public MutableLiveData<Result> getLoginResult() {
        return loginResult;
    }
    public MutableLiveData<Result> getCheckLoginResult() {
        return checkLoginResult;
    }



    private MutableLiveData<Result> checkSNSLoginResult = new MutableLiveData<>();
    public MutableLiveData<Result> getCheckSNSLoginResult() {
        return checkSNSLoginResult;
    }

    private MutableLiveData<Result> SNSJoinResult = new MutableLiveData<>();
    public MutableLiveData<Result> getSNSJoinResult() {
        return SNSJoinResult;
    }

    public void 로그인(String id, String pw){

        if(id.length() == 0){
            loginResult.setValue(new Result(false,"아이디를 입력해주세요.", Color.RED));
        }
        else if(pw.length() == 0){
            loginResult.setValue(new Result(false,"비밀번호를 입력해주세요.", Color.RED));
        }
        else{

            userRepository.loginRequest(id,pw).observeForever(result -> {
                loginResult.setValue(result);
            });

        }

    }

    public void CheckLogin(){

        userRepository.checkLoginRequest().observeForever(result -> {
            checkLoginResult.setValue(result);
        });

    }


    public void CheckSNSLogin(String snsType, String 회원번호){

        Log.i("CheckSNSLogin",snsType);
        Log.i("CheckSNSLogin",회원번호);

        userRepository.checkSNSLoginRequest(snsType,회원번호).observeForever(result -> {
            Log.i("","checkSNSLoginRequest");

            result.setMessage(snsType);
            checkSNSLoginResult.setValue(result);
//            Log.i("CheckSNSLogin","");

        });

    }


    public void RequestSNSJoin(String 닉네임, String  가입종류, String 회원번호){

        Log.i("RequestSNSJoin",회원번호);
        Log.i("RequestSNSJoin",닉네임);
        Log.i("RequestSNSJoin",가입종류);

        userRepository.RequestSNSJoinRequest(회원번호,가입종류,닉네임).observeForever(result -> {
            Log.i("","RequestSNSJoinRequest");
//            result.setMessage(snsType);
            SNSJoinResult.setValue(result);
//            Log.i("CheckSNSLogin","");
        });

    }

}
