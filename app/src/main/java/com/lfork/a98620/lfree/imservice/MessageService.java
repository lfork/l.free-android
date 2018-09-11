package com.lfork.a98620.lfree.imservice;

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

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.FreeApplication;
import com.lfork.a98620.lfree.chatwindow.ChatWindowActivity;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.base.entity.User;
import com.lfork.a98620.lfree.data.imdata.IMDataRepository;
import com.lfork.a98620.lfree.data.imdata.MessageDataRepository;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.imservice.message.Message;
import com.lfork.a98620.lfree.imservice.request.LoginListener;

import java.util.List;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class MessageService extends Service {
    private static final String TAG = "MessageService";

    private MessageBinder mBinder = new MessageBinder();

    private IMDataRepository repository;


    /**
     * 在这里进行TCP连接的建立
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 为主线程提供服务控制器 进而控制消息的接收
     */
    public class MessageBinder extends Binder implements MessageListener {

        private User user;

        private MessageDataRepository messageDataRepository;

        private MessageListener viewListener;

        private int currentContactId;


        @Override
        public void onReceived(Message message) {
            //收到消息后 1、把消息储存到消息仓库

            if (viewListener != null) {
                //2、如果能显示就进行显示
                if (message.getSenderID() == currentContactId) {
                    viewListener.onReceived(message);
                    return;
                }
            }
            //3、如果不能显示就进行通知
            sendChatMsg(message.getSenderID(), message.getContent());
        }


        MessageBinder() {
            user = new User();
            user.setId(UserDataRepository.INSTANCE.getUserId());
        }

        /**
         * 建立TCP连接，并进行用户登录
         */
        public void buildConnection() {
            FreeApplication.executeThreadInDefaultThreadPool(() -> {
                //这里会获取一些连接的索引 dataRepository。。  用来及时地进行交互
                //进行登录操作
                repository = IMDataRepository.Companion.getInstance();
                repository.login(user, new LoginListener() {
                    @Override
                    public void succeed(User user) {
                        buildUDPConnection();
                    }

                    @Override
                    public void failed(String log) {
                    }
                });
            });
        }

        private void buildUDPConnection() {
            messageDataRepository = MessageDataRepository.Companion.getInstance(user.getId());
            messageDataRepository.setMessageListener(this);
            repository.setServiceBinder(this);
        }

        public void closeConnection() {
            MessageDataRepository.Companion.destroyInstance();
            IMDataRepository.Companion.destroyInstance();
        }

        public void sendMessage(Message message, DataSource.GeneralCallback<Message> callback) {
            messageDataRepository.saveAndSendMessage(message, callback);
        }

        public void setListener(MessageListener listener, int userId) {
            this.viewListener = listener;
            currentContactId = userId;
        }

        public void cancelListener() {
            viewListener = null;
            currentContactId = 0;
        }
    }

    public MessageService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    //下载任务的UI部分， 也就是状态栏的通知
    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }


    public void sendChatMsg(int senderID, String message) {
        UserDataRepository repository = UserDataRepository.INSTANCE;
        repository.getUserInfo(new DataSource.GeneralCallback<User>() {
            @Override
            public void succeed(User data) {
                showChatNotification(message, data.getUserName(), senderID);
                addUser(data);
            }

            @Override
            public void failed(String log) {
            }
        }, senderID);

    }


    private void addUser(User data) {
        repository.getChatUserList(new DataSource.GeneralCallback<List<User>>() {
            @Override
            public void succeed(List<User> data1) {
                repository.addChatUser(data, repository.isUserExisted(data.getUserId()), new DataSource.GeneralCallback<String>() {
                    @Override
                    public void succeed(String data) {
                        Log.d(TAG, "succeed: " + "添加成功");
                    }

                    @Override
                    public void failed(String log) {
                        Log.d(TAG, "failed: " + log);

                    }
                });
            }

            @Override
            public void failed(String log) {

            }
        });


    }

    public void showChatNotification(String message, String senderName, int senderID) {
        Intent intent = new Intent(this, ChatWindowActivity.class);
        intent.putExtra("user_id", senderID);
        intent.putExtra("username", senderName);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "chat")
                .setContentTitle("收到一条聊天消息")
                .setContentText(senderName + ":" + message)
                .setContentIntent(pi)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.main_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.main_icon))
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }


//    private Notification getNotification(String title, int progress) {
//        Intent intent = new Intent(this, ChatWindowActivity.class);
//        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
//        builder.setContentIntent(pi);
//        builder.setContentTitle(title);
//
//        if (progress > 0) {
//            //当progress 大于或者等于0时才需要显示下载进度
//            builder.setContentText(progress + "%");
//            builder.setProgress(100, progress, false);
//        }
//        return builder.build();
//    }

//    public void showSubscribeNotification(View view) {
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification notification = new NotificationCompat.Builder(this, "subscribe")
//                .setContentTitle("收到一条订阅消息")
//                .setContentText("地铁沿线30万商铺抢购中！")
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.main_icon)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.main_icon))
//                .setAutoCancel(true)
//                .build();
//        manager.notify(2, notification);
//    }
}
