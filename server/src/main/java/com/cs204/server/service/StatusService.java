package com.cs204.server.service;

import com.cs204.server.dao.AuthTokenDAO;
import com.cs204.server.dao.DataPage;
import com.cs204.server.dao.FeedDAO;
import com.cs204.server.dao.FollowDAO;
import com.cs204.server.dao.StoryDAO;
import com.cs204.server.dao.UserDAO;

import java.util.List;

import javax.inject.Inject;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService extends AuthenticatedService {
    private FeedDAO feedDAO;
    private StoryDAO storyDAO;
    private FollowDAO followDAO;

    @Inject
    public StatusService(FeedDAO feedDAO, StoryDAO storyDAO, FollowDAO followDAO, UserDAO userDAO, AuthTokenDAO authTokenDAO) {
        super(authTokenDAO, userDAO);
        this.feedDAO = feedDAO;
        this.storyDAO = storyDAO;
        this.followDAO = followDAO;
    }

    public FeedResponse getFeed(FeedRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null || request.getAuthToken().getToken().length() == 0) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        }

        String lastPosted = request.getLastStatus() != null ? request.getLastStatus().getUser().getAlias() : null;

        DataPage<Status> page = feedDAO.getPageOfFeeds(request.getTargetUser(), request.getLimit(), lastPosted);
        page.getValues().forEach(p -> {
            User user = userDAO.getUser(p.getUser().getAlias());
            p.setUser(user);
        });
        return new FeedResponse(page.getValues(), page.isHasMorePages());
    }

    public StoryResponse getStory(StoryRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null || request.getAuthToken().getToken().length() == 0) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        }

        String lastPosted = request.getLastStatus() != null ? request.getLastStatus().getUser().getAlias() : null;

        DataPage<Status> page = storyDAO.getPageOfStories(request.getTargetUser(), request.getLimit(), lastPosted);
        page.getValues().forEach(p -> {
            User user = userDAO.getUser(p.getUser().getAlias());
            p.setUser(user);
        });
        return new StoryResponse(page.getValues(), page.isHasMorePages());
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if(request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        } else if (request.getAuthToken() == null || request.getAuthToken().getToken().length() == 0) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        }

        int currentTime = getCurrentTime();
        Status status = request.getStatus();
        String targetUserAlias = status.getUser().getAlias();
        storyDAO.setStory(status.getPost(), targetUserAlias, currentTime, status.getUrls(), status.getMentions());
        boolean hasMorePages = true;
        String lastPoster = null;
        while (hasMorePages) {
            DataPage<String> followers = followDAO.getPageOfFollowers(targetUserAlias, 10, lastPoster);
            hasMorePages = followers.isHasMorePages();
            followers.getValues().forEach(f -> feedDAO.setFeed(status.getPost(), f, currentTime, status.getUrls(), status.getMentions()));
            lastPoster = followers.getValues().get(followers.getValues().size() - 1);
        }
        return new PostStatusResponse();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }

    private int getCurrentTime() {
        return (int)System.currentTimeMillis();
    }
}
