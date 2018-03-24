package com.lfork.a98620.lfree.data.source.remote.httpservice;

import android.util.Log;

import com.lfork.a98620.lfree.data.DataSource;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 98620 on 2018/3/24.
 */

public class Service {

    private static final String TAG = "Service";

    public String sendPostRequest(String url, RequestBody requestBody) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url)
                .post(requestBody).build();

        try {
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            Log.d(TAG, "sendPostRequest: " + responseData);
            return responseData;
        } catch (IOException e) {
            return null;
        }


    }

}
