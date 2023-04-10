package com.cs204.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserResponse;

//pk: alias
//sk: none
public interface UserDAO {
    User getUser(String alias);
    User getUser(String username, String password);
    void setUser(String alias, String firstName, String lastName, String imageUrl, String password);
    void deleteUser(String alias);
    Integer getFolloweeCount(String follower);
    Integer getFollowerCount(String follower);
    void setFollowerCount(String follower, int count);
    void setFolloweeCount(String followee, int count);

}
