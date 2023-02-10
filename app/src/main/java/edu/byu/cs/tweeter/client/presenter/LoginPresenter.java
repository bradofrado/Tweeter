package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.UserTaskObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {
    public interface View {
        void displayError(String message);
        void displayMessage(String message);
        void cancelLoginToast();
        void setUser(User loggedInUser);
        void displayLoginMessage(String s);
    }

    private View view;

    private UserService userService;

    public LoginPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public void login(String username, String password) {
        // Login and move to MainActivity.
        try {
            validateLogin(username, password);
            view.displayError(null);

            view.displayLoginMessage("Logging In...");

            userService.loginUser(username, password, new LoginObserver());
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }

    public void validateLogin(String username, String password) {
        if (username.length() > 0 && username.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (username.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public class LoginObserver implements UserTaskObserver {

        @Override
        public void handleError(String message) {
            view.displayMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to login because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(User loggedInUser) {
            view.cancelLoginToast();

            view.displayMessage("Hello " + loggedInUser.getName());

            view.setUser(loggedInUser);
        }
    }
}
