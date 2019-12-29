package com.whoisspy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class User {

    private String account;
    private String email;
    private String photoBase64;
    private Image photo;

    public User(String account, String email, String photoBase64) {
        this.account = account;
        this.email = email;
        this.photoBase64 = photoBase64;
//        this.photo = ImageExtensions.base64StringToImage(photoBase64);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }
}