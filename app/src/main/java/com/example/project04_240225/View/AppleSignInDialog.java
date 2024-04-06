package com.example.project04_240225.View;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.project04_240225.Utility.Const;

import java.util.UUID;

public class AppleSignInDialog extends Dialog {
    private WebView webView;
    private Interaction interaction;

    public AppleSignInDialog(Context context, Interaction interaction) {
        super(context);
        this.interaction = interaction;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(getContext());
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new AppleWebViewClient());
        webView.loadUrl(Const.APPLE_AUTHURL
                + "?response_type=code&v=1.1.6&response_mode=form_post&client_id="
                + Const.APPLE_CLIENT_ID + "&scope="
                + Const.APPLE_SCOPE + "&state="
                + UUID.randomUUID().toString() + "&redirect_uri="
                + Const.APPLE_REDIRECT_URI
        );
        setContentView(webView);
    }

    private class AppleWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        // For API 19 and below
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url != null) {
                Uri uri = Uri.parse(url);
                String status = uri.getQueryParameter("status");
                if ("fail".equals(status)) {
                    Toast.makeText(getContext(), "로그인에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else if ("success".equals(status)) {
                    String email = uri.getQueryParameter("email");
                    if (email != null) {
                        if (interaction != null) {
                            interaction.onAppleEmailSuccess(email); //sub rather than email
                        }
                        dismiss();
                    }
                }
            }
            Rect displayRectangle = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

            // Set height of the Dialog to 90% of the screen
            android.view.ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = (int) (displayRectangle.height() * 0.9f);
            view.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public interface Interaction {
        void onAppleEmailSuccess(String email);
    }

}
