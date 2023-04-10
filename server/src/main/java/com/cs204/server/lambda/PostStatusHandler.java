package com.cs204.server.lambda;

import com.cs204.server.SQS.PostStatusQueue;
import com.cs204.server.service.StatusService;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

public class PostStatusHandler extends BaseHandler<PostStatusRequest, PostStatusResponse> {
    @Override
    public PostStatusResponse handleRequest(PostStatusRequest input) {
        PostStatusResponse response = getInstance(StatusService.class).postStory(input);

        if (response.isSuccess()) {
            //Put story into SQSPostStatusQueue
            PostStatusQueue postStatusQueue = getInstance(PostStatusQueue.class);
            postStatusQueue.sendMessage(input.getStatus());
        }

        return response;
    }
}
