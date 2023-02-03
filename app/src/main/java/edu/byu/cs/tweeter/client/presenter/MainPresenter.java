package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    public interface View {
        void displayMessage(String message);
        void setIsFollower(boolean value);

        void logoutUser();
    }

    private View view;

    private FollowService followService;
    private UserService userService;

    public MainPresenter(View view) {
        this.view = view;
        followService = new FollowService();
        userService = new UserService();
    }

    public void isFollower(User selectedUser) {
        followService.isFollower(selectedUser, new IsFollowObserver());
    }

    public void logout() {
        userService.logout(new LogoutObserver());
    }

    public class IsFollowObserver implements FollowService.IsFollowObserver {

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayError(String message) {
            view.displayMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }

        @Override
        public void setIsFollow(Boolean isFollower) {
            view.setIsFollower(isFollower);
        }
    }

    public class LogoutObserver implements UserService.LoginObserver {
        @Override
        public void displayError(String message) {
            view.displayMessage("Failed to logout: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to logout because of exception: " + ex.getMessage());
        }

        @Override
        public void loggedIn(User loggedInUser) {

        }

        @Override
        public void loggedOut() {
            //Clear user data (cached data).
            Cache.getInstance().clearCache();
            view.logoutUser();
        }
    }
}
