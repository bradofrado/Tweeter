package com.cs204.server.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cs204.server.dao.dynamo.UserDynamoDAO;
import com.cs204.server.util.HashingUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.User;

@DisplayName("User Dynamo DAO Test")
public class UserDAOTest {
    private UserDAO userDAO;
    private String password = "1234password";
    User user;
    @BeforeEach
    public void setup() {
        userDAO = new UserDynamoDAO();
        user = new User("Braydon", "Jones", "@brado", "www.google.com");
        String passwordHash = HashingUtil.hash(password);
        userDAO.setUser(user.getAlias(), user.getFirstName(), user.getLastName(), user.getImageUrl(), passwordHash);
    }

    @AfterEach
    public void cleanup() {
        userDAO.deleteUser(user.getAlias());
    }

    @Test
    @DisplayName("Should succeed when get user by alias and password")
    public void should_succeed_whenGetUser() {
        assertEquals(user, userDAO.getUser(user.getAlias()));
        assertEquals(user, userDAO.getUser(user.getAlias(), HashingUtil.hash(password)));
    }
}
