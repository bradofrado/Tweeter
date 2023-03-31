package edu.byu.cs.tweeter.client.model.service.backgroundTask;

public class TaskFailedException extends Exception {
    public TaskFailedException(String message) {
        super(message);
    }
}
