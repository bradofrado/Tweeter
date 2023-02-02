package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter {
    public interface View {
        void setLoadingFooter(boolean value);

        void displayMessage(String message);

        void addMoreItems(List<User> followees);
    }

    private static final int PAGE_SIZE = 10;

    private View view;
    private FollowService followService;

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

    public FollowingPresenter(View view) {
        this.view = view;
        followService = new FollowService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            followService.loadMoreItems(user, PAGE_SIZE, lastFollowee, new FollowingObserver());
        }
    }

    public class FollowingObserver implements FollowService.Observer {

        @Override
        public void displayMessage(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage(message);
        }

        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get following: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get following because of exception: " + ex.getMessage());
        }

        @Override
        public void addFollowees(List<User> followees, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addMoreItems(followees);
        }
    }
}
