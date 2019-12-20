package com.whoisspy.server;

public class Server {

    private SocketServer socketServer;

    public Server() {
        socketServer = new SocketServer(15566);
        socketServer.start();
    }


}
