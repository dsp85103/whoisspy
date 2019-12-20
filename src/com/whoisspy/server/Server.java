package com.whoisspy.server;

public class Server {

    private SocketServer socketServer;

    public Server(int port) {
        socketServer = new SocketServer(port);

    }

    public void start() {
        socketServer.start();
    }


}
