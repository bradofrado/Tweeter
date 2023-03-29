package com.cs204.server.service;

import com.cs204.server.dao.AuthTokenDAO;
import com.cs204.server.dao.UserDAO;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticatedService {
    protected AuthTokenDAO authTokenDAO;
    protected UserDAO userDAO;
    public AuthenticatedService(AuthTokenDAO authTokenDAO, UserDAO userDAO) {
        this.authTokenDAO = authTokenDAO;
        this.userDAO = userDAO;
    }

    protected User getAuthenticatedUser(AuthToken authToken) {
        String alias = authTokenDAO.getUser(authToken);
        if (alias == null) {
            throw new RuntimeException("[Bad Request] Bad or expired auth token");
        }
        return userDAO.getUser(alias);
    }
}
