package com.cs204.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

//pk: followeralias
//sk: followeealias
public interface FollowDAO {
    Integer getFolloweeCount(String follower);

    DataPage<String> getPageOfFollowees(String targetUserAlias, int pageSize, String lastUserAlias);

    DataPage<String> getPageOfFollowers(String targetUserAlias, int pageSize, String lastUserAlias);
}
