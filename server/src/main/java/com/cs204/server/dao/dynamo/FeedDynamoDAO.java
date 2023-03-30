package com.cs204.server.dao.dynamo;

import com.cs204.server.dao.DataPage;
import com.cs204.server.dao.FeedDAO;
import com.cs204.server.dao.dynamo.model.StatusBean;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class FeedDynamoDAO extends PageDynamoDAO<StatusBean> implements FeedDAO {
    private static final String AliasAttr = "alias";
    private static final String DatetimeAttr = "datetime";
    private static final String TableName = "feed";

    public FeedDynamoDAO() {
        super(TableName, AliasAttr, DatetimeAttr, null);
    }

    @Override
    public DataPage<Status> getPageOfFeeds(String alias, int pageSize, String lastPosted) {
        DataPage<StatusBean> page = getPageOfItems(alias, pageSize, lastPosted);

        return convertDataPage(page, p -> p.getStatus());
    }

    @Override
    public void setFeed(String post, String user, Long datetime, List<String> urls, List<String> mentions) {
        setItem(user, datetime.toString(), new ItemSetter<StatusBean>() {
            @Override
            public void setItem(StatusBean item) {
                item.setTime(datetime.toString());
                item.setAlias(user);
                item.setMentions(mentions);
                item.setPost(post);
                item.setUrls(urls);
            }

            @Override
            public StatusBean newItem() {
                return new StatusBean();
            }
        });
    }

    @Override
    public Status getFeed(String alias) {
        return getItem(alias, null).getStatus();
    }

    @Override
    public void deleteFeed(String alias) {
        deleteItem(alias, null);
    }
}
