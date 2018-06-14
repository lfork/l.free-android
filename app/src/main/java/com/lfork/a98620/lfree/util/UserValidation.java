package com.lfork.a98620.lfree.util;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidation {
	/**
	 * 所运用的正则表达式的集合：
	 * userId 的正则表达式："-?[1-9]\\d*" (数字)
	 * userName的正则表达式："[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+" ()
	 * userPassword 的正则表达式："^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$" (只能位数字或者字母)  //8位到16位
	 * userEmail 的正则表达式："\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}"
	 * userPhone 的正则表达式："0?(13|14|15|18)[0-9]{9}"
	 *
	 */

	private static final String regex_UserId = "[1-9]\\d{9}";

	private static final String regex_UserName = "[A-Za-z0-9_\\-\\u4e00-\\u9fa5]{5,16}";

	private static final String regex_UserPassword = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";

	private static final String regex_UserEmail = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

	private static final String regex_UserPhone = "0?(13|14|15|18)[0-9]{9}";

	// 对注册时的数据进行验证
	public static String RegisterValidation(String userId, String userName, String userPassword, String passwordRepeat) {

		if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(passwordRepeat)) {
			return "请输入完整的注册信息";
		}

	    if (!RegexValidation(userId, regex_UserId)) {
	        return "学号必须是10位";
        }

        if (!RegexValidation(userName, regex_UserName)) {
	        return "用户名不符合规范";
        }

        if (!RegexValidation(userPassword, regex_UserPassword)) {
            return "密码不符合规范";
        }


        if (!userPassword.equals(passwordRepeat) ) {
            return "两次输入的密码必须相同";
        }

        return null;
	}

	// 对登录的数据进行验证
	public static boolean LoginValidation(String userId, String userPassword) {
		return RegexValidation(userId, regex_UserId) && RegexValidation(userPassword, regex_UserPassword);
	}

	// 对修改用户信息后保存操作之前的数据进行修改
	public static boolean SaveValidation(String userId, String userName, String userPassword, String userEmail,
										 String userPhone) {
		return RegexValidation(userId, regex_UserId) && RegexValidation(userName, regex_UserName)
				&& RegexValidation(userPassword, regex_UserPassword) && RegexValidation(userEmail, regex_UserEmail)
				&& RegexValidation(userPhone, regex_UserPhone);
	}

	// 传入要验证的字段，以及需要的正则表达式， 进行字段的验证
	private static boolean RegexValidation(String args, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(args);
		System.out.println(matcher.matches());
		return matcher.matches();
	}

}
