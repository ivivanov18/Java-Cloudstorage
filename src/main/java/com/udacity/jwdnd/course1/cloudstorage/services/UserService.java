package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(HashService hashService, UserMapper userMapper) {
        this.hashService = hashService;
        this.userMapper = userMapper;

    }

    public boolean isUserNameAvailable(String userName) {
        return userMapper.getUser(userName) == null;
    }

    public User getUser(String userName) {
        return userMapper.getUser(userName);
    }

    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.insert(new User(null, user.getUserName(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
    }
}
