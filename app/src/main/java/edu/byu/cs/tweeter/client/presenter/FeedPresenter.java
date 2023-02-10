package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter {
    public interface View {
        void displayMessage(String message);

        void selectUser(User user);

        void setLoadingFooter(boolean value);

        void addItems(List<Status> statuses);
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

    public FeedPresenter(View view, User user) {
        this.view = view;
        this.user = user;
        userService = new UserService();
        statusService = new StatusService();
    }

    public void getUser(String userName) {
        userService.getUser(userName, new UserObserver());
    }

    public void loadFeedItems() {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);

            statusService.getStatuses(user, PAGE_SIZE, lastStatus, new FeedObserver());
        }
    }

    public class UserObserver implements UserService.UserObserver {

        @Override
        public void handleError(String message) {
            view.displayMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }

        @Override
        public void setUser(User _user) {
            user = _user;
            view.selectUser(user);
        }
    }

    public class FeedObserver implements PagedTaskObserver<Status> {

        @Override
        public void handleSuccess(List<Status> statuses, boolean _hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            hasMorePages = _hasMorePages;
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addItems(statuses);
        }

        @Override
        public void handleError(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get feed: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get feed because of exception: " + ex.getMessage());
        }
    }
}
