package com.cs204.server.dao.dynamo;

import com.cs204.server.dao.DataPage;
import com.cs204.server.dao.dynamo.model.FollowerBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public abstract class PageDynamoDAO<T> extends DynamoDAO<T> {
    private String partitionKey;
    private String sortKey;
    private String indexKey;

    public PageDynamoDAO(String tableName, String partitionKey, String sortKey, String indexKey) {
        super(tableName);
        this.partitionKey = partitionKey;
        this.sortKey = sortKey;
        this.indexKey = indexKey;
    }

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    public DataPage<T> getPageOfItems(String targetUserAlias, int pageSize, String lastUserAlias) {
        DynamoDbTable<T> table = startTransaction();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(getKey(targetUserAlias, null)))
                .limit(pageSize);

        if(isNonEmptyString(lastUserAlias)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(partitionKey, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(sortKey, AttributeValue.builder().s(lastUserAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<T> result = new DataPage<T>();

        PageIterable<T> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<T> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });

        return result;
    }

    public DataPage<T> getPageOfItemsIndex(String targetUserAlias, int pageSize, String lastUserAlias) {
        DynamoDbIndex<T> table = startTransaction().index(indexKey);

        QueryEnhancedRequest.Builder requestBuilder=QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(getKey(targetUserAlias, null)))
                .limit(pageSize);

        if (isNonEmptyString(lastUserAlias)) {
            Map<String, AttributeValue> startKey=new HashMap<>();
            startKey.put(sortKey, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(partitionKey, AttributeValue.builder().s(lastUserAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request=requestBuilder.build();

        DataPage<T> result=new DataPage<T>();

        SdkIterable<Page<T>> sdkIterable=table.query(request);
        PageIterable<T> pages=PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<T> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });

        return result;
    }

    protected <To> DataPage<To> convertDataPage(DataPage<T> from, Function<T, To> func) {
        return new DataPage<>(from.getValues().stream().map(func).collect(Collectors.toList()), from.isHasMorePages());
    }
}
