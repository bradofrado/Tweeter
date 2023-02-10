package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.handler.LoginHandler;
import edu.byu.cs.tweeter.client.model.service.handler.RegisterHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {
    public interface LoginObserver extends SimpleNotificationObserver {
        void loggedIn(User loggedInUser);
    }

    public interface RegisterObserver extends ServiceObserver {
        void registered(User registeredUser);
    }

    public interface UserObserver extends ServiceObserver {
        void setUser(User user);
    }
    
    public void getUser(String userName, UserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userName, new GetUserHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void loginUser(String username, String password, LoginObserver loginObserver) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(username, password, new LoginHandler(loginObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }

    public void registerUser(String firstname, String lastname, String username, String password,
                             String imageBytesBase64, RegisterObserver observer) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstname, lastname,
                username, password, imageBytesBase64, new RegisterHandler(observer));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(registerTask);
    }

    public void logout(SimpleNotificationObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new SimpleNotificationHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }
}
