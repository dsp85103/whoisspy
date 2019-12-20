package com.whoisspy;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    public String account = "";
    public String tag;

    public Logger(String tag, String account) {
        this.tag = tag;
        this.account = account;
    }

    public Logger(String tag) {
        this.tag = tag;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void log(String str) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss.SSS");
        if (!account.equals("")) {
            System.out.println(String.format("[%s][%s][%s] %s", sdf.format(now), tag, account, str));
        } else {
            System.out.println(String.format("[%s][%s] %s", sdf.format(now), tag,  str));
        }
    }


}
