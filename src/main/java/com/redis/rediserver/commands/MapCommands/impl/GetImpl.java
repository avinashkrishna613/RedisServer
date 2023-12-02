package com.redis.rediserver.commands.MapCommands.impl;

import com.redis.rediserver.commands.MapCommands.HashMapCommands;
import org.springframework.util.StringUtils;

public class GetImpl extends HashMapCommands {

    private String key;
    private static GetImpl getImpl;

    public void setKey(String key) {
        this.key = key;
    }

    public static GetImpl getInstance(String key) {
        if (getImpl != null) {
            getImpl.setKey(key);
            return getImpl;
        }
        return new GetImpl(key);
    }

    private GetImpl(String key) {
        this.key = key;
    }

    @Override
    public String process() {
       return this.customMap.get(key);
    }

    @Override
    public void validate() throws Exception {
        if (!StringUtils.hasText(this.key)) {
            throw new Exception("Key is invalid");
        }
    }
}
