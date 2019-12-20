package com.whoisspy;

public class Message {

    public enum OP {
        login,
        signup,
        modifypwd
    }

    public enum Status {
        process,
        success,
        failure
    }

    public OP op;
    public Status status;
    public String msg;
    public String data;

    public Message(OP op, Status status, String msg, String data) {
        this.op = op;
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

}
