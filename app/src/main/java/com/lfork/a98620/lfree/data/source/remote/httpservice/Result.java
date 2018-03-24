package com.lfork.a98620.lfree.data.source.remote.httpservice;

import com.google.gson.annotations.SerializedName;

public class Result<T> {

    private int code;

    @SerializedName("msg")
    private String message;

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
