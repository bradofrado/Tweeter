package com.cs204.server.lambda;

import com.cs204.server.service.StatusService;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

public class PostStatusHandler extends BaseHandler<PostStatusRequest, PostStatusResponse> {
    @Override
    public PostStatusResponse handleRequest(PostStatusRequest input) {
        PostStatusResponse response = getInstance(StatusService.class).postStory(input);
        //Put story into SQSPostStatusQueue
//        String messageBody = "This is a message coming from the sqs client";
//        String queueUrl = "https://sqs.us-west-2.amazonaws.com/051251608764/CS340Queue";
//
//        SendMessageRequest send_msg_request = new SendMessageRequest()
//                .withQueueUrl(queueUrl)
//                .withMessageBody(messageBody);
//
//        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
//        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);
//
//        String msgId = send_msg_result.getMessageId();
//        System.out.println("Message ID: " + msgId);

        return response;
    }
}
