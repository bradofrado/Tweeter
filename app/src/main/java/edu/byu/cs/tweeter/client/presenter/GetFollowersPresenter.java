package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersPresenter extends PagedPresenter<User> {
    FollowService followService;

    public GetFollowersPresenter(View view, User user) {
        super(view, user);
        followService = new FollowService();
    }

    @Override
    protected void getItems(User user, int pageSize, User lastItem) {
        followService.getMoreFollowers(user, pageSize, lastItem, new PagedObserver());
    }

    @Override
    protected String getDescription() {
        return "get followers";
    }
}
