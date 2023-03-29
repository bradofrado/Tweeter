package com.cs204.server.dao.dynamo;

import com.cs204.server.dao.DataPage;
import com.cs204.server.dao.StoryDAO;
import com.cs204.server.dao.dynamo.model.StatusBean;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class StoryDynamoDAO extends PageDynamoDAO<StatusBean> implements StoryDAO {
    private static final String AliasAttr = "alias";
    private static final String DatetimeAttr = "datetime";
    private static final String TableName = "story";

    public StoryDynamoDAO() {
        super(TableName, AliasAttr, DatetimeAttr);
    }

    @Override
    public DataPage<Status> getPageOfStories(String alias, int pageSize, String lastPosted) {
        DataPage<StatusBean> page = getPageOfItems(alias, pageSize, lastPosted);

        return convertDataPage(page, p -> p.getStatus());
    }

    @Override
    public void setStory(String post, String user, Integer time, List<String> urls, List<String> mentions) {
        setItem(user, time.toString(), new ItemSetter<StatusBean>() {
            @Override
            public void setItem(StatusBean item) {
                item.setDatetime(time);
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
    public Status getStory(String alias) {
        return getItem(alias, null).getStatus();
    }

    @Override
    public void deleteStory(String alias) {
        deleteItem(alias, null);
    }
}