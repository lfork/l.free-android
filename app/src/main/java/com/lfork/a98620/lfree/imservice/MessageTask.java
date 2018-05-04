package com.lfork.a98620.lfree.imservice;

import android.os.AsyncTask;

import com.lfork.a98620.lfree.imservice.message.Message;

///**
// * Created by 98620 on 2017/8/18.
// */

class MessageTask extends AsyncTask<String, Message, Integer> {

    private static final String TAG = "DownloadTask";
    private long downloadedLength = 0;
    private static final int TYPE_SUCCESS = 0;
    private static final int TYPE_FAILED = 1;
    private static final int TYPE_PAUSED = 2;
    private static final int TYPE_CANCELED = 3;
    private static final int TYPE_DOWNLOADING = 4;
    private  int DownloadStatus = TYPE_DOWNLOADING;

    private MessageListener listener;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;
    private String address ;


    //传入参数为下载监听器，  随时监听下载任务的状态， 结合  publishProgress 和 onProgressUpdate 进行 UI刷新
    MessageTask(MessageListener listener, String address) {
        this.listener = listener;
        this.address = address;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        return 0;
    }

    //在这里进行UI更新的调用。 配合listener 对下载过程中的状态进行监控 paused
    @Override
    protected void onProgressUpdate(Message... values) {
        super.onProgressUpdate(values);
        Message msg = values[0];
        if (msg != null) {
            listener.onReceived(msg);
        }
    }


    //下载任务完毕后(Success Failure Canceled) 就执行这个方法 。 配合listener对下载完毕的状态进行监控
    @Override
    protected void onPostExecute(Integer status) {
    }

}
