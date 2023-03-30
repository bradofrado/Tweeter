package com.cs204.server.service;

import com.cs204.server.dao.AuthTokenDAO;
import com.cs204.server.dao.ImageDAO;
import com.cs204.server.dao.UserDAO;
import com.cs204.server.util.HashingUtil;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService {
    private static final int AUTH_TOKEN_TIMEOUT = 1000000;
    private UserDAO userDAO;
    private AuthTokenDAO authTokenDAO;
    private ImageDAO imageDAO;
    public UserService(UserDAO userDAO, AuthTokenDAO authTokenDAO, ImageDAO imageDAO) {
        this.userDAO = userDAO;
        this.authTokenDAO = authTokenDAO;
        this.imageDAO = imageDAO;
    }

    public LoginResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        User user = userDAO.getUser(request.getUsername(), HashingUtil.hash(request.getPassword()));
        if (user == null) {
            return new LoginResponse("Invalid Username or password");
        }
        AuthToken authToken = createAuthToken(user.getAlias());
        return new LoginResponse(user, authToken);
    }

    public RegisterResponse register(RegisterRequest request) {
        if (request.getUsername() == null) {
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        } else if (request.getFirstName() == null) {
            throw new RuntimeException("[Bad Request] Missing a first name");
        } else if (request.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing a last name");
        } else if (request.getImage() == null) {
            throw new RuntimeException("[Bad Request] Missing an image");
        }
        String imageUrl = imageDAO.uploadImage(request.getUsername(), request.getImage());
        userDAO.setUser(request.getUsername(), request.getFirstName(), request.getLastName(),
                imageUrl, HashingUtil.hash(request.getPassword()));
        User user = new User(request.getFirstName(), request.getLastName(), request.getUsername(), imageUrl);
        AuthToken authToken = createAuthToken(request.getUsername());
        return new RegisterResponse(user, authToken);
    }

    public LogoutResponse logout(LogoutRequest request) {
        if (request.getAuthToken() == null || request.getAuthToken().getToken().length() == 0) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        authTokenDAO.deleteAuthToken(request.getAuthToken());

        return new LogoutResponse();
    }

    public UserResponse getUser(UserRequest request) {
        if (request.getAlias() == null || request.getAlias().length() == 0) {
            throw new RuntimeException("[Bad Request] Missing an alias");
        } else if (request.getAuthToken() == null || request.getAuthToken().getToken().length() == 0) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        User user = userDAO.getUser(request.getAlias());

        if (user == null) {
            return new UserResponse("Could not find user " + request.getAlias());
        }

        return new UserResponse(user);
    }

    private AuthToken createAuthToken(String alias) {
        return authTokenDAO.setAuthToken(new AuthToken(java.util.UUID.randomUUID().toString()), alias,
                (int) (System.currentTimeMillis() + AUTH_TOKEN_TIMEOUT));
    }
}