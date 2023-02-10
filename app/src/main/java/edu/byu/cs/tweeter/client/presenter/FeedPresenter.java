package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {
    private StatusService statusService;

    public FeedPresenter(View view, User user) {
        super(view, user);
        statusService = new StatusService();
    }

    @Override
    protected void getItems(User user, int pageSize, Status lastItem) {
        statusService.getStatuses(user, pageSize, lastItem, new PagedObserver());
    }

    @Override
    protected String getDescription() {
        return "get feed";
    }
}
