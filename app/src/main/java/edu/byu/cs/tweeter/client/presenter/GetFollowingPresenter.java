package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingPresenter extends PagedPresenter<User> {
    private FollowService followService;

    public GetFollowingPresenter(View view, User user) {
        super(view, user);
    }

    @Override
    protected void getItems(User user, int pageSize, User lastItem) {
        getFollowService().getMoreFollowing(user, pageSize, lastItem, new PagedObserver());
    }

    @Override
    protected String getDescription() {
        return "retrieve followees";
    }

    protected FollowService getFollowService() {
        if (followService == null) {
            followService = new FollowService();
        }

        return followService;
    }
}
