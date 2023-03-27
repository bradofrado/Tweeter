package com.cs204.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserResponse;

//pk: alias
//sk: none
public interface UserDAO {
    User getUser(String alias);
}
