package com.cs204.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
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

import com.cs204.server.dao.AuthTokenDAO;
import com.cs204.server.dao.DataPage;
import com.cs204.server.dao.FollowDAO;
import com.cs204.server.dao.UserDAO;
import com.cs204.server.dao.dynamo.FollowDynamoDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends AuthenticatedService {
    private FollowDAO followDAO;

    @Inject
    public FollowService(FollowDAO followDAO, UserDAO userDAO, AuthTokenDAO authTokenDAO) {
        super(authTokenDAO, userDAO);
        this.followDAO = followDAO;
    }
    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDynamoDAO} to
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
        } else if (request.getAuthToken() == null || request.getAuthToken().getToken().length() == 0) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        }

        getAuthenticatedUser(request.getAuthToken());
        DataPage<String> followees = followDAO.getPageOfFollowees(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias());
        List<User> users = new ArrayList<>();
        for (String followeeAlias : followees.getValues()) {
            User user = userDAO.getUser(followeeAlias);
            users.add(user);
        }

        return new FollowingResponse(users, followees.isHasMorePages());
    }

    public FollowerResponse getFollowers(FollowerRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null || request.getAuthToken().getToken().length() == 0) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        }

        getAuthenticatedUser(request.getAuthToken());
        DataPage<String> followers = getFollowers(request.getFollowerAlias(), request.getLimit(), request.getLastFollower());
        List<User> users = new ArrayList<>();
        for (String followerAlias : followers.getValues()) {
            User user = userDAO.getUser(followerAlias);
            users.add(user);
        }

        return new FollowerResponse(users, followers.isHasMorePages());
    }

    public DataPage<String> getFollowers(String followerAlias, int limit, String lastFollower) {
        return followDAO.getPageOfFollowers(followerAlias, limit, lastFollower);
    }

    /**
     * Makes the authenticated user follow the given followerAlias
     * @param request
     * @return
     */
    public FollowResponse follow(FollowRequest request) {
        if (request.getAuthToken() == null || request.getAuthToken().getToken().length() == 0) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        User user = getAuthenticatedUser(request.getAuthToken());
        User follower = userDAO.getUser(request.getFolloweeAlias());
        if (follower == null) {
            throw new RuntimeException("[Bad Request] Invalid follower alias " + request.getFolloweeAlias());
        }
        followDAO.setFollower(follower.getName(), follower.getAlias(), user.getName(), user.getAlias());
        userDAO.setFolloweeCount(user.getAlias(), userDAO.getFolloweeCount(user.getAlias()) + 1);
        userDAO.setFollowerCount(follower.getAlias(), userDAO.getFollowerCount(follower.getAlias()) + 1);

        return new FollowResponse();
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if (request.getAuthToken() == null || request.getAuthToken().getToken().length() == 0) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        User user = getAuthenticatedUser(request.getAuthToken());
        User follower = userDAO.getUser(request.getFolloweeAlias());
        if (follower == null) {
            throw new RuntimeException("[Bad Request] Invalid followee alias " + request.getFolloweeAlias());
        }
        followDAO.deleteFollower(follower.getAlias(), user.getAlias());
        userDAO.setFolloweeCount(user.getAlias(), userDAO.getFolloweeCount(user.getAlias()) - 1);
        userDAO.setFollowerCount(follower.getAlias(), userDAO.getFollowerCount(follower.getAlias()) - 1);

        return new UnfollowResponse();
    }

    /**
     * Checks whether or not the given follower follows the given followee
     * @param request
     * @return
     */
    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if (request.getAuthToken() == null || request.getAuthToken().getToken().length() == 0) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }
        getAuthenticatedUser(request.getAuthToken());
        boolean isFollower = followDAO.hasFollower(request.getFolloweeAlias(), request.getFollowerAlias());

        return new IsFollowerResponse(isFollower);
    }

    public FollowersCountResponse getFollowersCount(FollowersCountRequest request) {
        if (request.getAuthToken() == null || request.getAuthToken().getToken().length() == 0) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        } else if (request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        }

        getAuthenticatedUser(request.getAuthToken());
        int count = userDAO.getFollowerCount(request.getTargetUser());

        return new FollowersCountResponse(count);
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        if (request.getAuthToken() == null || request.getAuthToken().getToken().length() == 0) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        } else if (request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        }

        getAuthenticatedUser(request.getAuthToken());
        int count = userDAO.getFolloweeCount(request.getTargetUser());

        return new FollowingCountResponse(count);
    }
}