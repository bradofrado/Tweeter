package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;

public class IsFollowerHandler extends BackgroundTaskHandler<FollowService.IsFollowObserver> {

    public IsFollowerHandler(FollowService.IsFollowObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, FollowService.IsFollowObserver observer) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.setIsFollow(isFollower);
    }

}
