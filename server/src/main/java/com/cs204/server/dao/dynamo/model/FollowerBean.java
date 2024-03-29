package com.cs204.server.dao.dynamo.model;

import com.cs204.server.dao.dynamo.FollowDynamoDAO;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class FollowerBean {
    private String follower_handle;
    private String follower_name;
    private String followee_handle;
    private String followee_name;

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = FollowDynamoDAO.IndexName)
    public String getFollower_handle() {
        return follower_handle;
    }

    public void setFollower_handle(String follower_handle) {
        this.follower_handle = follower_handle;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = FollowDynamoDAO.IndexName)
    public String getFollowee_handle() {
        return followee_handle;
    }

    public void setFollowee_handle(String followee_handle) {
        this.followee_handle = followee_handle;
    }


    public String getFollower_name() {
        return follower_name;
    }

    public void setFollower_name(String follower_name) {
        this.follower_name=follower_name;
    }

    public String getFollowee_name() {
        return followee_name;
    }

    public void setFollowee_name(String followee_name) {
        this.followee_name=followee_name;
    }
}
