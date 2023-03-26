package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingCountRequest {
    private AuthToken authToken;
    private String targetUser;

    public FollowingCountRequest(AuthToken authToken, String targetUser) {
        this.authToken = authToken;
        this.targetUser = targetUser;
    }

    private FollowingCountRequest() {}

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }
}
