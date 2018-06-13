package com.lfork.a98620.lfree.data.entity;

/**
 * Created by 98620 on 2018/6/12.
 */
public class School {
     private String id;

     private String schoolName;

    public School(String id, String schoolName) {
        this.id = id;
        this.schoolName = schoolName;
    }

    public School() {
    }

    public School(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Override
    public String toString() {
        return schoolName;
    }
}
