package com.lfork.a98620.lfree.data.source;

import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.source.local.IMLocalDataSource;
import com.lfork.a98620.lfree.data.source.remote.IMRemoteDataSource;
import com.lfork.a98620.lfree.data.source.remote.imservice.MessageService;
import com.lfork.a98620.lfree.data.source.remote.imservice.request.LoginListener;

import java.util.LinkedList;
import java.util.List;

/**
 */
public class IMDataRepository implements IMDataSource {

    private static IMDataRepository INSTANCE;

    private IMDataSource mRemoteDataSource, mLocalDataSource;

    private User mCachedUser;

//    private HashMap<Integer, Friend> mCachedFriendsMap;

    private List<User> mCachedUserList;

    private boolean mCachedIMIsDirty, mCachedFriendListIsDirty;

    public static final int USER_THIS = -1;

    private MessageService.MessageBinder binder;


    private IMDataRepository(IMDataSource mRemoteTRDataSource, IMDataSource mLocalDataSource) {
        this.mRemoteDataSource = mRemoteTRDataSource;
        this.mLocalDataSource = mLocalDataSource;
    }


    public static IMDataRepository getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            INSTANCE = new IMDataRepository(IMRemoteDataSource.getInstance(), IMLocalDataSource.getInstance());
            return INSTANCE;
        }
    }

    public static void destroyInstance() {
        IMRemoteDataSource.releaseInstance();
//        IMLocalDataSource.releaseInstance();
        INSTANCE = null;
    }

    public void setServiceBinder(MessageService.MessageBinder binder) {
        this.binder = binder;

    }

    public MessageService.MessageBinder getBinder() {
        return binder;
    }

    @Override
    public void login(User user, LoginListener listener) {
        mRemoteDataSource.login(user, new LoginListener() {
            @Override
            public void succeed(User user) {
                //如果是Android的话 这里记得要返回UI线程  使用Handler或者是Handler的封装
                mCachedUser = user;
                listener.succeed(user);
            }

            @Override
            public void failed(String log) {
                //如果是Android的话 这里记得要返回UI线程  使用Handler或者是Handler的封装
                listener.failed(log);
            }
        });

    }

    @Override
    public void logout(int userId, GeneralCallback<User> result) {
        new Thread(() -> {
            mRemoteDataSource.logout(userId, result);
        }).start();
    }

    @Override
    public synchronized void getChatUserList(GeneralCallback<List<User>> callback) {
        if (mCachedUserList != null) {
            callback.succeed(mCachedUserList);
            return;
        }
        mLocalDataSource.getChatUserList(new GeneralCallback<List<User>>() {
            @Override
            public void succeed(List<User> data) {
                mCachedUserList = data;
                callback.succeed(data);
            }

            @Override
            public void failed(String log) {
                callback.failed(log);
            }
        });
    }

    @Override
    public synchronized void addChatUser(User user, GeneralCallback<String> callback) {

    }

    @Override
    public synchronized void addChatUser(User user, boolean isExisted, GeneralCallback<String> callback) {
        if (isExisted) {
            if (mCachedUserList != null) {
                for (User u : mCachedUserList) {
                    if (user.getUserId() == u.getUserId()) {
                        mCachedUserList.remove(u);
                        addChatUserInLocal(u, callback);
                        return;
                    }
                }
            }
        }
        addChatUserInLocal(user, callback);
    }

    private synchronized void addChatUserInLocal(User user,  GeneralCallback<String> callback){
        mLocalDataSource.addChatUser(user, new GeneralCallback<String>() {
            @Override
            public void succeed(String data) {

                if (mCachedUserList == null) {
                    mCachedUserList = new LinkedList<>();
                }
                mCachedUserList.add(0, user);
                callback.succeed(data);
            }

            @Override
            public void failed(String log) {
                callback.failed(log);

            }
        });

    }

    @Override
    public synchronized void removeChatUser(int userId, GeneralCallback<List<User>> callback) {
        mLocalDataSource.removeChatUser(userId, new GeneralCallback<List<User>>() {
            @Override
            public void succeed(List<User> data) {
                for (int i = 0; i < mCachedUserList.size(); i++) {
                    User u = mCachedUserList.get(i);
                    if (u.getUserId() == userId) {
                        mCachedUserList.remove(i);
                        break;
                    }
                }
                callback.succeed(mCachedUserList);
            }

            @Override
            public void failed(String log) {
                callback.failed(log);
            }
        });

    }

    public synchronized boolean isUserExisted(int userId) {
        if (mCachedUserList != null) {
            for (User u : mCachedUserList) {
                if (userId == u.getUserId()) {
                    return true;
                }
            }
        }
        return false;
    }

//    public void getUserIndex(GeneralCallback<Integer> callback) {
//
//    }

//    @Override
//    public void getChatUserInfo(int userId, GeneralCallback<String> callback) {
//
//    }


}
