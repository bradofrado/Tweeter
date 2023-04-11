package com.cs204.server.module;

import com.cs204.server.dao.AuthTokenDAO;
import com.cs204.server.dao.FeedDAO;
import com.cs204.server.dao.FollowDAO;
import com.cs204.server.dao.ImageDAO;
import com.cs204.server.dao.StoryDAO;
import com.cs204.server.dao.UserDAO;
import com.cs204.server.dao.dynamo.AuthTokenDynamoDAO;
import com.cs204.server.dao.dynamo.FeedDynamoDAO;
import com.cs204.server.dao.dynamo.FollowDynamoDAO;
import com.cs204.server.dao.dynamo.StoryDynamoDAO;
import com.cs204.server.dao.dynamo.UserDynamoDAO;
import com.cs204.server.dao.s3.ImageS3DAO;
import com.cs204.server.service.FollowService;
import com.cs204.server.service.StatusService;
import com.cs204.server.service.UserService;
import com.cs204.server.util.Filler;
import com.cs204.server.util.HashingUtil;
import com.google.inject.AbstractModule;

import java.util.Date;
import java.util.List;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FollowService.class).asEagerSingleton();
        bind(UserService.class).asEagerSingleton();
        bind(StatusService.class).asEagerSingleton();
        bind(UserDAO.class).to(UserDynamoDAO.class);
        bind(FollowDAO.class).to(FollowDynamoDAO.class);
        bind(AuthTokenDAO.class).to(AuthTokenDynamoDAO.class);
        bind(FeedDAO.class).to(FeedDynamoDAO.class);
        bind(StoryDAO.class).to(StoryDynamoDAO.class);
        bind(ImageDAO.class).to(ImageS3DAO.class);
    }

    public static void main(String[] args) {
//        FollowDAO followDAO = new FollowDynamoDAO();
//        StoryDAO storyDAO = new StoryDynamoDAO();
//        UserDAO userDAO = new UserDynamoDAO();
//        FeedDAO feedDAO = new FeedDynamoDAO();
//        List<User> users = getFakeData().getFakeUsers();
//        for (User user : users) {
//            for (User user1: users) {
//                if (user != user1)
//                    followDAO.setFollower(user.getName(), user.getAlias(), user1.getName(), user1.getAlias());
//            }
//            userDAO.setUser(user.getAlias(), user.getFirstName(), user.getLastName(), user.getImageUrl(), HashingUtil.hash("Random"));
//            userDAO.setFolloweeCount(user.getAlias(), 20);
//            userDAO.setFollowerCount(user.getAlias(), 20);
//        }
//
//        try {
//            List<Status> statuses = getFakeData().getFakeStatuses();
//            for (Status status : statuses) {
//                for (User user : users) {
//                    if (user.getAlias().equals(status.getUser().getAlias())) continue;
//                    long time = System.currentTimeMillis();
//                    time /= 1000;
//                    time *= 1000;
//                    feedDAO.setFeed(user.getAlias(), status.getPost(), status.getUser().getAlias(), time, status.getUrls(), status.getMentions());
//                }
//                //storyDAO.setStory(status.getPost(), status.getUser().getAlias(), System.currentTimeMillis(), status.getUrls(), status.getMentions());
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        Filler.fillDatabase();
    }

    static FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
