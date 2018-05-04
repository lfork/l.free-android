package com.lfork.a98620.lfree.data.imdata.local;

import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.imdata.IMDataSource;
import com.lfork.a98620.lfree.imservice.TCPConnection;
import com.lfork.a98620.lfree.imservice.request.LoginListener;

import org.litepal.crud.DataSupport;

import java.util.List;

public class IMLocalDataSource implements IMDataSource {
    private TCPConnection mConnection;

    private static IMLocalDataSource INSTANCE;


    private IMLocalDataSource() {
//        mConnection = new TCPConnection(Config.URL, 7000);
//        mConnection.start();
    }


    public static IMLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IMLocalDataSource();
        }
        return INSTANCE;
    }

    public static void releaseInstance() {
        if (INSTANCE != null ) {
            if (INSTANCE.mConnection.closeConnection()){
                INSTANCE = null;
            }
        }
    }

    @Override
    public void login(User user, LoginListener listener) {
//        IMUser u = new IMUser();
//        u.setId(user.getId());
//        Request<IMUser> request = new Request<IMUser>()
//                .setData(u)
//                .setRequestType(UserRequestType.DO_LOGIN)
//                .setMessage("haha");
//
//        mConnection.setUser(u);
//
//        if (!mConnection.send(request.getRequest())) {
//            listener.failed("与服务器的连接已断开");
//            return;
//        }
//        //这里还需要添加一个超时检测， 因为网络连接有问题的话 服务器是不会发送回执给客户端的
//        //超时检测线程  -》 错了，应该在TCPConnection里面设置这个，
//
//        String strResult = mConnection.receive();
//        Result<User> result = JSONUtil.parseJson(strResult, new TypeToken<Result<User>>() {
//        });
//        if (result != null) {
//            switch (result.getCode()) {
//                case -1:
//                    listener.failed("未知错误 -1");
//                    break;
//                case 0:
//                    listener.failed("密码错误");
//                    break;
//                case 1:
//                    listener.succeed(result.getData());
//                    break;
//                case 2:
//                    listener.failed("账号无效");
//                    break;
//                default:
//                    listener.failed("未知错误 -2");
//                    break;
//
//            }
//        } else {
//            listener.failed("连接失败");
//        }


    }

    @Override
    public void logout(int userId, GeneralCallback<User> callback) {
//        Request request = new Request()
//                .setRequestType(UserRequestType.DO_LOGOUT)
//                .setMessage(userId + "");
//
//        if (!mConnection.send(request.getRequest())) {
//            callback.failed("与服务器的连接已断开");
//            return;
//        }
//
//        Result result = JSONUtil.parseJson(mConnection.receive(), new TypeToken<Result>() {
//        });
//        if (result != null) {
//            switch (result.getCode()) {
//                case 0:
//                    callback.failed("未知错误 -3");
//                    break;
//                case 1:
//                    callback.succeed(null);
//                    break;
//                default:
//                    callback.failed("未知错误 -2");
//                    break;
//            }
//        } else {
//            callback.failed("连接失败");
//        }
//

    }

    @Override
    public void getChatUserList(GeneralCallback<List<User>> callback) {
        List<User> userList = DataSupport.where("ischatuser=1").order("timestamp desc").find(User.class);
        if (userList != null){
            callback.succeed(userList);
        } else {
            callback.failed("没有数据");
        }
    }

    @Override
    public void addChatUser(User user, GeneralCallback<String> callback) {
        user.setChatUser(true);
        user.setTimestamp(System.currentTimeMillis());
        user.save();
        callback.succeed("添加成功");
    }

    @Override
    public void addChatUser(User user, boolean isExisted, GeneralCallback<String> callback) {

    }

    @Override
    public void removeChatUser(int userId, GeneralCallback<List<User>> callback) {
        DataSupport.deleteAll(User.class,"userid = ?", userId+"");
        callback.succeed(null);
    }


//    @Override
//    public void getChatUserInfo(int userId, GeneralCallback<String> callback) {
//
//    }

}
