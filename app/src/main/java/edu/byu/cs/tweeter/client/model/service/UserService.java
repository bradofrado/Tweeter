package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.handler.AuthenticationTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.UserTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;

public class UserService extends BaseService {
   public void getUser(String userName, UserTaskObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userName, new GetUserHandler(observer));
        executeTask(getUserTask);
    }

    public void loginUser(String username, String password, UserTaskObserver loginObserver) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(username, password, new AuthenticationTaskHandler(loginObserver));
        executeTask(loginTask);
    }

    public void registerUser(String firstname, String lastname, String username, String password,
                             String imageBytesBase64, UserTaskObserver observer) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstname, lastname,
                username, password, imageBytesBase64, new AuthenticationTaskHandler(observer));

        executeTask(registerTask);
    }

    public void logout(SimpleNotificationObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new SimpleNotificationHandler(observer));
        executeTask(logoutTask);
    }
}
