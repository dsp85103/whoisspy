package com.whoisspy.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import com.whoisspy.Logger;
import com.whoisspy.Message;
import com.whoisspy.User;
import org.bson.Document;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.*;

public class UserConnection extends Thread {

    private String TAG = "Server";
    private String connectionName = "UserConnection";
    private String email;
    private Socket socket;
    private boolean isLogin = false;
    private Gson gson = new Gson();

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Map<String, MongoCollection<Document>> collections = new HashMap<>();

    private Logger logger;

    private User user;

    private UserConnectionObserver userConnectionObserver;
    public UserConnection(Socket socket, UserConnectionObserver userConnectionObserver) {
        super();
        this.userConnectionObserver = userConnectionObserver;
        this.socket = socket;

        logger = new Logger(TAG, connectionName);
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public void addCollection(String collectionName, MongoCollection<Document> collection) {
        collections.put(collectionName, collection);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String recvMessage = (String) input.readObject();  //blocking
                logger.log(String.format("Receiving message '%s'", recvMessage));
                if (recvMessage.equals("exit") || recvMessage.equals("quit")) {
                    close();
                    return;
                }

                processMessage(recvMessage);

            }
        } catch (ClassNotFoundException x) {
            x.printStackTrace();
        } catch (IOException x) {
            x.printStackTrace();
            close();
        }
    }

    public void processMessage(String received) {

//        System.out.println(String.format("processing message '%s'... ... ...", received));
//        send("I received your message: " + received);

        Message message = gson.fromJson(received, Message.class);
        JsonObject data = gson.fromJson(message.data, JsonObject.class);

        Message.OP op = message.getOp();

        switch (op) {

            case login:

                if (message.getStatus().equals(Message.Status.process)) {

                    String account = data.get("account").getAsString();
                    String password = getMD5(data.get("password").getAsString());

                    Document docs = collections.get("users").find(
                            and(eq("account", account),
                                    eq("password", password)
                            )
                    ).first();

                    if (docs != null) {
                        // login success
                        logger.log(String.format("%s login successful", account));
                        JsonObject returnData = new JsonObject();
                        returnData.addProperty("account", docs.getString("account"));
                        returnData.addProperty("email", docs.getString("email"));
                        returnData.addProperty("photo", docs.getString("photo"));
                        Message returnMessage = new Message(
                                Message.OP.login,
                                Message.Status.success,
                                String.format("%s 登入成功！歡迎回來誰是臥底！", account),
                                returnData.toString()
                        );

                        // 登入成功 修改 connection name、status
                        user = new User(docs.getString("account"),
                                docs.getString("email"),
                                docs.getString("photo"));
                        changeLoginStatus(true, docs.getString("account"), user);

                        send(returnMessage);

                    } else {

                        // login failure
                        logger.log(String.format("%s login failure, account or password is warn", account));
                        JsonObject returnData = new JsonObject();
                        returnData.addProperty("account", account);
                        Message returnMessage = new Message(
                                Message.OP.login,
                                Message.Status.failure,
                                String.format("%s 登入失敗！原因：%s！", account, "帳號或密碼錯誤"),
                                returnData.toString()
                        );

                        send(returnMessage);
                    }
                }
                break;

            case signUp:

                if (message.getStatus().equals(Message.Status.process)) {

                    String account = data.get("account").getAsString();
                    String password = getMD5(data.get("password").getAsString());
                    String email = data.get("email").getAsString();
                    String photo = data.get("photo").getAsString();

                    Document queryDocs = collections.get("users").find(eq("account", account)).first();

                    if (queryDocs != null) {
                        // 帳號已經存在

                        //sign up failure
                        logger.log(String.format("%s sign up failure, account already sign up.", account));
                        JsonObject returnData = new JsonObject();
                        returnData.addProperty("account", account);
                        Message returnMessage = new Message(
                                Message.OP.signUp,
                                Message.Status.failure,
                                String.format("%s 註冊失敗！原因：%s！", account, "帳號已經存在"),
                                returnData.toString()
                        );

                        send(returnMessage);


                    } else {
                        // 帳號不存在 可以註冊
                        Document docs = new Document();
                        docs.append("account", account);
                        docs.append("password", password);
                        docs.append("email", email);
                        docs.append("photo", photo);

                        collections.get("users").insertOne(docs);

                        // sign up success
                        logger.log(String.format("%s sign up successful", account));
                        JsonObject returnData = new JsonObject();
                        returnData.addProperty("account", docs.getString("account"));
                        returnData.addProperty("email", docs.getString("email"));
                        returnData.addProperty("photo", docs.getString("photo"));
                        Message returnMessage = new Message(
                                Message.OP.signUp,
                                Message.Status.success,
                                String.format("%s 註冊成功！歡迎來到誰是臥底！請使用帳號密碼登入遊戲！", account),
                                returnData.toString()
                        );


                        // 註冊成功 修改 connection name、status
                        user = new User(account, email, photo);
                        changeLoginStatus(true, account, user);

                        send(returnMessage);
                    }
                }
                break;

            case modifyPassword:

                if (message.getStatus().equals(Message.Status.process)) {

                    String account = data.get("account").getAsString();
                    String email = data.get("email").getAsString();
                    String newPwd = getMD5(data.get("newPwd").getAsString());

                    Document docs = collections.get("users").find(
                            and(eq("account", account),
                                    eq("email", email))
                    ).first();

                    if (docs != null) {
                        // account exist then change pwd
                        UpdateResult updateResult = collections.get("users").updateOne(
                                and(eq("account", account), eq("email", email)),
                                new Document("$set", new Document("password", newPwd)));

                        if (updateResult.getModifiedCount() > 0L) {
                            // 修改密碼成功

                            logger.log(String.format("%s modify password successful", account));
                            JsonObject returnData = new JsonObject();
                            returnData.addProperty("account", docs.getString("account"));
                            Message returnMessage = new Message(
                                    Message.OP.modifyPassword,
                                    Message.Status.success,
                                    String.format("%s 修改密碼成功！請使用新密碼進行登入！", account),
                                    returnData.toString()
                            );

                            // 修改密碼成功 修改為登出狀態
                            changeLoginStatus(false, "UserConnection", null);

                            send(returnMessage);

                        } else {
                            // 修改密碼失敗 ， 也許是 密碼沒有變更

                            logger.log(String.format("%s modify password failure", account));
                            JsonObject returnData = new JsonObject();
                            returnData.addProperty("account", docs.getString("account"));
                            Message returnMessage = new Message(
                                    Message.OP.modifyPassword,
                                    Message.Status.failure,
                                    String.format("%s 修改密碼失敗！請聯絡遊戲管理員！error code：0x1CF66", account),
                                    returnData.toString()
                            );

                            send(returnMessage);
                        }

                    } else {

                        // account or email warn
                        logger.log(String.format("%s modify password failure", account));
                        JsonObject returnData = new JsonObject();
                        returnData.addProperty("account", account);
                        Message returnMessage = new Message(
                                Message.OP.modifyPassword,
                                Message.Status.failure,
                                String.format("%s 修改密碼失敗！帳號或信箱輸入錯誤！", account),
                                returnData.toString()
                        );

                        send(returnMessage);
                    }
                }
                break;

            case logout:

                if (message.getStatus().equals(Message.Status.process)) {

                    if (userConnectionObserver.loginStatus(this)) {

                        String account = data.get("account").getAsString();
                        JsonObject returnData = new JsonObject();
                        returnData.addProperty("account", account);

                        Message returnMessage = new Message(Message.OP.logout,
                                Message.Status.success,
                                String.format("%s 登出成功！期待您下次回來！", account),
                                returnData.toString()
                        );

                        send(returnMessage);

                        // 登出成功 修改狀態
                        changeLoginStatus(false, "UserConnection", null);

                    } else {

                        String account = data.get("account").getAsString();
                        JsonObject returnData = new JsonObject();
                        returnData.addProperty("account", account);

                        Message returnMessage = new Message(Message.OP.logout,
                                Message.Status.failure,
                                String.format("%s 登出失敗！請稍後在試！", account),
                                returnData.toString()
                        );

                        send(returnMessage);

                    }

                }
                break;

            case modifyProfile:

                if (message.getStatus().equals(Message.Status.process)) {

                    String account = data.get("account").getAsString();
                    String newEmail = data.get("email").getAsString();
                    String newPhoto = data.get("photo").getAsString();

                    Document docs = collections.get("users").find(eq("account", account)).first();

                    if (docs != null) {
                        // account exist then change data
                        Document changeDocument = new Document();
                        changeDocument.append("email", newEmail);
                        changeDocument.append("photo", newPhoto);

                        UpdateResult updateResult = collections.get("users").updateOne(
                                eq("account", account),
                                new Document("$set", changeDocument));

                        if (updateResult.getModifiedCount() > 0L) {
                            // 修改資料成功

                            logger.log(String.format("%s modify profile successful", account));
                            JsonObject returnData = new JsonObject();
                            returnData.addProperty("account", account);
                            returnData.addProperty("email", newEmail);
                            returnData.addProperty("photo", newPhoto);
                            Message returnMessage = new Message(
                                    Message.OP.modifyProfile,
                                    Message.Status.success,
                                    String.format("%s 修改資料成功！", account),
                                    returnData.toString()
                            );

                            send(returnMessage);

                        } else {
                            // 修改資料失敗 ， 也許是 資料沒有變更

                            logger.log(String.format("%s modify profile failure", account));
                            JsonObject returnData = new JsonObject();
                            returnData.addProperty("account", account);
                            Message returnMessage = new Message(
                                    Message.OP.modifyProfile,
                                    Message.Status.failure,
                                    String.format("%s 修改資料失敗！請聯絡遊戲管理員！error code：0x17DFF", account),
                                    returnData.toString()
                            );

                            send(returnMessage);
                        }

                    } else {

                        // account or email not found
                        logger.log(String.format("%s modify profile failure", account));
                        JsonObject returnData = new JsonObject();
                        returnData.addProperty("account", account);
                        Message returnMessage = new Message(
                                Message.OP.modifyProfile,
                                Message.Status.failure,
                                String.format("%s 修改資料失敗！帳號或信箱異常錯誤！", account),
                                returnData.toString()
                        );

                        send(returnMessage);
                    }
                }
                break;

            case joinRoom:

                if (message.getStatus().equals(Message.Status.process)) {

                    boolean result = userConnectionObserver.onJoinRoom(this, data.get("roomId").getAsInt());

                    if (result) {

                        //change status then send back
                        message.setStatus(Message.Status.success);
                        message.setMsg(String.format("成功加入第 %s 號房間", data.get("roomId").getAsString()));
                        send(message);

                    } else {

                        message.setStatus(Message.Status.failure);
                        message.setMsg(String.format("加入第 %s 號房間失敗！原因：房間不存在！", data.get("roomId").getAsString()));
                        send(message);

                    }

                }
                break;
        }
    }

    public void changeLoginStatus(boolean isLogin, String connName, User user) {
        connectionName = connName;
        logger.setAccount(connectionName);
        this.isLogin = isLogin;
        this.user = user;
        if (isLogin) {
            logger.log("change connection status to login");
            userConnectionObserver.onLogin(this);
        } else {
            logger.log("change connection status to logout");
            userConnectionObserver.onLogout(this);
        }
    }

    public String getMD5(String str) {
        String ret = null;
        try {
            // 生成一個MD5加密計算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 計算md5函數
            md.update(str.getBytes());
            // digest()最後確定返回md5 hash值，返回值為8為字符串。因為md5 hash值是16位的hex值，實際上就是8位的字符
            // BigInteger函數則將8位的字符串轉換成16位hex值，用字符串來表示；得到字符串形式的hash值
            ret = new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            //throw new SpeedException("MD5加密出現錯誤");
            e.printStackTrace();
        }
        return ret;
    }

    public void send(String data) {
        logger.log(String.format("Sending a message '%s'", data));
        try {
            output.writeObject(data);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Message message) {
        String data = Message.makeMessageString(message);
        logger.log(String.format("Sending a message '%s'", data));
        try {
            output.writeObject(data);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

}
