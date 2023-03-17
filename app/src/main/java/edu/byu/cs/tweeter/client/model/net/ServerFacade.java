package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "https://awqed4rfs2.execute-api.us-west-2.amazonaws.com/dev";

    private static final String AUTH_TOKEN_HEADER = "AuthToken";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    public final static String LOGIN_URL = "/login";
    public final static String LOGOUT_URL = "/logout";
    public final static String REGISTER_URL = "/register";
    public final static String USER_URL = "/user";
    public final static String GET_FOLLOWERS_URL = "/followers";
    public final static String IS_FOLLOWER_URL = "/isfollower";
    public final static String GET_FOLLOWEES_URL = "/following";
    public final static String GET_FOLLOWERS_COUNT_URL = "/followers/count";
    public final static String GET_FOLLOWEES_COUNT_URL = "/followees/count";
    public final static String FOLLOW_URL = "/follow";
    public final static String UNFOLLOW_URL = "/unfollow";
    public final static String GET_FEED_URL = "/feed";
    public final static String GET_STORY_URL = "/story";
    public final static String POST_STATUS_URL = "/poststatus";


    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(LOGIN_URL, request, null, LoginResponse.class);
    }

    public LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(LOGOUT_URL, request, null, LogoutResponse.class);
    }

    public RegisterResponse register(RegisterRequest request)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(REGISTER_URL, request, null, RegisterResponse.class);
    }

    public UserResponse getUser(UserRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doGet(USER_URL + "/" + request.getAlias(), createAuthTokenHeader(request.getAuthToken()), UserResponse.class);
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(GET_FOLLOWEES_URL, request, null, FollowingResponse.class);
    }

    public FollowerResponse getFollowers(FollowerRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(GET_FOLLOWERS_URL, request, null, FollowerResponse.class);
    }

    public FollowersCountResponse getFollowersCount(FollowersCountRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(GET_FOLLOWERS_COUNT_URL, request, null,
                FollowersCountResponse.class);
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(GET_FOLLOWEES_COUNT_URL, request, null,
                FollowingCountResponse.class);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(IS_FOLLOWER_URL, request, null, IsFollowerResponse.class);
    }

    public FollowResponse follow(FollowRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(FOLLOW_URL, request, null, FollowResponse.class);
    }

    public UnfollowResponse unfollow(UnfollowRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(UNFOLLOW_URL, request, null, UnfollowResponse.class);
    }

    public FeedResponse getFeed(FeedRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(GET_FEED_URL, request, null, FeedResponse.class);
    }

    public StoryResponse getStory(StoryRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(GET_STORY_URL, request, null, StoryResponse.class);
    }

    public PostStatusResponse postStatus(PostStatusRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(POST_STATUS_URL, request, null, PostStatusResponse.class);
    }

    private Map<String, String> createAuthTokenHeader(AuthToken authToken) {
        Map<String, String> headers = new HashMap<>();
        headers.put(AUTH_TOKEN_HEADER, authToken.getToken());

        return headers;
    }

    private String concatURL(String... args) {
        String full = "";
        for (String arg: args) {
            full += "/" + arg;
        }

        return full;
    }
}