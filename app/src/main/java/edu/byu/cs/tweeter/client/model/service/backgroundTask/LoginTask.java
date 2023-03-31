package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenicationTask {

    private static final String LOG_TAG = "LoginTask";

    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    private String username;
    /**
     * The user's password.
     */
    private String password;

    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }

    @Override
    protected Pair<User, AuthToken> doAuthentication() throws IOException, TweeterRemoteException, TaskFailedException {
        LoginResponse response = getServerFacade().login(new LoginRequest(username, password));
        validateResponse(response);

        return new Pair<>(response.getUser(), response.getAuthToken());
    }
}
