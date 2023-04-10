package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class PostUpdateFeedMessages implements RequestHandler {
    @Override
    public Object handleRequest(Object input, Context context) {
        //Get status in message queue
        //loop:
        // Get page of followers
        // send DataPage of followers and status to SQS update feed queue
        // repeat until no more followers

        return null;
    }
}
