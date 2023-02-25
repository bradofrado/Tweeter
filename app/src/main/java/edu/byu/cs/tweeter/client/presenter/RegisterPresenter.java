package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;

public class RegisterPresenter extends AuthenticationPresenter {
    private UserService userService;

    public RegisterPresenter(View view) {
        super(view);
        userService = new UserService();
    }

    @Override
    protected String getDescription() {
        return "register";
    }

    public void register(String firstname, String lastname, String username, String password, String imageBytesBase64) {
        try {
            validateRegistration(firstname, lastname, username, password, imageBytesBase64);
            view.displayError(null);
            view.displayToastMessage("Registering...");

            // Register and move to MainActivity.
            userService.registerUser(firstname, lastname, username, password, imageBytesBase64, new UserObserver());
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }

    public void validateRegistration(String firstName, String lastName, String alias,
                                     String password, String image) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (image == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

}
