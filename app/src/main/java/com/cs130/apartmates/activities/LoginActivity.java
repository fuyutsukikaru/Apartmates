package com.cs130.apartmates.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.cs130.apartmates.R;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private static final String EXTRA_CUSTOM_TABS_SESSION =
            "android.support.customtabs.extra.SESSION";
    private static final String EXTRA_CUSTOM_TABS_TOOLBAR_COLOR =
            "android.support.customtabs.extra.TOOLBAR_COLOR";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.login_activity);
    }

    public void onLoginClick(View v) {
        String url = "https://api.venmo.com/v1/oauth/authorize?client_id=3003&scope=make_payments%20access_profile%20access_email%20access_phone%20access_balance&response_type=code";

        /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Bundle extras = new Bundle();
        extras.putBinder(EXTRA_CUSTOM_TABS_SESSION, null);
        intent.putExtras(extras);
        intent.putExtra(EXTRA_CUSTOM_TABS_TOOLBAR_COLOR, ContextCompat.getColor(this, R.color.colorPrimary));

        startActivity(intent);*/



        final Dialog diag = new Dialog(this);
        diag.setContentView(R.layout.dialog_webview);
        webView = (WebView) diag.findViewById(R.id.webview);
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
                    //new LoginTask().execute(code);
                }
            }
        });
        diag.show();

    }

    private class LoginTask extends AsyncTask<String, String, JSONObject> {
        private HttpURLConnection conn;
        private URL url;

        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                url = new URL(args[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", args[1]);
            } catch(Exception e) {
                System.err.println(e);
            } finally {
                conn.disconnect();
            }
            return null;
        }
    }

}
