package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cs204.server.dao.FeedDAO;
import com.cs204.server.service.StatusService;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class UpdateFeeds implements BaseHandler {
    @Override
    protected Object handleRequest(Object request) {
        //Get the batch of followers
        List<String> statuses = null;
        ((StatusService)getInstance(StatusService.class)).postFeed("user", statuses);

        return null;
    }
}
