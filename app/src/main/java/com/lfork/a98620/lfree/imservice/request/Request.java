package com.lfork.a98620.lfree.imservice.request;


//客户端构造请求 -> 客户端发送请求  -> 服务器接收请求 ->服务器解析请求 ->服务器处理请求 ->服务器返回处理结果 ->客户端接收请求结果 ->客户端解析请求结果 ->客户端处理请求结果
//构造规则和解析规则


import com.lfork.a98620.lfree.util.JSONUtil;

/**
 * T表示了请求 需要传递的数据对象
 * @param <T>
 */
public class Request<T> {

    private UserRequestType requestType;   //请求类型

    private String message;         //其他请求信息

    private T data;     //请求里面的具体信息: 请求类型 用户名 用户ID 用户密码 用户的其他信息等

    public UserRequestType getRequestType() {
        return requestType;
    }

    public Request<T> setRequestType(UserRequestType requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Request<T>setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Request<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getRequest() {
        return JSONUtil.toJson(this);
    }


    // 启动消息监听

//    public String getTaskType() {
//        return taskType;
//    }
//
//    public void setTaskType(String taskType) {
//        this.taskType = taskType;
//    }
//
//    public String getLoginRequest() {
//        StringBuilder str = new StringBuilder();
//        str.append("[");
//        String temp = "{\"taskType\":\"" + taskType + "\",\"username\":\"" + user.getSellerName() + "\",\"password\":\""
//                + user.getPassword() + "\"}";
//        str.append(temp);
//        str.append("]");
//        return str.toString();
//    }
//
//    public String getGeneralRequest() {
//        StringBuilder str = new StringBuilder();
//        str.append("[");
//        String temp = "{\"taskType\":\"" + taskType
//                + "\",\"username\":\"" + user.getSellerName()
//                + "\",\"senderId\":\"" + user.getId()
//                + "\",\"content\":\"" + task
//                + "\"}";
//        str.append(temp);
//        str.append("]");
//        return str.toString();
//    }
//
//    public String getRegisterRequest() {
//        StringBuilder str = new StringBuilder();
//        str.append("[");
//        String temp = "{\"taskType\":\"" + taskType + "\",\"username\":\"" + user.getSellerName() + "\",\"password\":\""
//                + user.getPassword() + "\",\"nickname\":\"" + user.getNickname() + "\",\"phonenumber\":\""
//                + user.getPhonenumber() + "\",\"email\":\"" + user.getEmail() + "\",\"gender\":\"" + user.getGender()
//                + "\"}";
//        str.append(temp);
//        str.append("]");
//        return str.toString();
//    }
//
//    public String getRequest(){
//        return null;
//    }



}


