package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {
    private static final String LOG_TAG = "LogoutTask";

    public LogoutTask(AuthToken authToken, Handler handler) {
        super(authToken, handler);
    }

    @Override
    protected void processTask() throws IOException, TweeterRemoteException, TaskFailedException {
        LogoutResponse response = getServerFacade().logout(new LogoutRequest(authToken));
        validateResponse(response);
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {

    }
}
