package com.cs204.server.service;

import com.cs204.server.dao.dynamo.AuthTokenDynamoDAO;
import com.cs204.server.dao.dynamo.FeedDynamoDAO;
import com.cs204.server.dao.dynamo.FollowDynamoDAO;
import com.cs204.server.dao.dynamo.StoryDynamoDAO;
import com.cs204.server.dao.dynamo.UserDynamoDAO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

@DisplayName("Status service tests")
public class StatusServiceTest {
    private AuthToken authToken;
    private User targetUser;
    private StatusService statusService;
    @BeforeEach
    public void setup() {
        authToken = new AuthToken("cf55b7ad-8257-4df3-9916-4b52880b5b82");
        targetUser = new User("@allen");
        statusService = new StatusService(new FeedDynamoDAO(), new StoryDynamoDAO(), new FollowDynamoDAO(), new UserDynamoDAO(), new AuthTokenDynamoDAO());
    }
    @Test
    @DisplayName("should return page of stories when given last status")
    public void should_returnPageOfStories_whenGivenLastStatus() {
        Status status = new Status();
        status.setDatetime("Fri Mar 31 22:58:24 UTC 2023");
        try {
            FeedRequest request = new FeedRequest(authToken, targetUser.getAlias(), 10, status);
            FeedResponse response = statusService.getFeed(request);
            Assertions.assertTrue(response.isSuccess());
            Assertions.assertEquals(10, response.getStatuses().size());
            Assertions.assertTrue(response.getHasMorePages());
        } catch (Exception ex) {
            Assertions.fail(ex.getMessage());
        }
    }
}

