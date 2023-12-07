package com.redis.rediserver.server;

import com.redis.rediserver.RequestUtils;
import com.redis.rediserver.exceptions.MaxConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisServer {

    // basically server sockets are low level communications
    // they work on TCP/UDP level
    private ServerSocket serverSocket;
    private static final Integer DEFAULT_PORT = 6379;
    private int connections = 0;

    private static final int MAX_CONNECTIONS = 2;

    private static RedisServer redisServer;

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisServer.class);

    public static RedisServer getInstance() throws Exception {
        if (redisServer == null) {
            redisServer = new RedisServer();
        }
        return redisServer;
    }

    public void start() throws RuntimeException {
        System.out.println("Server started");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                connections++;
                // it waits for the connection from client and once received terminates
                if (connections > MAX_CONNECTIONS) {
                    clientSocket.close();
                    throw new MaxConnectionException("Connection failed. Max connections established");
                }
                // this will check, if fewer threads are running than configured
                // if yes, creates a new thread with this as first task and start running it.
                executorService.execute(() -> {
                    try {
                        handleClientConnection(clientSocket);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (MaxConnectionException exception) {
                connections--;
                exception.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private static void closeSocket(Socket clientSocket) throws IOException {
        if (clientSocket != null) {
            System.out.println("Closing the socket/connection");
            clientSocket.close();
        }
    }

    private static void handleClientConnection(Socket clientSocket) throws Exception {
        try {
            LOGGER.info("Thread executing the request is " + Thread.currentThread().getName());
            InputStream inputStream = clientSocket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String readLine;
            while ((readLine = bufferedReader.readLine()) != null) {
                String[] request = readLine.split(" ");
                RequestUtils.validateRequest(request);
                String response = RequestUtils.executeRequest(request);
                bufferedWriter.write(response+"\r\n");
                bufferedWriter.flush();
            }
        } finally {
            try {
                closeSocket(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private RedisServer() throws Exception {
        try {
            int port = DEFAULT_PORT;
            this.serverSocket = new ServerSocket(port);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw exception;
        }
    }
}
