package edu.byu.cs.tweeter.model.net.response;

public class FollowingCountResponse extends Response {
    private int count;

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param count the amount of followers a user has
     */
    public FollowingCountResponse(int count) {
        super(true);
        this.count = count;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FollowingCountResponse(String message) {
        super(false, message);
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
