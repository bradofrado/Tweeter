package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cs204.server.service.FollowService;

import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;

public class GetFollowersCount implements RequestHandler<FollowersCountRequest, FollowersCountResponse> {
    @Override
    public FollowersCountResponse handleRequest(FollowersCountRequest input, Context context) {
        FollowService followService = new FollowService();
        return followService.getFollowersCount(input);
    }
}
