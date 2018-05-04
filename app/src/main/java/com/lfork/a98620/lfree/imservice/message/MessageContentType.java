package com.lfork.a98620.lfree.imservice.message;

public enum MessageContentType{
		COMMUNICATION_USER ,	//用户的聊天信息(单纯的字符串)

		COMMUNICATION_GROUP, //群组的聊天信息(单纯的字符串)

		COMMAND,	//系统命令(可见或者不可见)

		NOTIFICATION	//系统通知_好友添加的请求
	}