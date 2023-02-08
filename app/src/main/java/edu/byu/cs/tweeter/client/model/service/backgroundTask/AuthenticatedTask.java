package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class AuthenticatedTask extends  BackgroundTask {
    /**
     * Auth token for logged-in user.
     * This user is the "follower" in the relationship.
     */
    protected AuthToken authToken;

    public AuthenticatedTask(AuthToken authToken, Handler handler) {
        super(handler);
        this.authToken = authToken;
    }

}
