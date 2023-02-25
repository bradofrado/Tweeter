package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

@DisplayName("Main Presenter Tests")
public class MainPresenterTests {
    private MainPresenter classUnderTest;
    private MainPresenter.View mockView;
    private StatusService mockStatusService;
    private Cache mockCache;
    private User mockSelectedUser;

    @BeforeEach
    public void setup() {
        mockView = Mockito.mock(MainPresenter.View.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockSelectedUser = Mockito.mock(User.class);
        mockCache = Mockito.mock(Cache.class);
        classUnderTest = Mockito.spy(new MainPresenter(mockView, mockSelectedUser));
        Mockito.when(classUnderTest.getStatusService()).thenReturn(mockStatusService);
        Mockito.when(mockCache.getCurrUser()).thenReturn(new User("Bob", "Jones", "@bobo", "png"));

        Cache.setInstance(mockCache);
    }

    @Nested
    @DisplayName("Post Status Tests")
    public class PostStatusTests {
        @Test
        @DisplayName("Test Successful Post Status")
        public void testSuccessfulPostStatus() {
            Mockito.doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    MainPresenter.PostStatusObserver observer = invocation.getArgument(1, MainPresenter.PostStatusObserver.class);
                    observer.handleSuccess();
                    return null;
                }
            }).when(mockStatusService).postStatus(Mockito.any(), Mockito.any());

            classUnderTest.postStatus("My post");

            Mockito.verify(mockStatusService).postStatus(Mockito.any(Status.class), Mockito.any(MainPresenter.PostStatusObserver.class));
            Mockito.verify(mockView).displayInfoMessage("Posting Status...");
            Mockito.verify(mockView).cancelInfoMessage();
            Mockito.verify(mockView).displayMessage("Successfully Posted!");
        }

        @Test
        @DisplayName("Test Post Status Failure")
        public void testPostStatusFailure() {
            Mockito.doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    MainPresenter.PostStatusObserver observer = invocation.getArgument(1, MainPresenter.PostStatusObserver.class);
                    observer.handleError("the error message");
                    return null;
                }
            }).when(mockStatusService).postStatus(Mockito.any(), Mockito.any());

            classUnderTest.postStatus("My post");

            Mockito.verify(mockStatusService).postStatus(Mockito.any(Status.class), Mockito.any(MainPresenter.PostStatusObserver.class));
            Mockito.verify(mockView).displayInfoMessage("Posting Status...");
            Mockito.verify(mockView).cancelInfoMessage();
            Mockito.verify(mockView).displayMessage("Failed to post status: the error message");
        }

        @Test
        @DisplayName("Test Post Status Failure Exception")
        public void testPostStatusFailureException() {
            Mockito.doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    MainPresenter.PostStatusObserver observer = invocation.getArgument(1, MainPresenter.PostStatusObserver.class);
                    observer.handleException(new Exception("the exception"));
                    return null;
                }
            }).when(mockStatusService).postStatus(Mockito.any(), Mockito.any());

            classUnderTest.postStatus("My post");

            Mockito.verify(mockStatusService).postStatus(Mockito.any(Status.class), Mockito.any(MainPresenter.PostStatusObserver.class));
            Mockito.verify(mockView).displayInfoMessage("Posting Status...");
            Mockito.verify(mockView).cancelInfoMessage();
            Mockito.verify(mockView).displayMessage("Failed to post status because of exception: the exception");
        }
    }
}
