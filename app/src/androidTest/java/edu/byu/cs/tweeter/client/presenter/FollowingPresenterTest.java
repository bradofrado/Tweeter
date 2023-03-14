package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenterTest {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    private final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
    private final User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);
    private final User user3 = new User("Bob", "Bobson", MALE_IMAGE_URL);
    private final User user4 = new User("Bonnie", "Beatty", FEMALE_IMAGE_URL);
    private final User user5 = new User("Chris", "Colston", MALE_IMAGE_URL);

    private User fakeUser;
    private AuthToken fakeAuthToken;
    private FollowService followingServiceMock;
    private GetFollowingPresenter followingPresenterSpy;
    private GetFollowingPresenter.View followingViewMock;

    /**
     * Setup mocks and spies needed to let test cases control what users are returned
     * by {@link FollowService}.
     * Setup mock {@link GetFollowingPresenter} to verify that {@link GetFollowingPresenter}
     * correctly calls view methods.
     */
    @BeforeEach
    public void setup() {
        fakeUser = new User("Paul", "Bunyon", "@Paul_Bunyon_123", "https://s3.amazon.com/paul_bunyon");
        fakeAuthToken = new AuthToken("abc-123-xyz-789", "August 12, 2021 3:01 PM");

        // followingViewMock is used to verify that FollowingPresenter correctly calls view methods.
        followingViewMock = Mockito.mock(GetFollowingPresenter.View.class);

        // Create the mocks and spies needed to let test cases control what users are returned
        // FollowService.
        GetFollowingPresenter followingPresenter = new GetFollowingPresenter(followingViewMock, fakeUser);
        followingPresenterSpy = Mockito.spy(followingPresenter);

        followingServiceMock = Mockito.mock(FollowService.class);
        Mockito.doReturn(followingServiceMock).when(followingPresenterSpy).getFollowService();
    }

    /**
     * Verify that {@link GetFollowingPresenter} has the correct initial state.
     */
    @Test
    public void testInitialPresenterState() {
        Assertions.assertNull(followingPresenterSpy.getLastItem());
        Assertions.assertTrue(followingPresenterSpy.hasMorePages());
        Assertions.assertFalse(followingPresenterSpy.isLoading());
    }

    @Test
    public void testLoadMoreItems_CorrectParamsPassedToStatusService() {
        List<User> followees = Arrays.asList(user1, user2, user3, user4, user5);

        Answer<Void> manyFolloweesAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                User user = invocation.getArgument(0);
                int limit = invocation.getArgument(1);
                User lastFollowee = invocation.getArgument(2);

                // Assert that the parameters are correct
                Assertions.assertEquals(fakeUser, user);
                //Assertions.assertEquals(fakeAuthToken, authToken);
                Assertions.assertEquals(limit, GetFollowingPresenter.PAGE_SIZE);
                Assertions.assertEquals(lastFollowee, followingPresenterSpy.getLastItem());

                PagedTaskObserver<User> observer = invocation.getArgument(3);
                observer.handleSuccess(followees, true);
                return null;
            }
        };
        Mockito.doAnswer(manyFolloweesAnswer).when(followingServiceMock).getMoreFollowing(Mockito.any(), Mockito.anyInt(), Mockito.any(), Mockito.any());
        followingPresenterSpy.loadMoreItems();
    }



    /**
     * Verify that {@link GetFollowingPresenter#loadMoreItems} works correctly when there
     * are some pages of followees.
     */
    @Test
    public void testLoadMoreItems_GetFolloweesSuccess() throws InterruptedException {
        List<User> followees = Arrays.asList(user1, user2, user3, user4, user5);

        Answer<Void> manyFolloweesAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                PagedTaskObserver<User> observer = invocation.getArgument(3);
                observer.handleSuccess(followees, true);
                return null;
            }
        };
        Mockito.doAnswer(manyFolloweesAnswer).when(followingServiceMock).getMoreFollowing(Mockito.any(), Mockito.anyInt(), Mockito.any(), Mockito.any());

        followingPresenterSpy.loadMoreItems();

        Assertions.assertEquals(user5, followingPresenterSpy.getLastItem());
        Assertions.assertTrue(followingPresenterSpy.hasMorePages());
        Assertions.assertFalse(followingPresenterSpy.isLoading());

//        Mockito.verify(followingViewMock).setLoading(true);
//        Mockito.verify(followingViewMock).setLoading(false);
        Mockito.verify(followingViewMock).addMoreItems(Arrays.asList(user1, user2, user3, user4, user5));
    }

    /**
     * Verify that {@link GetFollowingPresenter#loadMoreItems} works correctly when there
     * are between two and three pages of followees.
     */
    @Test
    public void testLoadMoreItems_GetFolloweesFailsWithErrorMessage() throws InterruptedException {
        Answer<Void> failureAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                PagedTaskObserver<User> observer = invocation.getArgument(3);
                observer.handleError("failure message");
                return null;
            }
        };
        Mockito.doAnswer(failureAnswer).when(followingServiceMock).getMoreFollowing(Mockito.any(), Mockito.anyInt(), Mockito.any(), Mockito.any());

        followingPresenterSpy.loadMoreItems();

        Assertions.assertFalse(followingPresenterSpy.isLoading());

//        Mockito.verify(followingViewMock).setLoading(true);
//        Mockito.verify(followingViewMock).setLoading(false);
        Mockito.verify(followingViewMock).displayMessage("Failed to retrieve followees: " + "failure message");
        Mockito.verify(followingViewMock, Mockito.times(0)).addMoreItems(Mockito.any());
    }

    @Test
    public void testLoadMoreItems_GetFolloweesFailsWithExceptionMessage() throws InterruptedException {
        Answer<Void> exceptionAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                PagedTaskObserver<User> observer = invocation.getArgument(3);
                observer.handleException(new Exception("The exception message"));
                return null;
            }
        };
        Mockito.doAnswer(exceptionAnswer).when(followingServiceMock).getMoreFollowing(Mockito.any(), Mockito.anyInt(), Mockito.any(), Mockito.any());

        followingPresenterSpy.loadMoreItems();


        Assertions.assertFalse(followingPresenterSpy.isLoading());

//        Mockito.verify(followingViewMock).setLoading(true);
//        Mockito.verify(followingViewMock).setLoading(false);
        Mockito.verify(followingViewMock).displayMessage("Failed to retrieve followees because of exception: " + "The exception message");
        Mockito.verify(followingViewMock, Mockito.times(0)).addMoreItems(Mockito.any());
    }


}