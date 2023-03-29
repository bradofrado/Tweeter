package com.cs204.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

//pk: receiveralias
//sk: time (unix time stamp)
public interface FeedDAO {
    DataPage<Status> getPageOfFeeds(String alias, int pageSize, String lastPosted);
    void setFeed(String post, String user, Integer time, List<String> urls, List<String> mentions);
    Status getFeed(String alias);
    void deleteFeed(String alias);
}
