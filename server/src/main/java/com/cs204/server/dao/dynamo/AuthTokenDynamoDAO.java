package com.cs204.server.dao.dynamo;

import com.cs204.server.dao.AuthTokenDAO;
import com.cs204.server.dao.DataPage;
import com.cs204.server.dao.dynamo.model.AuthTokenBean;

import java.util.HashMap;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class AuthTokenDynamoDAO extends DynamoDAO<AuthTokenBean> implements AuthTokenDAO {
    private static final String TableName = "authToken";

    public AuthTokenDynamoDAO() {
        super(TableName);
    }

    @Override
    public String getUser(AuthToken authToken) {
        AuthTokenBean authTokenBean = getItem(authToken.getToken(), null);
        if (authTokenBean.getTimeout() > System.currentTimeMillis()) {
            return authTokenBean.getAlias();
        }

        return null;
    }

    @Override
    public AuthToken setAuthToken(AuthToken authToken, String alias, long timeout) {
        setItem(authToken.getToken(), null, new ItemSetter<AuthTokenBean>() {
            @Override
            public void setItem(AuthTokenBean item) {
                item.setToken(authToken.getToken());
                item.setAlias(alias);
                item.setTimeout(timeout);
            }

            @Override
            public AuthTokenBean newItem() {
                return new AuthTokenBean();
            }
        });

        return authToken;
    }

    @Override
    public void deleteAuthToken(AuthToken authToken) {
        deleteItem(authToken.getToken(), null);
    }
}
