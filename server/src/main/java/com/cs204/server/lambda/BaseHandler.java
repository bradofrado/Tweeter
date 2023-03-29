package com.cs204.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.cs204.server.module.MainModule;
import com.cs204.server.module.TestModule;
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

public abstract class BaseHandler<I, O> implements RequestStreamHandler {
    private Injector injector;
    private Injector getInjector() {
        if (injector == null) {
            injector = Guice.createInjector(new TestModule());
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("US-ASCII")));
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("US-ASCII"))));
        Gson gson = new Gson();
        try {
            I in = gson.fromJson(reader, getType1());
            O out = handleRequest(in);
            writer.write(gson.toJson(out));
        } catch (IllegalStateException | JsonSyntaxException ex) {

        }
        finally {
            reader.close();
            writer.close();
        }
    }

    protected abstract O handleRequest(I request);
}
