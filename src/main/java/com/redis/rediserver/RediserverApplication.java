package com.redis.rediserver;

import com.redis.rediserver.server.RedisServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RediserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(RediserverApplication.class, args);
		startServer();
	}

	private static void startServer() {
		try {
			RedisServer redisServer = RedisServer.getInstance();
			redisServer.start();
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("Redis server start failed");
		}
	}

}
