package com.cs204.server.SQS;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import edu.byu.cs.tweeter.util.JsonSerializer;

public class SQSMessager {
    private static final String BASE_URL = "https://sqs.us-west-2.amazonaws.com/363642605830/";
    private String queueName;

    public SQSMessager(String queueName) {
        this.queueName = queueName;
    }

    private AmazonSQS client = null;
    private AmazonSQS getClient() {
        if (client == null) {
            client = AmazonSQSClientBuilder.defaultClient();
        }

        return client;
    }

    private String getUrl() {
        return BASE_URL + queueName;
    }

    public String sendMessage(String message) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(getUrl())
                .withMessageBody(message);

        AmazonSQS sqs = getClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

        String msgId = send_msg_result.getMessageId();

        return msgId;
    }

    public <T> String sendMessage(T obj) {
        String body = JsonSerializer.serialize(obj);

        return sendMessage(body);
    }
}
