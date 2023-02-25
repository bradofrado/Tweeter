package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;

public class LoginPresenter extends AuthenticationPresenter {
   private UserService userService;

    public LoginPresenter(View view) {
        super(view);
        userService = new UserService();
    }

    @Override
    protected String getDescription() {
        return "login";
    }

    public void login(String username, String password) {
        // Login and move to MainActivity.
        try {
            validateLogin(username, password);
            view.displayError(null);
            view.displayToastMessage("Logging In...");

            userService.loginUser(username, password, new UserObserver());
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
}
