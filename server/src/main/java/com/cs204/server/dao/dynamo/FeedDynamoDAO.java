package com.cs204.server.dao.dynamo;

import com.cs204.server.dao.DataPage;
import com.cs204.server.dao.FeedDAO;
import com.cs204.server.dao.dynamo.model.FeedBean;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class FeedDynamoDAO extends PageDynamoDAO<FeedBean> implements FeedDAO {
    private static final String AliasAttr = "alias";
    private static final String DatetimeAttr = "time";
    private static final String TableName = "feed";

    public FeedDynamoDAO() {
        super(TableName, AliasAttr, DatetimeAttr, null);
    }

    @Override
    public DataPage<Status> getPageOfFeeds(String alias, int pageSize, Long time) {
        String timeStr = time != null ? time.toString() : null;
        DataPage<FeedBean> page = getPageOfItems(alias, pageSize, timeStr);

        return convertDataPage(page, p -> p.getStatus());
    }

    @Override
    public void setFeed(String alias, String post, String poster, Long datetime, List<String> urls, List<String> mentions) {
        setItem(alias, datetime.toString(), new ItemSetter<FeedBean>() {
            @Override
            public void setItem(FeedBean item) {
                item.setPosterAlias(poster);
                item.setTime(datetime.toString());
                item.setAlias(alias);
                item.setMentions(mentions);
                item.setPost(post);
                item.setUrls(urls);
            }

            @Override
            public FeedBean newItem() {
                return new FeedBean();
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
