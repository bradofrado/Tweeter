package com.cs204.server.SQS;

public class UpdateFeedQueue extends SQSMessager {
    private static final String QUEUE_NAME = "UpdateFeedQueue";
    public UpdateFeedQueue() {
        super(QUEUE_NAME);
    }
}
