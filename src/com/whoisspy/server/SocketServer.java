package com.whoisspy.server;

import com.whoisspy.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class SocketServer extends Thread {

    private Map<String, List<UserConnection>> rooms = new HashMap<>();
    private Map<String, UserConnection> lobbyClients = new HashMap<>();
    private List<UserConnection> noLoginConnections = Collections.synchronizedList(new LinkedList<>());

    private String tag = "Server";
    private String account = "socket";
    ServerSocket serverSocket;
    private Logger logger;

    public SocketServer(int port) {
        logger = new Logger(tag, account);

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    @Override
    public void run() {
        logger.log(String.format("Start server on %s", serverSocket.getLocalSocketAddress().toString()));
        while (true) {
            try {
                // connection Socket
                Socket connSocket = serverSocket.accept();
                UserConnection connection = new UserConnection(connSocket);
                connection.start();
                logger.log(String.format("Create a user connection from %s", connSocket.getRemoteSocketAddress().toString()));
                noLoginConnections.add(connection);
            } catch (IOException x) {
                x.printStackTrace();
            }
        }
    }
}
