package com.example.project04_240225;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppSignature {
    private static final String LOG = "YourLogTag";
    private static final String HASH_TYPE = "SHA-256"; // or whatever hash type you're using
    private static final int NUM_HASHED_BYTES = 9; // Adjust according to your needs
    private static final int NUM_BASE64_CHAR = 11; // Adjust according to your needs

    public List<String> getAppSignatures(Context context) {
        List<String> appCodes = new ArrayList<>();

        try {
            String packageName = context.getPackageName();
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;

            for (Signature signature : signatures) {
                String hash = getHash(packageName, signature.toCharsString());

                if (hash != null) {
                    appCodes.add(String.format("%s", hash));
                }

                Log.d(LOG, String.format("Attach this value to your SMS: %s", hash));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(LOG, "Unable to find package to obtain hash. : " + e);
        }

        Log.i("",appCodes.toString());

        return appCodes;
    }

    private String getHash(String packageName, String signature) {
        String appInfo = packageName + " " + signature;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_TYPE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                messageDigest.update(appInfo.getBytes(StandardCharsets.UTF_8));
            }

            byte[] hashSignature = Arrays.copyOfRange(messageDigest.digest(), 0, NUM_HASHED_BYTES);
            String base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING | Base64.NO_WRAP)
                    .substring(0, NUM_BASE64_CHAR);

            Log.d(LOG, String.format("\nPackage : %s\nHash : %s", packageName, base64Hash));

            return base64Hash;

        } catch (NoSuchAlgorithmException e) {
            Log.d(LOG, "hash:NoSuchAlgorithm : " + e);
        }

        return null;
    }
}