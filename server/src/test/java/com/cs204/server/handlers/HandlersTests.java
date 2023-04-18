package com.cs204.server.handlers;

import com.cs204.server.lambda.PostStatusHandler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.util.Timestamp;

@DisplayName("Handler Tests")
public class HandlersTests {
    @Nested
    @DisplayName("Post Status Test")
    public class PostStatusTest {
        PostStatusHandler handler = new PostStatusHandler();
        @Test
        @DisplayName("Should work")
        public void shouldWork() {
            AuthToken authToken = new AuthToken("58584392-5a34-42c2-b021-3400267151eb");
            Status status = new Status("Test post", new User("@bradofrado"),
                    Timestamp.getFormattedDate(System.currentTimeMillis()), new ArrayList<>(), new ArrayList<>());
            PostStatusRequest request = new PostStatusRequest(authToken, status);
            PostStatusResponse response = handler.handleRequest(request);
            Assertions.assertTrue(response.isSuccess());
        }
    }
}
