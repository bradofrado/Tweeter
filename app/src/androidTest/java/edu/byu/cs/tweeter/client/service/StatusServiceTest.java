package edu.byu.cs.tweeter.client.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusServiceTest {
    private User currentUser;
    private AuthToken currentAuthToken;

    private StatusService statusServiceSpy;
    private StatusServiceObserver observer;

    private CountDownLatch countDownLatch;

    /**
     * Create a StatusService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() {
        currentUser = new User("FirstName", "LastName", null);
        currentAuthToken = new AuthToken();

        statusServiceSpy = Mockito.spy(new StatusService());

        // Setup an observer for the StatusService
        observer = Mockito.spy(new StatusServiceObserver());

        // Prepare the countdown latch
        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    /**
     * A {@link PagedTaskObserver <User>} implementation that can be used to get the values
     * eventually returned by an asynchronous call on the {@link StatusService}. Counts down
     * on the countDownLatch so tests can wait for the background thread to call a method on the
     * observer.
     */
    private class StatusServiceObserver implements PagedTaskObserver<Status> {

        private boolean success;
        private String message;
        private List<Status> statuses;
        private boolean hasMorePages;
        private Exception exception;

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.statuses = statuses;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleError(String message) {
            this.success = false;
            this.message = message;
            this.statuses = null;
            this.hasMorePages = false;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleException(Exception exception) {
            this.success = false;
            this.message = null;
            this.statuses = null;
            this.hasMorePages = false;
            this.exception = exception;

            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public List<Status> getStatuses() {
            return statuses;
        }

        public boolean getHasMorePages() {
            return hasMorePages;
        }

        public Exception getException() {
            return exception;
        }
    }

    /**
     * Verify that for successful requests, the {@link StatusService#getStatuses}
     * asynchronous method eventually returns the same result as the {@link ServerFacade}.
     */
    @Test
    public void testGetStatuses_validRequest_correctResponse() throws InterruptedException {
        statusServiceSpy.getStatuses(currentUser, 3, null, observer);
        awaitCountDownLatch();

        List<Status> expectedStatuses = FakeData.getInstance().getFakeStatuses().subList(0, 3);
        Mockito.verify(observer).handleSuccess(expectedStatuses, true);

        Assertions.assertTrue(observer.isSuccess());
        Assertions.assertNull(observer.getMessage());
        Assertions.assertEquals(expectedStatuses, observer.getStatuses());
        Assertions.assertTrue(observer.getHasMorePages());
        Assertions.assertNull(observer.getException());
    }


    /**
     * Verify that for unsuccessful requests, the the {@link StatusService#getStatuses}
     * method returns the same failure response as the server facade.
     */
    @Test
    public void testGetStatuses_invalidRequest_returnsNoStatuses() throws InterruptedException {
        statusServiceSpy.getStatuses(null, 0, null, observer);
        awaitCountDownLatch();

        Assertions.assertFalse(observer.isSuccess());
        Assertions.assertNull(observer.getMessage());
        Assertions.assertNull(observer.getStatuses());
        Assertions.assertFalse(observer.getHasMorePages());
        Assertions.assertNotNull(observer.getException());
    }
}
