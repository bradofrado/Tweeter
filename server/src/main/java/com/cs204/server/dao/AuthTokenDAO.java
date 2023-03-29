package com.cs204.server.dao;

//pk: authtoken
//sk: timeout

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

//authtoken, timeout, alias
public interface AuthTokenDAO {
    String getUser(AuthToken authToken);
}
