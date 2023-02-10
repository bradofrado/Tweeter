package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.CountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;

public class GetFollowersCountHandler extends BackgroundTaskHandler<FollowService.FollowerCountObserver> {

    public GetFollowersCountHandler(FollowService.FollowerCountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, FollowService.FollowerCountObserver observer) {
        int count = data.getInt(CountTask.COUNT_KEY);
        observer.setFollowerCount(count);
    }

}
