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
        targetUser = new User("Braydon", "Jones", "bradofrado", "URL");
    }

    @Nested
    public class RegisterTests {
        private RegisterRequest request;
        private static final String registerUrl = "";

        @BeforeEach
        public void setup() {
            String password = "mypassword123";

            request = new RegisterRequest(targetUser.getFirstName(), targetUser.getLastName(), targetUser.getAlias(), password, targetUser.getImageUrl());
        }
        @Test
        @DisplayName("Should return valid user and auth token when user registers")
        public void should_Return_ValidUserAndAuthToken_whenUserRegisters() {


            try {
                RegisterResponse response = serverFacade.register(request, registerUrl);

                Assertions.assertTrue(response.isSuccess());
                Assertions.assertNotNull(response);
                Assertions.assertNotNull(response.getRegisteredUser());

                User registeredUser = response.getRegisteredUser();
                Assertions.assertEquals(targetUser.getFirstName(), registeredUser.getFirstName());
                Assertions.assertEquals(targetUser.getLastName(), registeredUser.getLastName());
                Assertions.assertEquals(targetUser.getAlias(), registeredUser.getAlias());
                Assertions.assertEquals(targetUser.getImageUrl(), registeredUser.getImageUrl());

                Assertions.assertNotNull(response.getAuthToken());
                Assertions.assertNotNull(response.getAuthToken().getToken());
            } catch (Exception ex) {
                Assertions.fail(ex.getMessage());
            }
        }

        @Test
        @DisplayName("Should fail when ")
        public void should_Fail_when() {
            Assertions.assertThrows(TweeterRemoteException.class, () -> serverFacade.register(request, registerUrl));
        }
    }

    @Nested
    public class GetFollowersTests {
        private FollowerRequest request;
        private AuthToken authToken;
        private static final String url = "";


        @BeforeEach
        public void setup() {
            authToken = createAuthToken();
            request = new FollowerRequest(authToken, targetUser, 10, null);
        }

        @Test
        @DisplayName("Should return _ when get followers")
        public void should_Return_whenGetFollowers() {
            try {
                FollowerResponse response = serverFacade.getFollowers(request, url);

                Assertions.assertTrue(response.isSuccess());
                Assertions.assertNotNull(response);

                List<User> users = response.getUsers();
                Assertions.assertNotNull(users);
                Assertions.assertNotEquals(0, users.size());
                Assertions.assertTrue(response.hasMorePages());
            } catch (Exception ex) {
                Assertions.fail(ex.getMessage());
            }
        }

        @Test
        @DisplayName("Should fail when given invalid limit")
        public void should_fail_whenGivenInvalidAuthToken() {
            request.setAuthToken(new AuthToken("badboi"));

            Assertions.assertThrows(TweeterRemoteException.class, () -> serverFacade.getFollowers(request, url));
        }

        @Test
        @DisplayName("Should fail when given invalid limit")
        public void should_fail_whenGivenInvalidLimit() {
            request.setLimit(-1);

            Assertions.assertThrows(TweeterRemoteException.class, () -> serverFacade.getFollowers(request, url));
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

            request = new FollowersCountRequest(authToken, targetUser);
        }

        @Test
        @DisplayName("Should return _ when get followers")
        public void should_Return_whenGetFollowers() {
            try {
                FollowersCountResponse response = serverFacade.getFollowersCount(request, url);

                Assertions.assertNotNull(response);

                Assertions.assertTrue(response.isSuccess());
                Assertions.assertNotEquals(0, response.getCount());
            } catch (Exception ex) {
                Assertions.fail(ex.getMessage());
            }
        }

        @Test
        @DisplayName("Should fail when given invalid limit")
        public void should_fail_whenGivenInvalidAuthToken() {
            request.setAuthToken(new AuthToken("badboi"));

            Assertions.assertThrows(TweeterRemoteException.class, () -> serverFacade.getFollowersCount(request, url));
        }
    }



    private AuthToken createAuthToken() {
        AuthToken authToken = null;
        try {
            RegisterResponse registerResponse = serverFacade.register(new RegisterRequest(targetUser.getFirstName(), targetUser.getLastName(),
                    targetUser.getAlias(), "mypassword123", targetUser.getImageUrl()), RegisterTests.registerUrl);
            Assertions.assertTrue(registerResponse.isSuccess());

            authToken = registerResponse.getAuthToken();
        } catch (Exception ex) {
            Assertions.fail(ex.getMessage());
        }

        return authToken;
    }

}
