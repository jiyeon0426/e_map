package com.kumoh19.e_map;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String kakaoId;
    public String userName;
    public String image;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String kakaoId, String userName, String image) {
        this. kakaoId = kakaoId;
        this.userName = userName;
        this.image = image;
    }
    public String getKakaoId() { return kakaoId; }

    public void setKakaoId(String userName) {
        this.kakaoId = kakaoId;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID='" + kakaoId + '\'' +
                "userName='" + userName + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
