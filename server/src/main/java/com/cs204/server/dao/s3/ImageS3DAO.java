package com.cs204.server.dao.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cs204.server.dao.ImageDAO;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class ImageS3DAO implements ImageDAO {
    private static final String BUCKET_NAME = "braydon-cs340";
    public static final String LINK = "https://braydon-cs340.s3.us-west-2.amazonaws.com/";

    private static AmazonS3 client = null;

    private static AmazonS3 getClient() {
        if (client == null) {
            client = AmazonS3ClientBuilder
                    .standard()
                    .withRegion("us-west-2")
                    .build();
        }

        return client;
    }
    @Override
    public String uploadImage(String name, String byte64Image) {
        byte[] byteArray = Base64.getDecoder().decode(byte64Image);

        ObjectMetadata data = new ObjectMetadata();

        data.setContentLength(byteArray.length);

        data.setContentType("image/jpeg");

        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, name, new ByteArrayInputStream(byteArray), data).withCannedAcl(CannedAccessControlList.PublicRead);

        getClient().putObject(request);

        return LINK + name;
    }
}
