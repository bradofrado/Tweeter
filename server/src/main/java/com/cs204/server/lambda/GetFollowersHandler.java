package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cs204.server.service.FollowService;

import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;

public class GetFollowersHandler implements RequestHandler<FollowerRequest, FollowerResponse> {
    @Override
    public FollowerResponse handleRequest(FollowerRequest input, Context context) {
        FollowService followService = new FollowService(null, null);
        return followService.getFollowers(input);
    }
}
