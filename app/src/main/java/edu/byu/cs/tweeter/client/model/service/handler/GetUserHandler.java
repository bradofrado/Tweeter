package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.observer.UserTaskObserver;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetUserTask.
 */
public class GetUserHandler extends BackgroundTaskHandler<UserTaskObserver> {

    public GetUserHandler(UserTaskObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, UserTaskObserver observer) {
        User user = (User) data.getSerializable(GetUserTask.USER_KEY);

        observer.handleSuccess(user);
    }

}
