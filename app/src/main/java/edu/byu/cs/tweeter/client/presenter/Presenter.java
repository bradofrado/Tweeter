package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public abstract class Presenter<T extends Presenter.View> {
    public interface View {
        void displayMessage(String message);
    }

    protected T view;

    public Presenter(T view) {
        this.view = view;
    }

    public abstract class ServiceObserver implements edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver {

        @Override
        public void handleError(String message) {
            view.displayMessage("Failed to " + getDescription() + ": " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to " + getDescription() + " because of exception: " + ex.getMessage());
        }

        protected abstract String getDescription();
    }
}
