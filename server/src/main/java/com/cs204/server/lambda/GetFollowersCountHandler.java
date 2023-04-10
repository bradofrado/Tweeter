package com.cs204.server.lambda;

import com.cs204.server.service.FollowService;

import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;

public class GetFollowersCountHandler extends BaseHandler<FollowersCountRequest, FollowersCountResponse> {
    @Override
    public FollowersCountResponse handleRequest(FollowersCountRequest input) {
        return getInstance(FollowService.class).getFollowersCount(input);
    }
}
