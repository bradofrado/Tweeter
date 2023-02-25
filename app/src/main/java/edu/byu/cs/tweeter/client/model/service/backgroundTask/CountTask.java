package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class CountTask extends AuthenticatedTask {
    public static final String COUNT_KEY = "count";

    /**
     * The user whose follower count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;

    public CountTask(AuthToken authToken, Handler handler, User targetUser) {
        super(authToken, handler);
        this.targetUser = targetUser;
    }

    @Override
    protected void processTask() {

    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, 20);
    }
}
