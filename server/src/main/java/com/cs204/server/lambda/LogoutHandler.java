package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cs204.server.service.UserService;

import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

public class LogoutHandler implements RequestHandler<LogoutRequest, LogoutResponse> {
    @Override
    public LogoutResponse handleRequest(LogoutRequest input, Context context) {
        UserService userService = new UserService();
        return userService.logout(input);
    }
}
