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
    public void setUser(String alias, String firstName, String lastName, String imageUrl) {
        setItem(alias, null, new ItemSetter<>() {
            @Override
            public void setItem(UserBean item) {
                item.setAlias(alias);
                item.setFirstName(firstName);
                item.setLastName(lastName);
                item.setImageUrl(imageUrl);
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
