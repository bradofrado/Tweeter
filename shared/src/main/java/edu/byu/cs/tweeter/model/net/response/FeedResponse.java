package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class FeedResponse extends Response {
    private boolean hasMorePages;
    private List<Status> statuses;

    public FeedResponse(List<Status> statuses, boolean hasMorePages) {
        super(true);
        this.hasMorePages = hasMorePages;
        this.statuses = statuses;
    }

    public FeedResponse(String message) {
        super(false, message);
    }
}
