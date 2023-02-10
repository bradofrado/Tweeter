package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersPresenter {
    public interface View {

        void displayMessage(String message);

        void selectUser(User user);

        void setLoadingFooter(boolean value);

        void addMoreItems(List<User> followers);
    }

    private View view;

    private UserService userService;
    private FollowService followService;

    private User user;

    private User lastFollower;
    private boolean hasMorePages;
    private boolean isLoading = false;

    private static final int PAGE_SIZE = 10;

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public GetFollowersPresenter(View view, User user) {
        this.view = view;
        this.user = user;
        userService = new UserService();
        followService = new FollowService();
    }

    public void getUser(String userName) {
        userService.getUser(userName, new UserObserver());
        view.displayMessage("Getting user's profile...");
    }

    public void loadMoreItems() {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);

            followService.getMoreFollowers(user, PAGE_SIZE, lastFollower, new FollowerObserver());
        }
    }

    public class FollowerObserver implements PagedTaskObserver<User> {

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
        public void handleSuccess(List<User> followers, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            GetFollowersPresenter.this.hasMorePages = hasMorePages;
            view.addMoreItems(followers);
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
        public void setUser(User user) {
            GetFollowersPresenter.this.user = user;
            view.selectUser(user);
        }
    }
}
