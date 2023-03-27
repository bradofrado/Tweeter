package com.cs204.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

import com.cs204.server.dao.DataPage;
import com.cs204.server.dao.FollowDAO;
import com.cs204.server.dao.UserDAO;
import com.cs204.server.dao.dynamo.FollowDynamoDAO;

public class FollowServiceTest {

    private FollowingRequest request;
    private FollowingResponse expectedResponse;
    private FollowDAO mockFollowDAO;
    private UserDAO mockUserDAO;
    private FollowService followServiceSpy;

    @BeforeEach
    public void setup() {
        AuthToken authToken = new AuthToken();

        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup a request object to use in the tests
        request = new FollowingRequest(authToken, currentUser.getAlias(), 3, null);

        // Setup a mock FollowDAO that will return known responses
        DataPage<String> daoResponse = new DataPage<String>();
        daoResponse.setValues(Arrays.asList(resultUser1.getAlias(), resultUser2.getAlias(), resultUser3.getAlias()));
        daoResponse.setHasMorePages(false);

        expectedResponse = new FollowingResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), daoResponse.isHasMorePages());
        mockFollowDAO = Mockito.mock(FollowDynamoDAO.class);
        Mockito.when(mockFollowDAO.getPageOfFollowees(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias())).thenReturn(daoResponse);

        mockUserDAO = Mockito.spy(UserDAO.class);
        Mockito.when(mockUserDAO.getUser(resultUser1.getAlias())).thenReturn(resultUser1);
        Mockito.when(mockUserDAO.getUser(resultUser2.getAlias())).thenReturn(resultUser2);
        Mockito.when(mockUserDAO.getUser(resultUser3.getAlias())).thenReturn(resultUser3);

        followServiceSpy = Mockito.spy(new FollowService(mockFollowDAO, mockUserDAO));
    }

    /**
     * Verify that the {@link FollowService#getFollowees(FollowingRequest)}
     * method returns the same result as the {@link FollowDynamoDAO} class.
     */
    @Test
    public void testGetFollowees_validRequest_correctResponse() {
        FollowingResponse response = followServiceSpy.getFollowees(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}