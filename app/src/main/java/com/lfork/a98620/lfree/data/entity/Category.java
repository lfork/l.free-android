package com.lfork.a98620.lfree.data.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 98620 on 2018/4/7.
 */

public class Category implements Serializable {

    @SerializedName("csId")
    private int id;

    @SerializedName("csName")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
