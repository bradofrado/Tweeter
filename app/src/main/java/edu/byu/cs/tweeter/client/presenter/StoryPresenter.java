package edu.byu.cs.tweeter.client.presenter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.view.main.story.StoryFragment;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter {
    public interface View {

        void sendUser(User user);

        void setLoadingFooter(boolean b);

        void setStory(List<Status> statuses);

        void displayMessage(String message);
    }

    private View view;
    private User user;

    private Status lastStatus;

    private boolean hasMorePages;
    private boolean isLoading = false;

    private UserService userService;
    private StatusService statusService;

    private static final int PAGE_SIZE = 10;

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public StoryPresenter(View view, User user) {
        this.view = view;
        this.user = user;
        userService = new UserService();
        statusService = new StatusService();
    }

    public void getUser(String userName) {
        userService.getUser(userName, new UserObserver());
    }

    public void loadStoryItems() {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);

            statusService.getStory(user, PAGE_SIZE, lastStatus, new StoryObserver());
        }
    }

    public class UserObserver implements UserService.Observer {

        @Override
        public void displayError(String message) {
            view.displayMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }

        @Override
        public void setUser(User _user) {
            user = _user;
            view.sendUser(user);
        }

        @Override
        public void loggedIn(User loggedInUser) {

        }

        @Override
        public void registered(User registeredUser) {

        }
    }

    public class StoryObserver implements StatusService.Observer {

        @Override
        public void setStatuses(boolean _hasMorePages, List<Status> statuses) {
            isLoading = false;
            view.setLoadingFooter(false);
            hasMorePages = _hasMorePages;
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.setStory(statuses);
        }

        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get story: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get story because of exception: " + ex.getMessage());
        }
    }
}
