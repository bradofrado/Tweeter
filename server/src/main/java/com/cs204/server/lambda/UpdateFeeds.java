package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.cs204.server.SQS.model.UpdateFeedRequest;
import com.cs204.server.dao.FeedDAO;
import com.cs204.server.module.MainModule;
import com.cs204.server.service.StatusService;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.util.JsonSerializer;

public class UpdateFeeds implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        StatusService statusService = getInstance(StatusService.class);
        //Get the batch of followers
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            UpdateFeedRequest request = JsonSerializer.deserialize(msg.getBody(), UpdateFeedRequest.class);
            statusService.postFeed(request.getStatus(), request.getFollowers());
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
