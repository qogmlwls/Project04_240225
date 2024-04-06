package com.example.project04_240225.ViewModel;

import android.graphics.Color;

import androidx.lifecycle.MutableLiveData;

import com.example.project04_240225.Model.UserRepository;
import com.example.project04_240225.Result;

public class ViewModel {


    UserRepository userRepository;

    public ViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 각 유효성 검사 결과를 위한 MutableLiveData 객체
    private MutableLiveData<Result> logoutResult = new MutableLiveData<>();
    private MutableLiveData<String> nameResult = new MutableLiveData<>();

    public MutableLiveData<Result> getLogoutResult() {
        return logoutResult;
    }
    public MutableLiveData<String> getNameResult() {
        return nameResult;
    }

    public void 로그아웃(){

        userRepository.logoutRequest().observeForever(result -> {
            logoutResult.setValue(result);
        });

    }

    public void 닉네임가져오기(){

        userRepository.nameRequest().observeForever(result -> {
            nameResult.setValue(result);
        });

    }


}
