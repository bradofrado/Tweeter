package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cs204.server.service.StatusService;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

public class PostStatusHandler implements RequestHandler<PostStatusRequest, PostStatusResponse> {
    @Override
    public PostStatusResponse handleRequest(PostStatusRequest input, Context context) {
        StatusService statusService = new StatusService();
        return statusService.postStatus(input);
    }
}
