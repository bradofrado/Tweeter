package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.cs204.server.SQS.UpdateFeedQueue;
import com.cs204.server.SQS.model.UpdateFeedRequest;
import com.cs204.server.dao.DataPage;
import com.cs204.server.module.MainModule;
import com.cs204.server.service.FollowService;
import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.util.JsonSerializer;

public class PostUpdateFeedMessages implements RequestHandler<SQSEvent, Void> {
    private static final int BATCH_SIZE = 10;
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        FollowService followService = getInstance(FollowService.class);
        UpdateFeedQueue updateFeedQueue = getInstance(UpdateFeedQueue.class);

        //Get status in message queue
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            Status status = JsonSerializer.deserialize(msg.getBody(), Status.class);
            String targetUser = status.getUser().getAlias();

            boolean hasMorePages = true;
            String lastFollower = null;
            while (hasMorePages) {
                DataPage<String> followers = followService.getFollowers(targetUser, BATCH_SIZE, lastFollower);
                UpdateFeedRequest request = new UpdateFeedRequest(followers.getValues(), status);
                updateFeedQueue.sendMessage(request);

                lastFollower = followers.getValues().get(followers.getValues().size()  - 1);
                hasMorePages = followers.isHasMorePages();
            }
        }

        return null;
    }

    private static Injector injector;
    private static Injector getInjector() {
        if (injector == null) {
            injector = Guice.createInjector(new MainModule());
        }

        return injector;
    }

    protected <T> T getInstance(Class<T> type) {
        Injector injector = getInjector();
        return injector.getInstance(type);
    }
}
