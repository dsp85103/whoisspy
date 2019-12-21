package com.whoisspy.server;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.whoisspy.Logger;
import org.bson.Document;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class SocketServer extends Thread {

    private Map<Integer, Room> rooms = new HashMap<>();
    private Map<String, UserConnection> lobbyClients = new HashMap<>();
    private List<UserConnection> noLoginConnections = Collections.synchronizedList(new LinkedList<>());

    private String tag = "Server";
    private String account = "socket";
    ServerSocket serverSocket;
    private Logger logger;
    private MongoDBHelper mongoDBHelper;
    private MongoCollection<Document> usersCollection;
    private MongoCollection<Document> wordsCollection;

    public SocketServer(int port) {
        logger = new Logger(tag, account);
        initMongoDB();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    private void initMongoDB() {
        mongoDBHelper = new MongoDBHelper("127.0.0.1", 27017, "whoisspy");
        // 如果有增加 collection 要記得給 user connection 增加 collection
        usersCollection = mongoDBHelper.getCollection("users");
        wordsCollection = mongoDBHelper.getCollection("words");
    }

    @Override
    public void run() {
        logger.log(String.format("Start server on %s", serverSocket.getLocalSocketAddress().toString()));
        while (true) {
            try {
                // connection Socket
                Socket connSocket = serverSocket.accept();
                UserConnection connection = new UserConnection(connSocket);
                connection.addCollection("users", usersCollection);
                connection.addCollection("words", wordsCollection);
                connection.setNoLoginUsersList(noLoginConnections);
                connection.setLobbyClientsMap(lobbyClients);
                connection.setRooms(rooms);
                connection.start();
                logger.log(String.format("Create a user connection from %s", connSocket.getRemoteSocketAddress().toString()));
                noLoginConnections.add(connection);
            } catch (IOException x) {
                x.printStackTrace();
            }
        }
    }

}
