package com.lfork.a98620.lfree.data.source;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.source.remote.imservice.request.LoginListener;

import java.util.List;

public interface IMDataSource extends DataSource {

	/**
	 * 登录成功后，客户端会马上请求用户的详细信息(当然，不包含密码)。 //TODO请求完毕后需要更新TCP本地连接的用户信息
	 */
	void login(User user, LoginListener listener);

	void logout(int userId, GeneralCallback<User> result);

	void getChatUserList(GeneralCallback<List<User>> callback);

	void addChatUser(User user, GeneralCallback<String> callback);

	void removeChatUser(int userId, GeneralCallback<List<User>> callback);

//	void getChatUserInfo(int userId, GeneralCallback<String> callback);

}
