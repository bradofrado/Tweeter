package com.cs204.server.dao.dynamo;

import com.cs204.server.dao.DataPage;
import com.cs204.server.dao.StoryDAO;
import com.cs204.server.dao.dynamo.model.StoryBean;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class StoryDynamoDAO extends PageDynamoDAO<StoryBean> implements StoryDAO {
    private static final String AliasAttr = "alias";
    private static final String DatetimeAttr = "datetime";
    private static final String TableName = "story";

    public StoryDynamoDAO() {
        super(TableName, AliasAttr, DatetimeAttr, null);
    }

    @Override
    public DataPage<Status> getPageOfStories(String alias, int pageSize, String lastPosted) {
        DataPage<StoryBean> page = getPageOfItems(alias, pageSize, lastPosted);

        return convertDataPage(page, p -> p.getStatus());
    }

    @Override
    public void setStory(String post, String user, Long time, List<String> urls, List<String> mentions) {
        setItem(user, time.toString(), new ItemSetter<StoryBean>() {
            @Override
            public void setItem(StoryBean item) {
                item.setTime(time.toString());
                item.setAlias(user);
                item.setMentions(mentions);
                item.setPost(post);
                item.setUrls(urls);
            }

            @Override
            public StoryBean newItem() {
                return new StoryBean();
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
