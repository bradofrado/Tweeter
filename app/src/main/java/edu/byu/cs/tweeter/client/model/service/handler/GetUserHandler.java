package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetUserTask.
 */
public class GetUserHandler extends BackgroundTaskHandler<UserService.UserObserver> {

    public GetUserHandler(UserService.UserObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, UserService.UserObserver observer) {
        User user = (User) data.getSerializable(GetUserTask.USER_KEY);

        observer.setUser(user);
    }

}
