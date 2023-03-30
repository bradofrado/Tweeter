package edu.byu.cs.tweeter.client.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

@DisplayName("Client Server Facade - Server API Integration tests")
public class ServerIntegrationTests {
    private ServerFacade serverFacade;
    private User targetUser;

    @BeforeEach
    public void setup() {
        serverFacade = new ServerFacade();
        targetUser = new User("Allen", "Anderson", "@allen", "URL");
    }

    @Nested
    public class RegisterTests {
        private RegisterRequest request;

        @BeforeEach
        public void setup() {
            String password = "mypassword123";

            request = new RegisterRequest(targetUser.getFirstName(), targetUser.getLastName(), targetUser.getAlias(), password, targetUser.getImageUrl());
        }
        @Test
        @DisplayName("Should return valid user and auth token when user registers")
        public void should_Return_ValidUserAndAuthToken_whenUserRegisters() {


            try {
                RegisterResponse response = serverFacade.register(request);

                Assertions.assertTrue(response.isSuccess());
                Assertions.assertNotNull(response);
                Assertions.assertNotNull(response.getRegisteredUser());

                User registeredUser = response.getRegisteredUser();
                Assertions.assertEquals(targetUser.getFirstName(), registeredUser.getFirstName());
                Assertions.assertEquals(targetUser.getLastName(), registeredUser.getLastName());
                Assertions.assertEquals(targetUser.getAlias(), registeredUser.getAlias());
                Assertions.assertEquals("https://braydon-cs340.s3.us-west-2.amazonaws.com/" + targetUser.getAlias(), registeredUser.getImageUrl());

                Assertions.assertNotNull(response.getAuthToken());
                Assertions.assertNotNull(response.getAuthToken().getToken());
            } catch (Exception ex) {
                Assertions.fail(ex.getMessage());
            }
        }

        @Test
        @DisplayName("Should fail when given no password")
        public void should_Fail_whenGivenNoPassword() {
            request.setPassword(null);
            Assertions.assertThrows(TweeterRemoteException.class, () -> serverFacade.register(request));
        }
    }

    @Nested
    public class GetFollowersTests {
        private FollowerRequest request;
        private AuthToken authToken;


        @BeforeEach
        public void setup() {
            authToken = createAuthToken();
            request = new FollowerRequest(authToken, targetUser.getAlias(), 10, null);
        }

        @Test
        @DisplayName("Should return page of users when get followers")
        public void should_ReturnPageOfUsers_whenGetFollowers() {
            try {
                FollowerResponse response = serverFacade.getFollowers(request);

                Assertions.assertTrue(response.isSuccess());
                Assertions.assertNotNull(response);

                List<User> users = response.getUsers();
                Assertions.assertNotNull(users);
                Assertions.assertNotEquals(0, users.size());
                Assertions.assertTrue(response.getHasMorePages());
            } catch (Exception ex) {
                Assertions.fail(ex.getMessage());
            }
        }

        @Test
        @DisplayName("Should fail when given invalid limit")
        public void should_fail_whenGivenInvalidLimit() {
            request.setLimit(-1);

            Assertions.assertThrows(TweeterRemoteException.class, () -> serverFacade.getFollowers(request));
        }
    }

    @Nested
    public class GetFollowersCountTests {
        private FollowersCountRequest request;
        private AuthToken authToken;

        private static final String url = "";


        @BeforeEach
        public void setup() {
            authToken = createAuthToken();

            request = new FollowersCountRequest(authToken, targetUser.getAlias());
        }

        @Test
        @DisplayName("Should return users when get followers")
        public void should_Return_whenUsersGetFollowers() {
            try {
                FollowersCountResponse response = serverFacade.getFollowersCount(request);

                Assertions.assertNotNull(response);

                Assertions.assertTrue(response.isSuccess());
                Assertions.assertNotEquals(0, response.getCount());
            } catch (Exception ex) {
                Assertions.fail(ex.getMessage());
            }
        }

        @Test
        @DisplayName("Should fail when given invalid user")
        public void should_fail_whenGivenInvalidUser() {
            request.setTargetUser(null);

            Assertions.assertThrows(TweeterRemoteException.class, () -> serverFacade.getFollowersCount(request));
        }
    }



    private AuthToken createAuthToken() {
        AuthToken authToken = null;
        try {
            RegisterResponse registerResponse = serverFacade.register(new RegisterRequest(targetUser.getFirstName(), targetUser.getLastName(),
                    targetUser.getAlias(), "mypassword123", targetUser.getImageUrl()));
            Assertions.assertTrue(registerResponse.isSuccess());

            authToken = registerResponse.getAuthToken();
        } catch (Exception ex) {
            Assertions.fail(ex.getMessage());
        }

        return authToken;
    }

}
