package com.cs204.server.service;

import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

import com.cs204.server.dao.FollowDAO;

import java.util.Random;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return getFollowingDAO().getFollowees(request);
    }

    public FollowerResponse getFollowers(FollowerRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        }

        return getFollowingDAO().getFollowers(request);
    }

    /**
     * Makes the authenticated user follow the given followerAlias
     * @param request
     * @return
     */
    public FollowResponse follow(FollowRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        return new FollowResponse();
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        return new UnfollowResponse();
    }

    /**
     * Checks whether or not the given follower follows the given followee
     * @param request
     * @return
     */
    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }
        boolean isFollower = new Random().nextInt() > 0;

        return new IsFollowerResponse(isFollower);
    }

    public FollowersCountResponse getFollowersCount(FollowersCountRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        } else if (request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        }

        int count = 20;

        return new FollowersCountResponse(count);
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        } else if (request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        }

        int count = 20;

        return new FollowingCountResponse(count);
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowingDAO() {
        return new FollowDAO();
    }
}