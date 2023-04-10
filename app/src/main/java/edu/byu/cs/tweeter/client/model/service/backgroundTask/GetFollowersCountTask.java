package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends CountTask {
    private static final String LOG_TAG = "GetFollowersCountTask";
    private int count;

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, messageHandler, targetUser);
    }

    @Override
    protected void processTask() throws IOException, TweeterRemoteException, TaskFailedException {
        FollowersCountResponse response = getServerFacade().getFollowersCount(new FollowersCountRequest(authToken, getTargetUser().getAlias()));
        validateResponse(response);

        count = response.getCount();
    }

    @Override
    protected int getCount() {
        return count;
    }
}
