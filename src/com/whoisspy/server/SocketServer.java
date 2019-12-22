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

    private Map<String, UserConnection> lobbyClients = new HashMap<>();
    private List<UserConnection> noLoginConnections = Collections.synchronizedList(new LinkedList<>());

    private RoomsManager roomsManager;

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
        initRoomsManager();
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

    public void initRoomsManager() {
        roomsManager = new RoomsManager();
    }

    @Override
    public void run() {
        logger.log(String.format("Start server on %s", serverSocket.getLocalSocketAddress().toString()));
        while (true) {
            try {
                // connection Socket
                Socket connSocket = serverSocket.accept();
                UserConnection connection = new UserConnection(connSocket, userConnectionObserver);
                connection.addCollection("users", usersCollection);
                connection.addCollection("words", wordsCollection);
                connection.start();
                logger.log(String.format("Create a user connection from %s", connSocket.getRemoteSocketAddress().toString()));
                noLoginConnections.add(connection);
            } catch (IOException x) {
                x.printStackTrace();
            }
        }
    }

    public UserConnectionObserver userConnectionObserver = new UserConnectionObserver() {

        @Override
        public boolean onCreateRoom(UserConnection roomOwner,
                                    String roomName,
                                    int roomClientAmount,
                                    String roomDescription,
                                    boolean roomPrivate) {


            return false;
        }

        @Override
        public boolean onJoinRoom(UserConnection roomPlayer, int roomId) {
            return roomsManager.joinRoom(roomPlayer, roomId);
        }

        @Override
        public boolean onDeleteRoom(UserConnection roomOwner, int roomId) {
            return false;
        }

        @Override
        public boolean onLeaveRoom(UserConnection roomPlayer) {
            return false;
        }

        @Override
        public boolean onChangeRoomOwner(UserConnection roomOwner, UserConnection roomNewOwner) {
            return false;
        }

        @Override
        public void onLogin(UserConnection userConn) {
            if (noLoginConnections.contains(userConn)) {
                noLoginConnections.remove(userConn);
                lobbyClients.put(userConn.getUser().getAccount(), userConn);
            }
        }

        @Override
        public void onLogout(UserConnection userConn) {
            if (lobbyClients.containsKey(userConn.getUser().getAccount())) {
                lobbyClients.remove(userConn.getUser().getAccount());
                noLoginConnections.add(userConn);
            } else if (!noLoginConnections.contains(userConn)) {
                roomsManager.forceLeaveRoom(userConn.getUser().getAccount());
            }
        }

        @Override
        public boolean loginStatus(UserConnection userConn) {
            // if login return true
            // else     return false
            return !noLoginConnections.contains(userConn);
        }
    };


}
