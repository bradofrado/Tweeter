package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowerResponse extends Response {
    private boolean hasMorePages;
    private List<User> users;

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param hasMorePages Whether or not there are more pages of users
     * @param users The list of followers
     */
    public FollowerResponse(List<User> users, boolean hasMorePages) {
        super(true);
        this.hasMorePages = hasMorePages;
        this.users = users;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FollowerResponse(String message) {
        super(false, message);
    }


    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
