package com.cs204.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

//pk: followeralias
//sk: followeealias
public interface FollowDAO {
    DataPage<String> getPageOfFollowees(String targetUserAlias, int pageSize, String lastUserAlias);

    DataPage<String> getPageOfFollowers(String targetUserAlias, int pageSize, String lastUserAlias);

    void setFollower(String follower_name, String follower_handle, String followee_name, String followee_handle);

    void deleteFollower(String follower_handle, String followee_handle);

    boolean hasFollower(String follower_handle, String followee_handle);
}
