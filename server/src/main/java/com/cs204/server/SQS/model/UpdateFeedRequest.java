package com.cs204.server.SQS.model;

import com.cs204.server.dao.DataPage;

import edu.byu.cs.tweeter.model.domain.Status;

public class UpdateFeedRequest {
    private DataPage<String> followers;
    private Status status;

    public UpdateFeedRequest(DataPage<String> followers, Status status) {
        this.followers = followers;
        this.status = status;
    }

    private UpdateFeedRequest() {}


    public DataPage<String> getFollowers() {
        return followers;
    }

    public void setFollowers(DataPage<String> followers) {
        this.followers = followers;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
