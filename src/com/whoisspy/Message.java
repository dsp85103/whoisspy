package com.whoisspy;

import com.google.gson.Gson;

public class Message {

    public enum OP {
        login,
        signUp,
        modifyPassword,
        logout,
        modifyProfile,
        createRoom,
        joinRoom,
        listRooms,
        leaveRoom,
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

    public OP getOp() {
        return op;
    }

    public void setOp(OP op) {
        this.op = op;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static String makeMessageString(Message message) {
        return new Gson().toJson(message);
    }
}
