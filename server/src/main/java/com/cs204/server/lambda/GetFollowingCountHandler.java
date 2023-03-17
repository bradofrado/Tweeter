package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cs204.server.service.FollowService;

import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

public class GetFollowingCountHandler implements RequestHandler<FollowingCountRequest, FollowingCountResponse> {
    @Override
    public FollowingCountResponse handleRequest(FollowingCountRequest input, Context context) {
        FollowService followService = new FollowService();
        return followService.getFollowingCount(input);
    }
}
