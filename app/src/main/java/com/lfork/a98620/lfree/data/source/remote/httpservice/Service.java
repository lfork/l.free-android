package com.lfork.a98620.lfree.data.source.remote.httpservice;

import com.lfork.a98620.lfree.util.http.HttpLogger;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by 98620 on 2018/3/24.
 */

public class Service {

    private static final String TAG = "Service";

    /**
     *  异步请求
     */
    public String sendPostRequest(String url, RequestBody requestBody) {

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient().newBuilder().addNetworkInterceptor(logInterceptor).build();

        Request request = new Request.Builder().url(url)
                .post(requestBody).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseData = response.body().string();
//            Log.d(TAG, "sendPostRequest: " + responseData);
            return responseData;
        } catch (IOException e) {
            return null;
        } finally {
            if (response != null){
                response.close();
            }
        }
    }

}
