package com.lfork.a98620.lfree.data.source.remote.imservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.chatwindow.ChatWindowActivity;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.entity.message.Message;
import com.lfork.a98620.lfree.data.source.IMDataRepository;
import com.lfork.a98620.lfree.data.source.MessageDataRepository;
import com.lfork.a98620.lfree.data.source.UserDataRepository;
import com.lfork.a98620.lfree.data.source.remote.imservice.request.LoginListener;

public class MessageService extends Service {
    private static final String TAG = "MessageService";

    private MessageBinder mBinder = new MessageBinder();

    private IMDataRepository repository;

//    private TCPConnection mConnection;

    /**
     * 在这里进行TCP连接的建立
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

//    private void buildTcpConnection() {
////        mConnection = new TCPConnection(Config.URL, 7000);
////        mConnection.start();
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //为主线程提供服务控制器 进而控制消息的接收
    public class MessageBinder extends Binder {

        private User user;

        private MessageDataRepository messageDataRepository;

        private MessageListener viewListener;

        MessageListener listener = new MessageListener() {
            @Override
            public void onReceived(Message message) {
                //收到消息后 1、把消息储存到消息仓库

                messageDataRepository.addReceivedMessage(message);

                if (viewListener != null) {
                    //2、如果能显示就进行显示
                    viewListener.onReceived(message);
                } else
                    //3、如果不能显示就进行通知
                    sendChatMsg(null, message.getContent());
                }



        };


//        private  MessageListener listener;


        MessageBinder() {
            user = new User();
            user.setId(UserDataRepository.getInstance().getUserId());
        }

        /**
         * 建立TCP连接，并进行用户登录
         */
        public void buildConnection() {
            Log.d(TAG, "buildConnection: 正在建立TCP UDP连接");
            Log.d(TAG, "buildUDPConnection: 不执行这里的吗？？5");
            new Thread(() -> {
                //这里会获取一些连接的索引 dataRepository。。  用来及时地进行交互

//            buildTcpConnection();

                //进行登录操作
                repository = IMDataRepository.getInstance();

                Log.d(TAG, "buildUDPConnection: 不执行这里的吗？？4");
                repository.login(user, new LoginListener() {
                    @Override
                    public void succeed(User user) {
                        Log.d(TAG, "buildUDPConnection: 不执行这里的吗？？3");
                        buildUDPConnection();
                    }

                    @Override
                    public void failed(String log) {
                        Log.d(TAG, "buildUDPConnection: 不执行这里的吗？？8" + log);

                    }
                });
            }).start();
        }

        private void buildUDPConnection() {
            Log.d(TAG, "buildUDPConnection: 不执行这里的吗？？1");
            messageDataRepository = MessageDataRepository.getInstance(user.getId());
            messageDataRepository.setMessageListener(listener);
            repository.setServiceBinder(this);
            Log.d(TAG, "buildUDPConnection: 不执行这里的吗？？2");

        }

        public void closeConnection() {

            MessageDataRepository.destroyInstance();
            IMDataRepository.destroyInstance();
            Log.d(TAG, "closeConnection: Message TCP/UDP相关资源已释放");
        }

        public void sendMessage(Message message, DataSource.GeneralCallback<Message> callback) {
            messageDataRepository.saveAndSendMessage(message, callback);

        }

        public void setListener(MessageListener listener) {
            this.viewListener = listener;
        }

        public void cancelListener(){
            viewListener = null;
        }
    }

    public MessageService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    /**
     * 将消息在任务栏进行通知
     */
    private void messageNotify() {

    }


    //下载任务的UI部分， 也就是状态栏的通知
    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, ChatWindowActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);

        if (progress > 0) {
            //当progress 大于或者等于0时才需要显示下载进度
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }


    public void sendChatMsg(View view, String message) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "chat")
                .setContentTitle("收到一条聊天消息")
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.main_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.main_icon))
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }

    public void sendSubscribeMsg(View view) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "subscribe")
                .setContentTitle("收到一条订阅消息")
                .setContentText("地铁沿线30万商铺抢购中！")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.main_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.main_icon))
                .setAutoCancel(true)
                .build();
        manager.notify(2, notification);
    }
}
