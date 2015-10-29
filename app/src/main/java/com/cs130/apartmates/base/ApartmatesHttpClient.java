package com.cs130.apartmates.base;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ApartmatesHttpClient {
    private static final String ENDPOINT = "http://backend-apartmates.rhcloud.com";
    private static volatile ApartmatesHttpClient client;

    private ApartmatesHttpClient() {
    }

    public static ApartmatesHttpClient getInstance() {
        if (client == null) {
            synchronized (ApartmatesHttpClient.class) {
                if (client == null) {
                    client = new ApartmatesHttpClient();
                }
            }
        }
        return client;
    }

    //sends request to given url endpoint and returns response in string
    public String sendRequest(String address, HashMap<String, String> params, String data, String method) {
        try {
            String fulladdress;
            if (params != null && !params.isEmpty()) {
                StringBuilder result = new StringBuilder();
                boolean first = true;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (first)
                        first = false;
                    else
                        result.append("&");

                    result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
                fulladdress = ENDPOINT + address + "?" + result;
            } else {
                fulladdress = ENDPOINT + address;
            }
            System.err.println(fulladdress);

            return new RequestTask().execute(fulladdress, data, method).get(3, TimeUnit.SECONDS).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

