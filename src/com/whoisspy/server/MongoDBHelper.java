package com.whoisspy.server;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBHelper {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public MongoDBHelper(String host, int port, String db) {

        mongoClient = new com.mongodb.MongoClient(host,port);
        mongoDatabase = mongoClient.getDatabase(db);

    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return mongoDatabase.getCollection(collectionName);
    }

}
