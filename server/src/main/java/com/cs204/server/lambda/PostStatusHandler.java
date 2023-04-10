package com.cs204.server.lambda;

import com.cs204.server.service.StatusService;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

public class PostStatusHandler extends BaseHandler<PostStatusRequest, PostStatusResponse> {
    @Override
    public PostStatusResponse handleRequest(PostStatusRequest input) {
        return getInstance(StatusService.class).postStatus(input);
    }
}
