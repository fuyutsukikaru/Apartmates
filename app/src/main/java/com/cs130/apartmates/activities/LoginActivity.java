package com.cs130.apartmates.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.cs130.apartmates.R;
import com.cs130.apartmates.base.ApartmatesHttpClient;
import com.cs130.apartmates.base.RequestTask;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginClick(View v) {
        String url = "https://api.venmo.com/v1/oauth/authorize?client_id=3003&scope=make_payments%20access_profile%20access_email%20access_phone%20access_balance&response_type=code";
        final String loginEndpoint = "http://backend-apartmates.rhcloud.com/user/login";

        final Dialog diag = new Dialog(this);
        diag.setContentView(R.layout.dialog_webview);
        WebView webView = (WebView) diag.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            boolean authComplete = false;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains("?code=") && !authComplete) {
                    Uri uri = Uri.parse(url);
                    String code = uri.getQueryParameter("code");
                    System.err.println("Code is " + code);
                    diag.cancel();
                    new LoginTask().execute(loginEndpoint + "?code=" + code, null, "POST");
                }
            }
        });
        diag.show();

    }

    private class LoginTask extends RequestTask {
        @Override
        public void onPostExecute(JSONObject result) {
            try {
                if (result == null || !result.has("user_id")) {
                    Snackbar.make(findViewById(R.id.login_fragment), R.string.login_error, Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                    prefs.edit().putLong("userId", Long.parseLong(result.getString("user_id"))).apply();
                    if (result.has("group_id") && result.get("group_id") != null) {
                        prefs.edit().putLong("groupId", Long.parseLong(result.getString("group_id"))).apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, GroupCreateActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
