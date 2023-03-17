package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerRequest {
    private AuthToken authToken;
    private String followerAlias;
    private int limit;
    private String lastFollower;

    public FollowerRequest(AuthToken authToken, String targetUser, int limit, String lastFollower) {
        this.authToken = authToken;
        this.followerAlias = targetUser;
        this.limit = limit;
        this.lastFollower = lastFollower;
    }


    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getFollowerAlias() {
        return followerAlias;
    }

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getLastFollower() {
        return lastFollower;
    }

    public void setLastFollower(String lastFollower) {
        this.lastFollower = lastFollower;
    }
}
