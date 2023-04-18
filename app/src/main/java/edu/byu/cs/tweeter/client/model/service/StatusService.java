package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends BaseService {
    public void getStatuses(User user, int pageSize, Status lastStatus, PagedTaskObserver<Status> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PagedTaskHandler<>(observer));
        executeTask(getFeedTask);
    }

    public void getStory(User user, int pageSize, Status lastStatus, PagedTaskObserver<Status> storyObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PagedTaskHandler<>(storyObserver));
        executeTask(getStoryTask);
    }

    public void postStatus(Status newStatus, SimpleNotificationObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(getCurrUserAuthToken(),
                newStatus, new SimpleNotificationHandler(observer));
        executeTask(statusTask);
    }

    public AuthToken getCurrUserAuthToken() {
        return Cache.getInstance().getCurrUserAuthToken();
    }
}
