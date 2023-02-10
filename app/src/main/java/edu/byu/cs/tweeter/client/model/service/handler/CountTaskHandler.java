package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.CountTask;
import edu.byu.cs.tweeter.client.model.service.observer.CountTaskObserver;

public class CountTaskHandler extends BackgroundTaskHandler<CountTaskObserver> {
    public CountTaskHandler(CountTaskObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, CountTaskObserver observer) {
        int count = data.getInt(CountTask.COUNT_KEY);
        observer.handleSuccess(count);
    }
}
