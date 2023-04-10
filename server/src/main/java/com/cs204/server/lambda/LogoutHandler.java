package com.cs204.server.lambda;

import com.cs204.server.service.UserService;

import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

public class LogoutHandler extends BaseHandler<LogoutRequest, LogoutResponse> {
    @Override
    public LogoutResponse handleRequest(LogoutRequest input) {
        return getInstance(UserService.class).logout(input);
    }
}
