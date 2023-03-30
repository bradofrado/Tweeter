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
import com.google.inject.AbstractModule;

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
}
