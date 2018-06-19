package com.lfork.a98620.lfree.data.entity;

public class IMUser {
    private String username;

    private String password;

    private int id;

    private String nickname;

    private String gender;

    private String phone;

    private String email;

    private String portraitPath;

    private int deleted;  //0表示 在列表当中 ，1表示被删除了

    private int newMessageNumber; //未读消息的数量

    public IMUser() {
    }

    public IMUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public IMUser(String username, String password, int id) {
        this(username, password);
        this.id = id;
    }

    public String getIMUsername() {
        return username;
    }

    public void setIMUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }

    @Override
    public String toString() {
        return "[{\"username\":\"" + getIMUsername() + "\",\"id\":\"" + getId() + "\"}]";
    }
}
