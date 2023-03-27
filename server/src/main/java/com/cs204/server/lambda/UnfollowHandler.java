package com.cs204.server.lambda;

import com.cs204.server.service.FollowService;

import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

public class UnfollowHandler extends BaseHandler<UnfollowRequest, UnfollowResponse> {
    @Override
    public UnfollowResponse handleRequest(UnfollowRequest input) {
       return getInstance(FollowService.class).unfollow(input);
    }
}
