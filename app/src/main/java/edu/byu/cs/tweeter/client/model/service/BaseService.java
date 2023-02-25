package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;

public class BaseService {
    protected void executeTask(BackgroundTask task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    protected void executeTasks(BackgroundTask... tasks) {
        ExecutorService executor = Executors.newFixedThreadPool(tasks.length);
        for (BackgroundTask task : tasks) {
            executor.execute(task);
        }
    }
}
