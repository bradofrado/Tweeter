package com.cs204.server.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.cs204.server.dao.dynamo.FollowDynamoDAO;
import com.cs204.server.dao.dynamo.UserDynamoDAO;
import com.cs204.server.dao.s3.ImageS3DAO;
import com.cs204.server.util.HashingUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

@DisplayName("Dynamo DAO tests")
public class DAOTests {
    @Nested
    @DisplayName("Feed Dynamo DAO tests")
    public class FeedDAOTests {
        private FollowDAO followDAO;
        List<User> users;
        private FakeData fakeData = FakeData.getInstance();

        @BeforeEach
        public void setup() {
            followDAO = new FollowDynamoDAO();
            users = fakeData.getFakeUsers();
        }

        @Test
        @DisplayName("Should have follower after setting follower")
        public void should_have_followerAfterSettingFollower() {
            User user1 = users.get(0);
            User user2 = users.get(1);
            followDAO.setFollower(user1.getName(), user1.getAlias(), user2.getName(), user2.getAlias());
            assertTrue(followDAO.hasFollower(user1.getAlias(), user2.getAlias()));
        }

        @Test
        @DisplayName("Should return page of followers")
        public void should_return_pageOfFollowers() {
            DataPage<String> page = followDAO.getPageOfFollowers("@StantheMan", 10, null);
            assertTrue(page.isHasMorePages());
        }
    }

    @Nested
    @DisplayName("User Dynamo DAO Test")
    public class UserDAOTest {
        private UserDAO userDAO;
        private String password = "1234password";
        User user;
        @BeforeEach
        public void setup() {
            userDAO = new UserDynamoDAO();
            user = new User("Braydon", "Jones", "@brado", "www.google.com");
            String passwordHash = HashingUtil.hash(password);
            userDAO.setUser(user.getAlias(), user.getFirstName(), user.getLastName(), user.getImageUrl(), passwordHash);
        }

        @AfterEach
        public void cleanup() {
            userDAO.deleteUser(user.getAlias());
        }

        @Test
        @DisplayName("Should succeed when get user by alias and password")
        public void should_succeed_whenGetUser() {
            assertEquals(user, userDAO.getUser(user.getAlias()));
            assertEquals(user, userDAO.getUser(user.getAlias(), HashingUtil.hash(password)));
        }
    }

    @Nested
    @DisplayName("Image DAO tests")
    public class ImageDAOTests {
        private ImageDAO imageDAO;
        private String imageBytes;

        @BeforeEach
        public void setup() {
            try {
                imageDAO = new ImageS3DAO();
                imageBytes = readAndConvertImage("C:\\Users\\brado\\Downloads\\IMG-0381.jpg");
            } catch (IOException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        @DisplayName("should upload and return valid url when given base64 string")
        public void should_uploadAndReturnValidUrl_whenGivenBase64String() {
            String fileName = "bob";
            String url = imageDAO.uploadImage(fileName, imageBytes);
            assertEquals(ImageS3DAO.LINK + fileName, url);
        }

        private String readAndConvertImage(String filePath) throws IOException {
            BufferedImage bImage = ImageIO.read(new File(filePath));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", bos );
            byte [] data = bos.toByteArray();

            return Base64.getEncoder().encodeToString(data);
        }
    }
}
