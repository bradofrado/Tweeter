package com.cs204.server.module;

import com.cs204.server.dao.AuthTokenDAO;
import com.cs204.server.dao.DataPage;
import com.cs204.server.dao.FeedDAO;
import com.cs204.server.dao.FollowDAO;
import com.cs204.server.dao.ImageDAO;
import com.cs204.server.dao.StoryDAO;
import com.cs204.server.dao.UserDAO;
import com.cs204.server.dao.s3.ImageS3DAO;
import com.cs204.server.service.FollowService;
import com.cs204.server.service.StatusService;
import com.cs204.server.service.UserService;
import com.google.inject.AbstractModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class TestModule extends AbstractModule {
    @Override
    public void configure() {
        bind(FollowService.class).asEagerSingleton();
        bind(UserService.class).asEagerSingleton();
        bind(StatusService.class).asEagerSingleton();
        bind(FollowDAO.class).toInstance(new FakeFollowDAO());
        bind(UserDAO.class).toInstance(new FakeUserDAO());
        bind(FeedDAO.class).toInstance(new FakeFeedDAO());
        bind(StoryDAO.class).toInstance(new FakeStoryDAO());
        bind(AuthTokenDAO.class).toInstance(new FakeAuthTokenDAO());
        bind(ImageDAO.class).toInstance(new FakeImageDAO());
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy followees.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }

    private class FakeFollowDAO implements FollowDAO {
        @Override
        public DataPage<String> getPageOfFollowees(String targetUserAlias, int pageSize, String lastUserAlias) {
            List<User> allFollowees = getDummyFollowees();
            List<String> responseFollowees = new ArrayList<>(pageSize);

            boolean hasMorePages = false;

            if(pageSize > 0) {
                if (allFollowees != null) {
                    int followeesIndex = getFolloweesStartingIndex(lastUserAlias, allFollowees);

                    for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < pageSize; followeesIndex++, limitCounter++) {
                        responseFollowees.add(allFollowees.get(followeesIndex).getAlias());
                    }

                    hasMorePages = followeesIndex < allFollowees.size();
                }
            }

            return new DataPage<>(responseFollowees, hasMorePages);
        }

        @Override
        public DataPage<String> getPageOfFollowers(String targetUserAlias, int pageSize, String lastUserAlias) {
            List<User> allFollowers = getDummyFollowers();
            List<String> responseFollowers = new ArrayList<>(pageSize);

            boolean hasMorePages = false;

            if(pageSize > 0) {
                if (allFollowers != null) {
                    int followeesIndex = getFollowersStartingIndex(lastUserAlias, allFollowers);

                    for(int limitCounter = 0; followeesIndex < allFollowers.size() && limitCounter < pageSize; followeesIndex++, limitCounter++) {
                        responseFollowers.add(allFollowers.get(followeesIndex).getAlias());
                    }

                    hasMorePages = followeesIndex < allFollowers.size();
                }
            }

            return new DataPage<>(responseFollowers, hasMorePages);
        }

        @Override
        public void setFollower(String follower_name, String follower_handle, String followee_name, String followee_handle) {

        }

        @Override
        public void deleteFollower(String follower_handle, String followee_handle) {

        }

        @Override
        public boolean hasFollower(String follower_handle, String followee_handle) {
            return new Random().nextInt() > 0;
        }

        /**
         * Determines the index for the first followee in the specified 'allFollowees' list that should
         * be returned in the current request. This will be the index of the next followee after the
         * specified 'lastFollowee'.
         *
         * @param lastFolloweeAlias the alias of the last followee that was returned in the previous
         *                          request or null if there was no previous request.
         * @param allFollowees the generated list of followees from which we are returning paged results.
         * @return the index of the first followee to be returned.
         */
        private int getFolloweesStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {

            int followeesIndex = 0;

            if(lastFolloweeAlias != null) {
                // This is a paged request for something after the first page. Find the first item
                // we should return
                for (int i = 0; i < allFollowees.size(); i++) {
                    if(lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
                        // We found the index of the last item returned last time. Increment to get
                        // to the first one we should return
                        followeesIndex = i + 1;
                        break;
                    }
                }
            }

            return followeesIndex;
        }

        /**
         * Determines the index for the first followee in the specified 'allFollowees' list that should
         * be returned in the current request. This will be the index of the next followee after the
         * specified 'lastFollowee'.
         *
         * @param lastFollowerAlias the alias of the last followee that was returned in the previous
         *                          request or null if there was no previous request.
         * @param allFollowers the generated list of followees from which we are returning paged results.
         * @return the index of the first followee to be returned.
         */
        private int getFollowersStartingIndex(String lastFollowerAlias, List<User> allFollowers) {

            int followersIndex = 0;

            if(lastFollowerAlias != null) {
                // This is a paged request for something after the first page. Find the first item
                // we should return
                for (int i = 0; i < allFollowers.size(); i++) {
                    if(lastFollowerAlias.equals(allFollowers.get(i).getAlias())) {
                        // We found the index of the last item returned last time. Increment to get
                        // to the first one we should return
                        followersIndex = i + 1;
                        break;
                    }
                }
            }

            return followersIndex;
        }

        /**
         * Returns the list of dummy followee data. This is written as a separate method to allow
         * mocking of the followees.
         *
         * @return the followees.
         */
        List<User> getDummyFollowees() {
            return getFakeData().getFakeUsers();
        }

        /**
         * Returns the list of dummy follower data. This is written as a separate method to allow
         * mocking of the followers.
         *
         * @return the followees.
         */
        List<User> getDummyFollowers() {
            return getFakeData().getFakeUsers();
        }
    }

    private class FakeUserDAO implements UserDAO {

        @Override
        public User getUser(String alias) {
            return getFakeData().findUserByAlias(alias);
        }

        @Override
        public User getUser(String username, String password) {
            return getFakeData().getFakeUsers().get(0);
        }

        @Override
        public void setUser(String alias, String firstName, String lastName, String imageUrl, String password) {

        }

        @Override
        public void deleteUser(String alias) {

        }

        @Override
        public Integer getFolloweeCount(String follower) {
            return 20;
        }

        @Override
        public Integer getFollowerCount(String follower) {
            return 20;
        }

        @Override
        public void setFollowerCount(String follower, int count) {

        }

        @Override
        public void setFolloweeCount(String followee, int count) {

        }
    }

    private class FakeFeedDAO implements FeedDAO {

        @Override
        public DataPage<Status> getPageOfFeeds(String alias, int pageSize, Long lastPosted) {
            Status status = new Status();
            //status.setUser(new User(lastPosted));
            Pair<List<Status>, Boolean> pageOfStatus = getFakeData().getPageOfStatus(status, pageSize);

            return new DataPage<>(pageOfStatus.getFirst(), pageOfStatus.getSecond());
        }

        @Override
        public void setFeed(String alias, String post, String user, Long time, List<String> urls, List<String> mentions) {

        }

        @Override
        public Status getFeed(String alias) {
            return null;
        }

        @Override
        public void deleteFeed(String alias) {

        }
    }

    private class FakeStoryDAO implements StoryDAO {

        @Override
        public DataPage<Status> getPageOfStories(String alias, int pageSize, Long lastPosted) {
            Status status = new Status();
            //status.setUser(new User(lastPosted));
            Pair<List<Status>, Boolean> pageOfStatus = getFakeData().getPageOfStatus(status, pageSize);

            return new DataPage<>(pageOfStatus.getFirst(), pageOfStatus.getSecond());
        }

        @Override
        public void setStory(String post, String user, Long time, List<String> urls, List<String> mentions) {

        }

        @Override
        public Status getStory(String alias) {
            return null;
        }

        @Override
        public void deleteStory(String alias) {

        }
    }

    private class FakeAuthTokenDAO implements AuthTokenDAO {

        @Override
        public String getUser(AuthToken authToken) {
            return getFakeData().getFakeUsers().get(0).getAlias();
        }

        @Override
        public AuthToken setAuthToken(AuthToken authToken, String alias, long timeout) {
            return authToken;
        }

        @Override
        public void deleteAuthToken(AuthToken authToken) {

        }
    }

    private class FakeImageDAO implements ImageDAO {

        @Override
        public String uploadImage(String name, String byte64Image) {
            return ImageS3DAO.LINK + name;
        }
    }
}
