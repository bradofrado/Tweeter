package com.cs204.server.lambda;

import com.cs204.server.service.FollowService;

import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class GetFollowingHandler extends BaseHandler<FollowingRequest, FollowingResponse> {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    @Override
    public FollowingResponse handleRequest(FollowingRequest request) {
        return getInstance(FollowService.class).getFollowees(request);
    }
}