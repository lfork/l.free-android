package com.lfork.a98620.lfree.data.entity;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by 98620 on 2018/4/7.
 */

public class Category extends DataSupport implements Serializable {

    private int id;

    private int csId;

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

    public int getCsId() {
        return csId;
    }

    public void setCsId(int csId) {
        this.csId = csId;
    }

    @Override
    public String toString() {
        return name;
    }
}
