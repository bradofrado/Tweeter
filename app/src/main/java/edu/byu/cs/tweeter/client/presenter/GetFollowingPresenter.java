package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.UserTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingPresenter {
    public interface View {
        void setLoadingFooter(boolean value);

        void displayMessage(String message);

        void addMoreItems(List<User> followees);

        void selectUser(User user);
    }

    private static final int PAGE_SIZE = 10;

    private View view;
    private User user;
    private FollowService followService;
    private UserService userService;

    private User lastFollowee;

    private boolean hasMorePages;
    private boolean isLoading = false;

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean value) {
        hasMorePages = value;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public GetFollowingPresenter(View view, User user) {
        this.view = view;
        this.user = user;
        followService = new FollowService();
        userService = new UserService();
    }

    public void loadMoreItems() {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            followService.getMoreFollowing(user, PAGE_SIZE, lastFollowee, new FollowingObserver());
        }
    }
    
    public void getUser(String userName) {
        userService.getUser(userName, new UserObserver());
        view.displayMessage("Getting user's profile...");
    }

    public class FollowingObserver implements PagedTaskObserver<User> {

//        @Override
//        public void displayMessage(String message) {
//            isLoading = false;
//            view.setLoadingFooter(false);
//            view.displayMessage(message);
//        }

        @Override
        public void handleError(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get following: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get following because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(List<User> followees, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addMoreItems(followees);
        }
    }

    public class UserObserver implements UserTaskObserver {

        @Override
        public void handleError(String message) {
            view.displayMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(User user) {
            GetFollowingPresenter.this.user = user;
            view.selectUser(user);
        }
    }
}
