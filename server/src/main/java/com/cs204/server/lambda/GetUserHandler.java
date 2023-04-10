package com.cs204.server.lambda;

import com.cs204.server.service.UserService;

import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserResponse;

public class GetUserHandler extends BaseHandler<UserRequest, UserResponse> {
    @Override
    public UserResponse handleRequest(UserRequest input) {
        return getInstance(UserService.class).getUser(input);
    }
}
