package com.plugspace.common;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import com.plugspace.R;

import java.net.URLDecoder;

public class DialogNew extends Dialog {

    Context context;
    AuthenticationListener authenticationListener;
    WebView webView;
    String redirect_url, request_url;

    public DialogNew(@NonNull Context context, AuthenticationListener authenticationListener) {
        super(context, R.style.full_screen_dialog);
        this.context = context;
        this.authenticationListener = authenticationListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_insta_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        redirect_url = context.getResources().getString(R.string.redirect_url);
        request_url = "https://instagram.com/oauth/authorize/?client_id=" +
                context.getResources().getString(R.string.client_id) +
                "&redirect_uri=" + redirect_url +
                "&response_type=code&scope=user_profile";

        Log.e("URL", redirect_url + " -- " + request_url);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(request_url);
        webView.setWebViewClient(webViewClient);

    }

    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(redirect_url)) {
                DialogNew.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            try {
                String afterDecode = URLDecoder.decode(url, "UTF-8");
                Log.e("AA_S",afterDecode + " -- ");
                if (afterDecode.contains("code=")) {
                    Uri uri = Uri.EMPTY.parse(afterDecode);
                    String u = uri.getQueryParameter("u");
                    Uri u1 = Uri.EMPTY.parse(u);
                    String code = u1.getQueryParameter("code");
                    Log.e("AA_S-CODE",code + " -- ");
                    authenticationListener.onTokenReceived(code);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

}
