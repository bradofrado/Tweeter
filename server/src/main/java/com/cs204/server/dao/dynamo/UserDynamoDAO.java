package com.cs204.server.dao.dynamo;

import com.cs204.server.dao.UserDAO;
import com.cs204.server.dao.dynamo.model.UserBean;

import edu.byu.cs.tweeter.model.domain.User;

public class UserDynamoDAO extends DynamoDAO<UserBean> implements UserDAO {
    private static final String TableName = "user";
    public UserDynamoDAO() {
        super(TableName);
    }

    @Override
    public User getUser(String alias) {
        return getItem(alias, null).getUser();
    }

    @Override
    public User getUser(String username, String password) {
        UserBean userBean = getItem(username, null);
        if (userBean.getPassword().equals(password)) {
            return userBean.getUser();
        }

        return null;
    }

    @Override
    public void setUser(String alias, String firstName, String lastName, String imageUrl, String password) {
        setItem(alias, null, new ItemSetter<>() {
            @Override
            public void setItem(UserBean item) {
                item.setAlias(alias);
                item.setFirstName(firstName);
                item.setLastName(lastName);
                item.setImageUrl(imageUrl);
                item.setPassword(password);
            }

            @Override
            public UserBean newItem() {
                return new UserBean();
            }
        });
    }

    @Override
    public void deleteUser(String alias) {
        deleteItem(alias, null);
    }
}
