package com.example.repository;

import com.example.repository.entity.User;

import java.util.Optional;

public interface UserRepository {

    void save(User user);

    Optional<User> getByUserName(String username);

}
