package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.observer.UserTaskObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticationPresenter extends Presenter<AuthenticationPresenter.View> {
    public interface View extends Presenter.View {
        void displayError(String message);
        void displayMessage(String message);
        void cancelToast();
        void setUser(User loggedInUser);
        void displayToastMessage(String s);
    }

    public AuthenticationPresenter(View view) {
        super(view);
    }

    protected class UserObserver implements UserTaskObserver {
        @Override
        public void handleError(String message) {
            view.displayMessage("Failed to " + getDescription() + ": " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to " + getDescription() + " because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(User user) {
            view.cancelToast();

            view.displayMessage("Hello " + user.getName());
            try {
                view.setUser(user);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
