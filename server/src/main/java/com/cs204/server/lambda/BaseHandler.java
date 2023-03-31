package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.cs204.server.dao.AuthTokenDAO;
import com.cs204.server.dao.dynamo.AuthTokenDynamoDAO;
import com.cs204.server.dao.dynamo.FeedDynamoDAO;
import com.cs204.server.dao.dynamo.FollowDynamoDAO;
import com.cs204.server.dao.dynamo.StoryDynamoDAO;
import com.cs204.server.dao.dynamo.UserDynamoDAO;
import com.cs204.server.dao.s3.ImageS3DAO;
import com.cs204.server.module.MainModule;
import com.cs204.server.module.TestModule;
import com.cs204.server.service.FollowService;
import com.cs204.server.service.StatusService;
import com.cs204.server.service.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.Charset;

import edu.byu.cs.tweeter.util.JsonSerializer;

public abstract class BaseHandler<I, O> implements RequestStreamHandler {
    private static Injector injector;
    private static Injector getInjector() {
        if (injector == null) {
            injector = Guice.createInjector(new MainModule());
        }

        return injector;
    }

    private <I> Class<I> getType1() {
        return (Class<I>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private <O> Class<O> getType2() {
        return (Class<O>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
    }

    protected <T> T getInstance(Class<T> type) {
        Injector injector = getInjector();
        return injector.getInstance(type);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        LambdaLogger logger = context.getLogger();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("US-ASCII")));
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("US-ASCII"))));
        try {
            I in = JsonSerializer.deserialize(reader, getType1());
            O out = handleRequest(in);
            writer.write(JsonSerializer.serialize(out));
        } catch (IllegalStateException | JsonSyntaxException ex) {
            logger.log(ex.getMessage());
        }
        finally {
            reader.close();
            writer.close();
        }
    }

    protected abstract O handleRequest(I request);
}
