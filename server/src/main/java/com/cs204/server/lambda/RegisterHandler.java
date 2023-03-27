package com.cs204.server.lambda;

import com.cs204.server.service.UserService;

import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

/**
 * An AWS lambda function that registers a user and returns the user object and auth token
 */
public class RegisterHandler extends BaseHandler<RegisterRequest, RegisterResponse> {
    @Override
    public RegisterResponse handleRequest(RegisterRequest input) {
        return getInstance(UserService.class).register(input);
    }
}
