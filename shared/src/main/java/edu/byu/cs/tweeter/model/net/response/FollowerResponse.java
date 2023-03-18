package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowerResponse extends PagedResponse {
    private List<User> users;

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param hasMorePages Whether or not there are more pages of users
     * @param users The list of followers
     */
    public FollowerResponse(List<User> users, boolean hasMorePages) {
        super(true, hasMorePages);
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
