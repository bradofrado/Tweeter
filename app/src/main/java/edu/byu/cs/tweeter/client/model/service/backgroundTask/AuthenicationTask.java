package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.util.Pair;

public abstract class AuthenicationTask extends BackgroundTask {
    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    private User user;
    private AuthToken authToken;

    public AuthenicationTask(Handler handler) {
        super(handler);
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, user);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }

    @Override
    protected void processTask() throws IOException, TweeterRemoteException, TaskFailedException {
        Pair<User, AuthToken> authenticationResult = doAuthentication();

        user = authenticationResult.getFirst();
        authToken = authenticationResult.getSecond();
    }

    protected abstract Pair<User, AuthToken> doAuthentication() throws IOException, TweeterRemoteException, TaskFailedException;
}
