package edu.byu.cs.tweeter.client.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.Timestamp;

@DisplayName("Client Server Facade - Server API Integration tests")
public class ServerIntegrationTests {
    private ServerFacade serverFacade;
    private User targetUser;
    private String password = "password";
    private AuthToken authToken;

    private CountDownLatch countDownLatch;

    @BeforeEach
    public void setup() {
        serverFacade = new ServerFacade();
        targetUser = new User("Braydon", "Jones", "@bradofrado", "URL");
        LoginRequest request = new LoginRequest(targetUser.getAlias(), password);
        try {
            LoginResponse response = serverFacade.login(request);
            authToken = response.getAuthToken();
        } catch(Exception ex) {
            Assertions.fail(ex.getMessage());
        }

        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    @Nested
    public class PostStatusTests {
        private Status status;
        private MainPresenter mainPresenter;
        private MainPresenter.View view;
        private StatusService statusService;

        @BeforeEach
        public void setup() {
            status = new Status("This is a post", targetUser, Timestamp.getFormattedDate(System.currentTimeMillis()),
                    null, null);
            view = Mockito.spy(new MainPresenter.View() {
                @Override
                public void setIsFollower(boolean value) {

                }

                @Override
                public void logoutUser() {

                }

                @Override
                public void setFollowerCount(int count) {

                }

                @Override
                public void setFollowingCount(int count) {

                }

                @Override
                public void updateFollowButton(boolean value) {

                }

                @Override
                public void setFollowButtonEnabled(boolean b) {

                }

                @Override
                public void cancelInfoMessage() {

                }

                @Override
                public void displayInfoMessage(String message) {

                }

                @Override
                public void displayMessage(String message) {

                }
            });
            mainPresenter = Mockito.spy(new MainPresenter(view, targetUser));
            Cache cache = Mockito.mock(Cache.class);
            statusService = Mockito.spy(new StatusService());
            Mockito.when(statusService.getCurrUserAuthToken()).thenReturn(authToken);
            Mockito.when(cache.getCurrUser()).thenReturn(targetUser);
            Mockito.when(cache.getCurrUserAuthToken()).thenReturn(authToken);
            Mockito.when(mainPresenter.getCache()).thenReturn(cache);
            Mockito.when(mainPresenter.getStatusService()).thenReturn(statusService);
            Mockito.when(mainPresenter.getNewPostStatusObserver()).thenReturn(mainPresenter.new PostStatusObserver() {
                @Override
                public void handleSuccess() {
                    countDownLatch.countDown();
                    super.handleSuccess();
                }

                @Override
                public void handleError(String error) {
                    countDownLatch.countDown();
                    super.handleError(error);
                }

                @Override
                public void handleException(Exception ex) {
                    countDownLatch.countDown();
                    super.handleException(ex);
                }
            });
        }
        @Test
        @DisplayName("Should return status when post status")
        public void should_ReturnStatus_whenPostStatus() {
            mainPresenter.postStatus("This is a post");

            try {
                awaitCountDownLatch();
                Mockito.verify(view).displayMessage("Successfully Posted!");
                StoryResponse response = serverFacade.getStory(new StoryRequest(authToken, targetUser.getAlias(), 1, null));
                Assertions.assertTrue(response.isSuccess());
                Assertions.assertEquals(1, response.getStatuses().size());
                Status attemptedPost = response.getStatuses().get(0);
                Assertions.assertEquals(status.getPost(), attemptedPost.getPost());
                Assertions.assertEquals(status.getUser().getAlias(), attemptedPost.getUser().getAlias());
            } catch (Exception ex) {
                Assertions.fail(ex.getMessage());
            }
        }
    }

//    @Nested
//    public class RegisterTests {
//        private RegisterRequest request;
//
//        @BeforeEach
//        public void setup() {
//            request = new RegisterRequest(targetUser.getFirstName(), targetUser.getLastName(), targetUser.getAlias(), password, targetUser.getImageUrl());
//        }
//        @Test
//        @DisplayName("Should return valid user and auth token when user registers")
//        public void should_Return_ValidUserAndAuthToken_whenUserRegisters() {
//
//
//            try {
//                RegisterResponse response = serverFacade.register(request);
//
//                Assertions.assertTrue(response.isSuccess());
//                Assertions.assertNotNull(response);
//                Assertions.assertNotNull(response.getRegisteredUser());
//
//                User registeredUser = response.getRegisteredUser();
//                Assertions.assertEquals(targetUser.getFirstName(), registeredUser.getFirstName());
//                Assertions.assertEquals(targetUser.getLastName(), registeredUser.getLastName());
//                Assertions.assertEquals(targetUser.getAlias(), registeredUser.getAlias());
//                Assertions.assertEquals("https://braydon-cs340.s3.us-west-2.amazonaws.com/" + targetUser.getAlias(), registeredUser.getImageUrl());
//
//                Assertions.assertNotNull(response.getAuthToken());
//                Assertions.assertNotNull(response.getAuthToken().getToken());
//            } catch (Exception ex) {
//                Assertions.fail(ex.getMessage());
//            }
//        }
//
//        @Test
//        @DisplayName("Should fail when given no password")
//        public void should_Fail_whenGivenNoPassword() {
//            request.setPassword(null);
//            Assertions.assertThrows(TweeterRemoteException.class, () -> serverFacade.register(request));
//        }
//    }
//
//    @Nested
//    public class GetFollowersTests {
//        private FollowerRequest request;
//        private AuthToken authToken;
//
//
//        @BeforeEach
//        public void setup() {
//            authToken = createAuthToken();
//            request = new FollowerRequest(authToken, targetUser.getAlias(), 10, null);
//        }
//
//        @Test
//        @DisplayName("Should return page of users when get followers")
//        public void should_ReturnPageOfUsers_whenGetFollowers() {
//            try {
//                FollowerResponse response = serverFacade.getFollowers(request);
//
//                Assertions.assertTrue(response.isSuccess());
//                Assertions.assertNotNull(response);
//
//                List<User> users = response.getUsers();
//                Assertions.assertNotNull(users);
//                Assertions.assertNotEquals(0, users.size());
//                Assertions.assertTrue(response.getHasMorePages());
//            } catch (Exception ex) {
//                Assertions.fail(ex.getMessage());
//            }
//        }
//
//        @Test
//        @DisplayName("Should fail when given invalid limit")
//        public void should_fail_whenGivenInvalidLimit() {
//            request.setLimit(-1);
//
//            Assertions.assertThrows(TweeterRemoteException.class, () -> serverFacade.getFollowers(request));
//        }
//    }
//
//    @Nested
//    public class GetFollowersCountTests {
//        private FollowersCountRequest request;
//        private AuthToken authToken;
//
//        private static final String url = "";
//
//
//        @BeforeEach
//        public void setup() {
//            authToken = createAuthToken();
//
//            request = new FollowersCountRequest(authToken, targetUser.getAlias());
//        }
//
//        @Test
//        @DisplayName("Should return users when get followers")
//        public void should_Return_whenUsersGetFollowers() {
//            try {
//                FollowersCountResponse response = serverFacade.getFollowersCount(request);
//
//                Assertions.assertNotNull(response);
//
//                Assertions.assertTrue(response.isSuccess());
//                Assertions.assertNotEquals(0, response.getCount());
//            } catch (Exception ex) {
//                Assertions.fail(ex.getMessage());
//            }
//        }
//
//        @Test
//        @DisplayName("Should fail when given invalid user")
//        public void should_fail_whenGivenInvalidUser() {
//            request.setTargetUser(null);
//
//            Assertions.assertThrows(TweeterRemoteException.class, () -> serverFacade.getFollowersCount(request));
//        }
//    }
//
//    @Nested
//    @DisplayName("Test login")
//    public class LoginTest {
//        private AuthToken authToken;
//        private LoginRequest request;
//        @BeforeEach
//        public void setup() {
//            authToken = createAuthToken();
//            request = new LoginRequest(targetUser.getAlias(), password);
//        }
//
//        @Test
//        @DisplayName("Should login when login")
//        public void should_Login_whenLogin() {
//            try {
//                LoginResponse response = serverFacade.login(request);
//                Assertions.assertTrue(response.isSuccess());
//                Assertions.assertNotNull(response.getAuthToken());
//                Assertions.assertNotNull(response.getAuthToken().getToken());
//                Assertions.assertNotNull(response.getUser());
//                Assertions.assertEquals(targetUser, response.getUser());
//            } catch (Exception ex) {
//                Assertions.fail(ex.getMessage());
//            }
//
//        }
//
//        @Test
//        @DisplayName("Should fail when invalid login")
//        public void should_fail_whenInvalidLogin() {
//            request.setPassword("invalid");
//            try {
//                LoginResponse response = serverFacade.login(request);
//                Assertions.assertFalse(response.isSuccess());
//                Assertions.assertNotNull(response.getMessage());
//                Assertions.assertNull(response.getAuthToken());
//                Assertions.assertNull(response.getUser());
//            } catch (Exception ex) {
//                Assertions.fail(ex.getMessage());
//            }
//
//        }
//
//    }

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
