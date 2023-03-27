package com.cs204.server.dao.dynamo;

import com.cs204.server.dao.DataPage;
import com.cs204.server.dao.FollowDAO;

import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FollowDynamoDAO implements FollowDAO {
    private static final String TableName = "follows";
    public static final String IndexName = "follows_index";

    private static final String FollowerAttr = "follower_handle";
    private static final String FolloweeAttr = "followee_handle";

    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    /**
     * Gets a follower object using the follower_handle and followee_handle
     * @param follower_handle
     * @param followee_handle
     * @return
     */
    public Follower getFollowsItem(String follower_handle, String followee_handle) {
        DynamoDbTable<Follower> table = enhancedClient.table(TableName, TableSchema.fromBean(Follower.class));
        Key key = Key.builder()
                .partitionValue(follower_handle).sortValue(followee_handle)
                .build();

        Follower visit = table.getItem(key);
        return visit;
    }


    /**
     * Updates a follower if the given follower_handle exists, creates a new one if it does not exist
     *
     * @param follower_handle
     * @param follower_name
     * @param followee_handle
     * @param followee_name
     */
    public void setFollower(String follower_name, String follower_handle, String followee_name, String followee_handle) {
        DynamoDbTable<Follower> table = enhancedClient.table(TableName, TableSchema.fromBean(Follower.class));
        Key key = Key.builder()
                .partitionValue(follower_handle).sortValue(followee_handle)
                .build();

        // load it if it exists
        Follower follower = table.getItem(key);
        if(follower != null) {
            follower.setFollower_handle(follower_handle);
            follower.setFollower_name(follower_name);
            follower.setFollowee_handle(followee_handle);
            follower.setFollowee_name(followee_name);
            table.updateItem(follower);
        } else {
            Follower newFollower = new Follower();
            newFollower.setFollower_handle(follower_handle);
            newFollower.setFollower_name(follower_name);
            newFollower.setFollowee_handle(followee_handle);
            newFollower.setFollowee_name(followee_name);
            table.putItem(newFollower);
        }
    }

    /**
     * Delete a follower given its follower_handle and followee_handle
     * @param follower_handle
     * @param followee_handle
     */
    public void deleteFollower(String follower_handle, String followee_handle) {
        DynamoDbTable<Follower> table = enhancedClient.table(TableName, TableSchema.fromBean(Follower.class));
        Key key = Key.builder()
                .partitionValue(follower_handle).sortValue(followee_handle)
                .build();
        table.deleteItem(key);
    }

    public DataPage<String> getPageOfFollowers(String targetUserAlias, int pageSize, String lastUserAlias ) {
        DynamoDbTable<Follower> table = enhancedClient.table(TableName, TableSchema.fromBean(Follower.class));
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(isNonEmptyString(lastUserAlias)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(FolloweeAttr, AttributeValue.builder().s(lastUserAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<Follower> result = new DataPage<Follower>();

        PageIterable<Follower> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<Follower> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });

        return new DataPage<>(result.getValues().stream().map(f -> f.getFollower_handle()).collect(Collectors.toList()), result.isHasMorePages());
    }

    @Override
    public Integer getFolloweeCount(String follower) {
        return null;
    }

    public DataPage<String> getPageOfFollowees(String targetUserAlias, int pageSize, String lastUserAlias ) {
        DynamoDbIndex<Follower> index=enhancedClient.table(TableName, TableSchema.fromBean(Follower.class)).index(IndexName);
        Key key=Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder=QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if (isNonEmptyString(lastUserAlias)) {
            Map<String, AttributeValue> startKey=new HashMap<>();
            startKey.put(FolloweeAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(FollowerAttr, AttributeValue.builder().s(lastUserAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request=requestBuilder.build();

        DataPage<Follower> result=new DataPage<Follower>();

        SdkIterable<Page<Follower>> sdkIterable=index.query(request);
        PageIterable<Follower> pages=PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<Follower> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });

        return new DataPage<>(result.getValues().stream().map(f -> f.getFollowee_handle()).collect(Collectors.toList()), result.isHasMorePages());
    }
}
