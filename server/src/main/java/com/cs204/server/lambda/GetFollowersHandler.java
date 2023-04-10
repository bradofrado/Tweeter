package com.cs204.server.lambda;

import com.cs204.server.service.FollowService;

import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;

public class GetFollowersHandler extends BaseHandler<FollowerRequest, FollowerResponse> {
    @Override
    public FollowerResponse handleRequest(FollowerRequest input) {
        return getInstance(FollowService.class).getFollowers(input);
    }
}
