package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cs204.server.service.StatusService;

import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

public class GetStoryHandler implements RequestHandler<StoryRequest, StoryResponse> {
    @Override
    public StoryResponse handleRequest(StoryRequest input, Context context) {
        StatusService statusService = new StatusService();
        return statusService.getStory(input);
    }
}
