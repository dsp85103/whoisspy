package com.whoisspy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class User {

    private String account;
    private String email;
    private Image photo;

    public User(String account, String email, String photobase64) {
        this.account = account;
        this.email = email;
        this.photo = ImageExtensions.base64StringToImage(photobase64);
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

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }
}