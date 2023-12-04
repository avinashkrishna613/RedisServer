package com.redis.rediserver.commands.MapCommands;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class HashMapCommands implements Get<String>, Put<Integer> {
    protected static final ConcurrentHashMap<String, String> customMap = new ConcurrentHashMap<>();
}
