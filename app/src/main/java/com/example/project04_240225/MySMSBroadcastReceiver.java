package com.example.project04_240225;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.project04_240225.View.SignUpActivity;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BroadcastReceiver to wait for SMS messages. This can be registered either
 * in the AndroidManifest or at runtime.  Should filter Intents on
 * SmsRetriever.SMS_RETRIEVED_ACTION.
 */
public class MySMSBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch(status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Log.i("message",message);
//                    String patternString = "\\[(.*?)\\]";
                    Pattern pattern = Pattern.compile("\\[(\\d+)\\]");
//                    Pattern pattern = Pattern.compile(patternString);
                    Matcher matcher = pattern.matcher(message);

                    while (matcher.find()) {
                        // find() 메소드를 호출할 때마다 대괄호([]) 내의 다음 값을 찾습니다.
                        // matcher.group(1)은 첫 번째 괄호에 해당하는 부분, 즉 대괄호 안의 값을 반환합니다.
                        System.out.println("대괄호 안의 값: " + matcher.group(1));
                        Log.i("message",matcher.group(1));
                        // 새 엑티비티에 코드 전달

                        // LocalBroadcastManager를 사용하여 새 엑티비티에 코드 전달
                        Intent messageIntent = new Intent("CustomAction");
                        messageIntent.putExtra("EXTRA_CODE", matcher.group(1));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(messageIntent);
                    }
//                    Android is always a sweet treat!wYnnrAzzEbI
                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server.
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    break;
            }
        }
    }
}