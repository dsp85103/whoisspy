package com.whoisspy.server;

import com.whoisspy.Logger;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserConnection extends Thread {

    private String TAG = "Server";
    private String account = "UserConnection";
    private String email;
    private Socket socket;
    private boolean isLogin = false;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Logger logger;
    public UserConnection(Socket socket) {
        super();
        this.socket = socket;

        logger = new Logger(TAG,account);
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException x) {
            x.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            while (true) {
                String recvMessage = (String) input.readObject();  //blocking
                logger.log(String.format("Receiving message '%s'", recvMessage));
                if (recvMessage.equals("exit") || recvMessage.equals("quit"))
                {
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

        // TODO 如果登入了 設定 isLogin=true, logger.setAccount(account)
    }

    public void send(String data) {
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
