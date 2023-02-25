package edu.byu.cs.tweeter.client.model.service.observer;

public interface CountTaskObserver extends ServiceObserver {
    void handleSuccess(int count);
}
