package edu.byu.cs.tweeter.client.model.service.observer;

public interface ServiceObserver {
    void handleError(String message);
    void handleException(Exception ex);
}
