package com.redis.rediserver.commands.MapCommands.impl;

import com.redis.rediserver.commands.MapCommands.HashMapCommands;
import org.springframework.util.StringUtils;

public class PutImpl extends HashMapCommands {

    private String key;
    private String value;
    private static PutImpl putImpl;

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static PutImpl getInstance(String key, String value) {
        if (putImpl == null) {
            return new PutImpl(key,value);
        }
        putImpl.setKey(key);
        putImpl.setValue(value);
        return putImpl;
    }

    private PutImpl(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Integer process() {
        this.customMap.put(key, value);
        return 1;
    }

    @Override
    public void validate() throws Exception {
        if (!StringUtils.hasText(key) || !StringUtils.hasText(value)) {
            throw new Exception("Key and value should not be empty");
        }
    }
}
