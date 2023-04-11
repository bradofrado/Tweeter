package com.cs204.server.dao.dynamo;

import com.cs204.server.dao.DataPage;
import com.cs204.server.dao.FollowDAO;
import com.cs204.server.dao.dynamo.model.FollowerBean;
import com.cs204.server.dao.dynamo.model.UserBean;

import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class DynamoDAO<T> {
    protected interface ItemSetter<T> {
        void setItem(T item);
        T newItem();
    }

    private String TableName;

    private static DynamoDbEnhancedClient client = null;
    protected static DynamoDbEnhancedClient getClient() {
        if (client == null) {
            DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                    .region(Region.US_WEST_2)
                    .build();

            client = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();
        }

        return client;
    }


    public DynamoDAO(String tableName) {
        TableName = tableName;
    }

    /**
     * Gets a follower object using the follower_handle and followee_handle
     * @param partitionKey
     * @param sortKey
     * @return
     */
    public T getItem(String partitionKey, String sortKey) {
        DynamoDbTable<T> table = getClient().table(TableName, TableSchema.fromBean(getType()));

        T item = table.getItem(getKey(partitionKey, sortKey));
        return item;
    }


    /**
     * Updates a follower if the given follower_handle exists, creates a new one if it does not exist
     *
     */
    public void setItem(String partitionKey, String sortKey, ItemSetter<T> setter) {
        DynamoDbTable<T> table = startTransaction();
        // load it if it exists
        T newItem = table.getItem(getKey(partitionKey, sortKey));
        if(newItem != null) {
            setter.setItem(newItem);
            table.updateItem(newItem);
        } else {
            newItem = setter.newItem();
            setter.setItem(newItem);
            table.putItem(newItem);
        }
    }

    public void deleteItem(String partitionKey, String sortKey) {
        DynamoDbTable<T> table = startTransaction();
        table.deleteItem(getKey(partitionKey, sortKey));
    }

    private Class<T> getType() {
        return (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected DynamoDbTable<T> startTransaction() {
        DynamoDbTable<T> table = getClient().table(TableName, TableSchema.fromBean(getType()));
        return table;
    }

    protected Key getKey(String partitionKey, String sortKey) {
        var key = Key.builder()
                .partitionValue(partitionKey);
        if (sortKey != null) {
            key.sortValue(sortKey);
        }

        return key.build();
    }

    protected void writeChunkOfDTOs(List<T> userDTOs) {
        if(userDTOs.size() > 25)
            throw new RuntimeException("Too many users to write");

        DynamoDbTable<T> table = startTransaction();

        WriteBatch.Builder<T> writeBuilder = WriteBatch.builder(getType()).mappedTableResource(table);
        for (T item : userDTOs) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = getClient().batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfDTOs(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    protected String getTableName() {
        return TableName;
    }
}
