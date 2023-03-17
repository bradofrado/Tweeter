package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;

public class IsFollowerResponse extends Response {
    private boolean isFollower;

    public IsFollowerResponse(boolean isFollower) {
        super(true);
        this.isFollower = isFollower;
    }

    public IsFollowerResponse(String message) {
        super(false, message);
    }


    public boolean isFollower() {
        return isFollower;
    }

    public void setFollower(boolean follower) {
        isFollower = follower;
    }
}
