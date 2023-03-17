package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cs204.server.service.UserService;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

/**
 * An AWS lambda function that logs a user in and returns the user object and an auth code for
 * a successful login.
 */
public class LoginHandler implements RequestHandler<LoginRequest, LoginResponse> {
    @Override
    public LoginResponse handleRequest(LoginRequest loginRequest, Context context) {
        UserService userService = new UserService();
        return userService.login(loginRequest);
    }
}