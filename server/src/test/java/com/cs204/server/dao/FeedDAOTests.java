package com.cs204.server.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cs204.server.dao.dynamo.FeedDynamoDAO;
import com.cs204.server.dao.dynamo.FollowDynamoDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

@DisplayName("Feed Dynamo DAO tests")
public class FeedDAOTests {
    private FollowDAO followDAO;
    List<User> users;
    private FakeData fakeData = FakeData.getInstance();

    @BeforeEach
    public void setup() {
        followDAO = new FollowDynamoDAO();
        users = fakeData.getFakeUsers();
    }

    @Test
    @DisplayName("Should have follower after setting follower")
    public void should_have_followerAfterSettingFollower() {
        User user1 = users.get(0);
        User user2 = users.get(1);
        followDAO.setFollower(user1.getName(), user1.getAlias(), user2.getName(), user2.getAlias());
        assertTrue(followDAO.hasFollower(user1.getAlias(), user2.getAlias()));
    }

    @Test
    @DisplayName("Should return page of followers")
    public void should_return_pageOfFollowers() {
        DataPage<String> page = followDAO.getPageOfFollowers("@StantheMan", 10, null);
        assertTrue(page.isHasMorePages());
    }
}
