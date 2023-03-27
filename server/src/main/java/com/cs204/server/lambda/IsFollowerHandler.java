package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cs204.server.service.FollowService;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;

public class IsFollowerHandler implements RequestHandler<IsFollowerRequest, IsFollowerResponse> {
    @Override
    public IsFollowerResponse handleRequest(IsFollowerRequest input, Context context) {
        FollowService followService = new FollowService(null, null);
        return followService.isFollower(input);
    }
}
