package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observer.UserTaskObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {
    public interface View<T> {

        void displayMessage(String message);

        void selectUser(User user);

        void setLoadingFooter(boolean value);

        void addMoreItems(List<T> followers);
    }

    private static final int PAGE_SIZE = 10;

    private View view;
    private User user;
    private T lastItem;
    private boolean hasMorePages;
    private boolean isLoading = false;

    private UserService userService;

    public boolean hasMorePages() {
        return hasMorePages;
    }
    public boolean isLoading() {
        return isLoading;
    }

    public PagedPresenter(View view, User user) {
        this.view = view;
        this.user = user;
        userService = new UserService();
    }

    public void getUser(String userName) {
        userService.getUser(userName, new UserObserver());
        view.displayMessage("Getting user's profile...");
    }


    public void loadMoreItems() {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            getItems(user, PAGE_SIZE, lastItem);
        }
    }

    protected abstract void getItems(User user, int pageSize, T lastItem);

    protected abstract String getDescription();


    private class UserObserver implements UserTaskObserver {

        @Override
        public void handleError(String message) {
            view.displayMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(User _user) {
            user = _user;
            view.selectUser(user);
        }
    }

    protected class PagedObserver implements PagedTaskObserver<T> {
        @Override
        public void handleError(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to " + getDescription() + ": " + message);
        }

        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to " + getDescription() + " because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(List<T> lastItems, boolean _hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastItem = (lastItems.size() > 0) ? lastItems.get(lastItems.size() - 1) : null;
            hasMorePages = _hasMorePages;
            view.addMoreItems(lastItems);
        }
    }
}
