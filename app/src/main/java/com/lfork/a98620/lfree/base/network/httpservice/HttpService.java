package com.lfork.a98620.lfree.base.network.httpservice;

import android.util.Log;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.util.http.HttpLogger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by 98620 on 2018/3/24.
 * 使用OkHttpClient 的时候需要注意以下几点：
 * 最好只使用一个共享的OkHttpClient 实例，将所有的网络请求都通过这个实例处理。
 * 因为每个OkHttpClient 实例都有自己的连接池和线程池，重用这个实例能降低延时，减少内存消耗，而重复创建新实例则会浪费资源。
 * OkHttpClient的线程池和连接池在空闲的时候会自动释放，所以一般情况下不需要手动关闭，但是如果出现极端内存不足的情况，可以使用以下代码释放内存：
 * client.dispatcher().executorService().shutdown();   //清除并关闭线程池
 * client.connectionPool().evictAll();                 //清除并关闭连接池
 * client.cache().close();                             //清除cache
 * 如果对一些请求需要特殊定制，可以使用
 * OkHttpClient eagerClient = client.newBuilder()
 * .readTimeout(500, TimeUnit.MILLISECONDS)
 * .build();
 * 这样创建的实例与原实例共享线程池、连接池和其他设置项，只需进行少量配置就可以实现特殊需求。
 */
public class HttpService {

    private static final String TAG = "HttpService";

    private final static int CONNECT_TIMEOUT = 40;
    private final static int READ_TIMEOUT = 30;
    private final static int WRITE_TIMEOUT = 30;

    private OkHttpClient client;

    private static HttpService INSTANCE = null;

    /**
     *  Double-checked locking for singleton
     * @return instance
     */
    public static HttpService getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpService.class) {
                if (INSTANCE == null)
                    INSTANCE = new HttpService();
            }
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

    public void closeConnection() {
        try {
            if (client != null && client.cache() != null)
                client.cache().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * synchronize post
     */
    public String sendPostRequest(String url, RequestBody requestBody) {
        Request request = new Request.Builder().url(url)
                .post(requestBody).build();
        Response response = null;
        String result = null;
        try {
            response = client.newCall(request).execute(); //同步请求
            result = response.body().string();
        } catch (IOException e) {
            Log.d(TAG, "sendPostRequest: " + e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return result;
    }

    /**
     * async post
     */
    public void sendPostRequestAsync(String url, RequestBody requestBody, DataSource.GeneralCallback<String> callback) {
        Request request = new Request.Builder().url(url)
                .post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.failed(e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.succeed(response.body().string());

                if (response != null) {
                    response.close();
                }
            }
        });



    }
}
