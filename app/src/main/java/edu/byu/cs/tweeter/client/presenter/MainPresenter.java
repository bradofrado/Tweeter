package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.CountTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter<MainPresenter.View> {
    private static final String LOG_TAG = "MainPresenter";

    public interface View extends Presenter.View {
        void setIsFollower(boolean value);

        void logoutUser();

        void setFollowerCount(int count);

        void setFollowingCount(int count);

        void updateFollowButton(boolean value);

        void setFollowButtonEnabled(boolean b);

        void cancelInfoMessage();
        void displayInfoMessage(String message);
    }

    private User selectedUser;

    private FollowService followService;
    private UserService userService;
    private StatusService statusService;

    public MainPresenter(View view, User selectedUser) {
        super(view);
        this.selectedUser = selectedUser;
        followService = new FollowService();
        userService = new UserService();
    }

    public void isFollower(User selectedUser) {
        followService.isFollower(selectedUser, new IsFollowObserver());
    }

    public void logout() {
        userService.logout(new LogoutObserver());
    }

    public void getFollowCounts() {
        followService.getFollowCounts(selectedUser, new FollowerCountObserver(), new FollowingCountObserver());
    }

    public void setFollowingStatus(boolean isFollowing) {
        if (isFollowing) {
            followService.unfollow(selectedUser, new UnFollowObserver());

            view.displayMessage("Removing " + selectedUser.getName() + "...");
        } else {
            followService.follow(selectedUser, new FollowObserver());

            view.displayMessage("Adding " + selectedUser.getName() + "...");
        }
    }

    public void postStatus(String post) {
        try {
            view.displayInfoMessage("Posting Status...");
            Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), getFormattedDateTime(), parseURLs(post), parseMentions(post));
            getStatusService().postStatus(newStatus, new PostStatusObserver());
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            view.displayMessage("Failed to post the status because of exception: " + ex.getMessage());
        }
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }

        return statusService;
    }

    private class LogoutObserver extends ServiceObserver implements SimpleNotificationObserver {
        @Override
        public void handleSuccess() {
            //Clear user data (cached data).
            Cache.getInstance().clearCache();
            view.logoutUser();
        }

        @Override
        protected String getDescription() {
            return "logout";
        }
    }

    private class IsFollowObserver extends ServiceObserver implements edu.byu.cs.tweeter.client.model.service.observer.IsFollowObserver {
        @Override
        public void handleSuccess(Boolean isFollower) {
            view.setIsFollower(isFollower);
        }

        @Override
        protected String getDescription() {
            return "determine following relationship";
        }
    }

    private class FollowerCountObserver extends ServiceObserver implements CountTaskObserver {
        @Override
        public void handleSuccess(int count) {
            view.setFollowerCount(count);
        }

        @Override
        protected String getDescription() {
            return "get followers count";
        }
    }

    private class FollowingCountObserver extends ServiceObserver implements CountTaskObserver {
        @Override
        protected String getDescription() {
            return "get following count";
        }

        @Override
        public void handleSuccess(int count) {
            view.setFollowingCount(count);
        }
    }

    private class UnFollowObserver extends ServiceObserver implements SimpleNotificationObserver {
        @Override
        public void handleError(String message) {
            super.handleError(message);
            view.setFollowButtonEnabled(true);
        }

        @Override
        public void handleException(Exception ex) {
            super.handleException(ex);
            view.setFollowButtonEnabled(true);
        }

        @Override
        protected String getDescription() {
            return "unfollow";
        }

        @Override
        public void handleSuccess() {
            getFollowCounts();
            view.updateFollowButton(true);
            view.setFollowButtonEnabled(true);
        }
    }

    private class FollowObserver extends ServiceObserver implements SimpleNotificationObserver {
        @Override
        public void handleError(String message) {
            super.handleError(message);
            view.setFollowButtonEnabled(true);
        }

        @Override
        public void handleException(Exception ex) {
            super.handleException(ex);
            view.setFollowButtonEnabled(true);
        }

        @Override
        protected String getDescription() {
            return "follow";
        }

        @Override
        public void handleSuccess() {
            getFollowCounts();
            view.updateFollowButton(false);
            view.setFollowButtonEnabled(true);
        }
    }

    protected class PostStatusObserver extends ServiceObserver implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            view.cancelInfoMessage();
            view.displayMessage("Successfully Posted!");
        }

        @Override
        public void handleError(String message) {
            view.cancelInfoMessage();
            super.handleError(message);
        }

        @Override
        public void handleException(Exception ex) {
            view.cancelInfoMessage();
            super.handleException(ex);
        }

        @Override
        protected String getDescription() {
            return "post status";
        }
    }

    private String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    private List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    private int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    private List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }
}
