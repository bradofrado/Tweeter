package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;

public class GetFollowingCountHandler extends BackgroundTaskHandler<FollowService.FollowingCountObserver> {

    public GetFollowingCountHandler(FollowService.FollowingCountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, FollowService.FollowingCountObserver observer) {
        int count = data.getInt(GetFollowingCountTask.COUNT_KEY);
        observer.setFollowingCount(count);
    }
}
