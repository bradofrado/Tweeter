package com.cs204.server.lambda;

import com.cs204.server.service.StatusService;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;

public class GetFeedHandler extends BaseHandler<FeedRequest, FeedResponse> {
    @Override
    public FeedResponse handleRequest(FeedRequest input) {
        return getInstance(StatusService.class).getFeed(input);
    }
}
