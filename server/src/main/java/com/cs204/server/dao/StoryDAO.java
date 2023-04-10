package com.cs204.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public interface StoryDAO {
    DataPage<Status> getPageOfStories(String alias, int pageSize, Long time);
    void setStory(String post, String user, Long time, List<String> urls, List<String> mentions);
    Status getStory(String alias);
    void deleteStory(String alias);
}
