package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cs204.server.service.StatusService;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;

public class GetFeedHandler implements RequestHandler<FeedRequest, FeedResponse> {
    @Override
    public FeedResponse handleRequest(FeedRequest input, Context context) {
        StatusService statusService = new StatusService();

        return statusService.getFeed(input);
    }
}
