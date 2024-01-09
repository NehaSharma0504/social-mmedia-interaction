package eca.learnings.socialmedia.interaction.dao;

import com.mongodb.MongoClientSettings;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@Component
public class SocialMediaDao {
    private String collectionName;

    private MongoCollection<Document> collection;
    private MongoDatabase database;
    private MongoClient mongoClient;


    @Autowired
    public SocialMediaDao(@Value("${spring.data.mongodb.uri}") String mongoUri, @Value("${collection.name}") String collectionName) {
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
          // Connect to the MongoDB database using the provided URI
            mongoClient = MongoClients.create(mongoUri);
            database = mongoClient.getDatabase("MyMongoDB").withCodecRegistry(pojoCodecRegistry);
            collection = database.getCollection(collectionName);
    }

    public <T> void saveData(Document data, String collectionName) {
        // Get the appropriate collection based on the collectionName parameter
        MongoCollection<Document> targetCollection = database.getCollection(collectionName);
        // Insert the document into the target collection
        targetCollection.insertOne(data);
    }

    public  <T> List<? super T> getPosts(String collectionName, Class<T> type) {
        List<? super T> data = new ArrayList<>();
        // Get the collection with the custom CodecRegistry
        MongoCollection<T> collection = database.getCollection(collectionName, type);

        return collection.find().into(data);
    }

    public  <T> List<? super T> getUser(String collectionName, Class<T> type) {
        List<? super T> data = new ArrayList<>();
        // Get the collection with the custom CodecRegistry
        MongoCollection<T> collection = database.getCollection(collectionName, type);

        return collection.find().into(data);
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }
    public void closeConnection() {
        // Close the MongoDB client connection when it's no longer needed
        if (mongoClient != null) {
            mongoClient.close();
        }
    }// You can add more methods to the DAO class to support other database operations as needed.
}

