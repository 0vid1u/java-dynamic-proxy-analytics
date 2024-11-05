package io.example.fixtures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public void login(String username) {
        log.info("User logged in: {}", username);
    }

    @Override
    public void logout(String username) {
        log.info("User logged out: {}", username);
    }

    @Override
    public User getProfile(String userId) {
        return new User(userId, "John Doe");
    }
}
