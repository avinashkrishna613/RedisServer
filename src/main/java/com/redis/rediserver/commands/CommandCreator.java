package com.redis.rediserver.commands;

import com.redis.rediserver.commands.MapCommands.impl.GetImpl;
import com.redis.rediserver.commands.MapCommands.impl.PutImpl;

public class CommandCreator {

    public static Command createCommand(String[] requestArgs) {
        String commandName = requestArgs[0];
        switch (commandName) {
            case "GET": return GetImpl.getInstance(requestArgs[1]);
            case "PUT", "SET": return PutImpl.getInstance(requestArgs[1], requestArgs[2]);
        }
        return null;
    }
}
