package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterResponse extends Response {
    private User registeredUser;
    private AuthToken authToken;

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param registeredUser the now registered user.
     * @param authToken the auth token representing this user's session with the server.
     */
    public RegisterResponse(User registeredUser, AuthToken authToken) {
        super(true);
        this.registeredUser = registeredUser;
        this.authToken = authToken;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public RegisterResponse(String message) {
        super(false, message);
    }

    public User getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(User registeredUser) {
        this.registeredUser = registeredUser;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
