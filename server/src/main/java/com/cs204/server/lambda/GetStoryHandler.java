package com.cs204.server.lambda;

import com.cs204.server.service.StatusService;

import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

public class GetStoryHandler extends BaseHandler<StoryRequest, StoryResponse> {
    @Override
    public StoryResponse handleRequest(StoryRequest input) {
        return getInstance(StatusService.class).getStory(input);
    }
}
