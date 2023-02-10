package edu.byu.cs.tweeter.client.presenter;

public abstract class Presenter<T extends Presenter.View> {
    public interface View {
        void displayMessage(String message);
    }

    protected T view;

    public Presenter(T view) {
        this.view = view;
    }

    protected abstract String getDescription();
}
