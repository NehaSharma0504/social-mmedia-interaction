package eca.learnings.userdata.repository;


import eca.learnings.userdata.model.User;

public interface UserRepository  {
    User findById(String id, String collectionName);
    void deleteMany(String collectionName);
}
