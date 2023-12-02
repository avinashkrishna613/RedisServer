package com.redis.rediserver.commands.MapCommands;

import java.util.HashMap;
import java.util.Map;

public abstract class HashMapCommands implements Get<String>, Put<Integer> {
    protected static final Map<String, String> customMap = new HashMap<>();
}
