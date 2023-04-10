package com.cs204.server.lambda;

import com.cs204.server.service.FollowService;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;

public class IsFollowerHandler extends BaseHandler<IsFollowerRequest, IsFollowerResponse> {
    @Override
    public IsFollowerResponse handleRequest(IsFollowerRequest input) {
        return getInstance(FollowService.class).isFollower(input);
    }
}
