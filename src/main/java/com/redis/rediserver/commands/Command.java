package com.redis.rediserver.commands;

public interface Command<T> {
    T process();

    void validate() throws Exception;
}
