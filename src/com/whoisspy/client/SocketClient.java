package com.whoisspy.client;

import com.whoisspy.Logger;
import com.whoisspy.Message;
import com.whoisspy.MessageObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SocketClient {

    private String host;
    private int port;
    private String tag = "Client";
    private String connectionName = "socket";
    private Socket socket;
    private Thread socketThread;
    private boolean isLogin = false;
    private Logger logger;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private MessageObserver messageObserver;

    public SocketClient(String host, int port, MessageObserver messageObserver) {

        this.messageObserver = messageObserver;
        socketThread = new Thread(() -> RunClinet(host, port));

        logger = new Logger(tag, connectionName);
        socketThread.start();

    }

    public void RunClinet(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            socket = new Socket(InetAddress.getByName(this.host), port);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());

            recv();

            // TODO Send 'test data'
//            send("test data");
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    public ObjectInputStream getInputStream() {
        return input;
    }

    public ObjectOutputStream getOutputStream() {
        return output;
    }

    private void recv() {
        new Thread(() -> {
            while(true) {
                try {
                    String message = (String)input.readObject();
                    messageObserver.OnMessage(message);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

}
