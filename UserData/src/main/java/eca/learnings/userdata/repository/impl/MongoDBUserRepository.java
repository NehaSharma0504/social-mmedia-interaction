package eca.learnings.userdata.repository.impl;

import eca.learnings.userdata.model.User;
import eca.learnings.userdata.repository.UserRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MongoDBUserRepository implements UserRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public User findById(String id, String collectionName) {
        Query query = Query.query(Criteria.where("userId").is(id));
        return mongoTemplate.findOne(query, User.class, collectionName);
    }

    @Override
    public void deleteMany(String collectionName) {
        mongoTemplate.getCollection(collectionName).deleteMany(new Document());
    }
}

