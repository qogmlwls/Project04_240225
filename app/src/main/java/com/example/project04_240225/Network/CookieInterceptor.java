package com.example.project04_240225.Network;

import android.content.Context;

import com.example.project04_240225.Model.UserRepository;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;

public  class CookieInterceptor implements Interceptor {

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
