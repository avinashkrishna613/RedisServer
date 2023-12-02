package com.redis.rediserver;

import com.redis.rediserver.commands.Command;
import com.redis.rediserver.commands.CommandCreator;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestUtils {

    private static final Map<String, String> customMap = new HashMap<>();

    public static void validateRequest(String[] request) throws Exception {
        if (request == null || request.length < 2 || !StringUtils.hasText(request[0])) {
            throw new Exception("Received invalid request");
        }
    }

    public static String executeRequest(String[] request) throws Exception {
        Command command = CommandCreator.createCommand(request);
        command.validate();
        return new String(String.valueOf(command.process()));
    }

}
