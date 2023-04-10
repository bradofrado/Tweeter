package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends CountTask {
    private static final String LOG_TAG = "GetFollowingCountTask";

    private int count;

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, messageHandler, targetUser);
    }

    @Override
    protected void processTask() throws IOException, TweeterRemoteException, TaskFailedException {
        FollowingCountResponse response = getServerFacade().getFollowingCount(new FollowingCountRequest(authToken, getTargetUser().getAlias()));
        validateResponse(response);

        count = response.getCount();
    }

    @Override
    protected int getCount() {
        return count;
    }
}
