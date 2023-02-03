package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {
    public interface View {

        void displayError(String message);

        void displayRegisterToast(String message);

        void setRegisteredUser(User registeredUser);

        void cancelRegisterToast();

        void displayMessage(String message);
    }

    private View view;

    private UserService userService;

    public RegisterPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public void register(String firstname, String lastname, String username, String password, BitmapDrawable imageDrawable) {
        try {
            validateRegistration(firstname, lastname, username, password, imageDrawable);
            view.displayError(null);
            view.displayRegisterToast("Registering...");

            // Register and move to MainActivity.
            // Convert image to byte array.
            Bitmap image = imageDrawable.getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] imageBytes = bos.toByteArray();

            // Intentionally, Use the java Base64 encoder so it is compatible with M4.
            String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);


            userService.registerUser(firstname, lastname, username, password, imageBytesBase64, new RegisterUserObserver());
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }

    public void validateRegistration(String firstName, String lastName, String alias,
                                     String password, BitmapDrawable image) {
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

    public class RegisterUserObserver implements UserService.RegisterObserver {
        @Override
        public void displayError(String message) {
            view.displayMessage("Failed to register: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to register because of exception: " + ex.getMessage());
        }

        @Override
        public void registered(User registeredUser) {
            view.cancelRegisterToast();

            view.displayMessage("Hello " + registeredUser.getName());
            try {
                view.setRegisteredUser(registeredUser);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
