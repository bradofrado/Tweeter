package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class StoryResponse extends Response {
    private boolean hasMorePages;
    private List<Status> statuses;

    public StoryResponse(List<Status> statuses, boolean hasMorePages) {
        super(true);
        this.statuses = statuses;
        this.hasMorePages = hasMorePages;
    }
    public StoryResponse(String message) {
        super(false, message);
    }


    public boolean isHasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }
}
