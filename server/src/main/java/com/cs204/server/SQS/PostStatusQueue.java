package com.cs204.server.SQS;

public class PostStatusQueue extends SQSMessager {
    private static final String QUEUE_NAME = "PostStatusQueue";
    public PostStatusQueue() {
        super(QUEUE_NAME);
    }
}
