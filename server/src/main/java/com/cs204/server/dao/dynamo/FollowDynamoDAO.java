package com.cs204.server.dao.dynamo;

import com.cs204.server.dao.DataPage;
import com.cs204.server.dao.FollowDAO;
import com.cs204.server.dao.dynamo.model.FollowerBean;

public class FollowDynamoDAO extends PageDynamoDAO<FollowerBean> implements FollowDAO {
    public FollowDynamoDAO() {
        super(TableName, FollowerAttr, FolloweeAttr);
    }
    private static final String TableName = "follows";
    public static final String IndexName = "follows_index";

    private static final String FollowerAttr = "follower_handle";
    private static final String FolloweeAttr = "followee_handle";



    /**
     * Gets a follower object using the follower_handle and followee_handle
     * @param follower_handle
     * @param followee_handle
     * @return
     */
    public boolean hasFollower(String follower_handle, String followee_handle) {
        return getItem(follower_handle, followee_handle) != null;
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
        setItem(follower_handle, followee_handle, new ItemSetter<>() {
            @Override
            public void setItem(FollowerBean item) {
                item.setFollower_handle(follower_handle);
                item.setFollowee_handle(followee_handle);
                item.setFollower_name(follower_name);
                item.setFollowee_name(followee_name);
            }

            @Override
            public FollowerBean newItem() {
                return new FollowerBean();
            }
        });
    }

    /**
     * Delete a follower given its follower_handle and followee_handle
     * @param follower_handle
     * @param followee_handle
     */
    public void deleteFollower(String follower_handle, String followee_handle) {
        deleteItem(follower_handle, followee_handle);
    }

    @Override
    public Integer getFollowerCount(String follower) {
        return null;
    }

    @Override
    public DataPage<String> getPageOfFollowees(String targetUserAlias, int pageSize, String lastUserAlias) {
        return convertDataPage(getPageOfItemsIndex(targetUserAlias, pageSize, lastUserAlias), p -> p.getFollowee_handle());
    }

    @Override
    public DataPage<String> getPageOfFollowers(String targetUserAlias, int pageSize, String lastUserAlias) {
        return convertDataPage(getPageOfItems(targetUserAlias, pageSize, lastUserAlias), p -> p.getFollower_handle());
    }
}
