package com.cs204.server.dao.dynamo;

import com.cs204.server.dao.UserDAO;
import com.cs204.server.dao.dynamo.model.FollowerBean;
import com.cs204.server.dao.dynamo.model.UserBean;

import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class UserDynamoDAO implements UserDAO {
    private static final String TableName = "user";

    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    @Override
    public User getUser(String alias) {
        return getUserBean(alias, startTransaction()).getUser();
    }

    @Override
    public void setUser(String alias, String firstName, String lastName, String imageUrl) {
        DynamoDbTable<UserBean> table = startTransaction();
        UserBean user = getUserBean(alias, table);
        if(user != null) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setImageUrl(imageUrl);
            table.updateItem(user);
        } else {
            user = new UserBean();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setImageUrl(imageUrl);
            table.putItem(user);
        }
    }

    @Override
    public void deleteUser(String alias) {
        DynamoDbTable<UserBean> table = startTransaction();
        table.deleteItem(getKey(alias));
    }

    private UserBean getUserBean(String alias, DynamoDbTable<UserBean> table) {
        // load it if it exists
        return table.getItem(getKey(alias));
    }

    private DynamoDbTable<UserBean> startTransaction() {
        DynamoDbTable<UserBean> table = enhancedClient.table(TableName, TableSchema.fromBean(UserBean.class));
        return table;
    }

    private Key getKey(String alias) {
        return Key.builder()
        .partitionValue(alias)
        .build();
    }
}
