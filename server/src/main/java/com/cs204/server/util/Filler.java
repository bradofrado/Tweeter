package com.cs204.server.util;

import com.cs204.server.dao.FollowDAO;
import com.cs204.server.dao.UserDAO;
import com.cs204.server.module.MainModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public class Filler {

    // How many follower users to add
    // We recommend you test this with a smaller number first, to make sure it works for you
    private final static int NUM_USERS = 25;

    // The alias of the user to be followed by each user created
    // This example code does not add the target user, that user must be added separately.
    private final static String FOLLOW_TARGET = "@bradofrado";

    private static Injector injector;
    private static Injector getInjector() {
        if (injector == null) {
            injector = Guice.createInjector(new MainModule());
        }

        return injector;
    }

    public static void fillDatabase() {

        UserDAO userDAO = getInjector().getInstance(UserDAO.class);
        FollowDAO followDAO = getInjector().getInstance(FollowDAO.class);

        List<String> followers = new ArrayList<>();
        List<User> users = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 1; i <= NUM_USERS; i++) {

            String name = "Guy";
            String alias = "guy" + i;

            // Note that in this example, a UserDTO only has a name and an alias.
            // The url for the profile image can be derived from the alias in this example
            User user = new User(name, Integer.toString(i), alias, "image");
            users.add(user);

            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            followers.add(alias);
        }

        // Call the DAOs for the database logic
        if (users.size() > 0) {
            //userDAO.addUserBatch(users);
        }
        if (followers.size() > 0) {
            followDAO.addFollowersBatch(followers, FOLLOW_TARGET);
        }
    }
}