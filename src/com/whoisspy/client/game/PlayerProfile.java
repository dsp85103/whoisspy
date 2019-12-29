package com.whoisspy.client.game;

import com.whoisspy.ImageExtensions;

import java.awt.*;

public class PlayerProfile {

    private String account;
    private String photoBase64;

    public PlayerProfile(String account, String photoBase64) {
        this.account = account;
        this.photoBase64 = photoBase64;
    }


    public String getAccount() {
        return account;
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }
}
