package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.AuthenicationTask;
import edu.byu.cs.tweeter.client.model.service.observer.UserTaskObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticationTaskHandler extends BackgroundTaskHandler<UserTaskObserver> {
    public AuthenticationTaskHandler(UserTaskObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, UserTaskObserver observer) {
        User user = (User) data.getSerializable(AuthenicationTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(AuthenicationTask.AUTH_TOKEN_KEY);

        // Cache user session information
        Cache.getInstance().setCurrUser(user);
        Cache.getInstance().setCurrUserAuthToken(authToken);

        observer.handleSuccess(user);
    }
}
