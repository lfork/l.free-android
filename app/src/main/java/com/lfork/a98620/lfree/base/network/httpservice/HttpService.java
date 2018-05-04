package com.lfork.a98620.lfree.base.network.httpservice;

import com.lfork.a98620.lfree.util.http.HttpLogger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by 98620 on 2018/3/24.
 */

public class HttpService {

    private static final String TAG = "HttpService";

    private final static int CONNECT_TIMEOUT = 40;
    private final static int READ_TIMEOUT = 30;
    private final static int WRITE_TIMEOUT = 30;

    private static HttpService INSTANCE;

    private OkHttpClient client;

    public static HttpService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HttpService();
        }

        return INSTANCE;

    }

    private HttpService() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient()
                .newBuilder()
                .addNetworkInterceptor(logInterceptor)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .build();
    }

    public static void destroyInstance() {
        try {
            if (INSTANCE.client != null)
                INSTANCE.client.cache().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        INSTANCE = null;

    }


    /**
     * post
     */
    public String sendPostRequest(String url, RequestBody requestBody) {
        Request request = new Request.Builder().url(url)
                .post(requestBody).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseData = response.body().string();
            return responseData;
        } catch (IOException e) {
            return null;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

}
