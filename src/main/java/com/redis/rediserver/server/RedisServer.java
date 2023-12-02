package com.redis.rediserver.server;

import com.redis.rediserver.RequestUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RedisServer {

    // basically server sockets are low level communications
    // they work on TCP/UDP level
    private ServerSocket serverSocket = null;
    private static final Integer DEFAULT_PORT = 6379;
    private int connections = 0;

    private static final int MAX_CONNECTIONS = 2;

    private static RedisServer redisServer;

    public static RedisServer getInstance() throws IOException {
        if (redisServer == null) {
            redisServer = new RedisServer();
        }
        return redisServer;
    }

    public void start() throws IOException {
        System.out.println("Server started");
        Socket clientSocket = null;
        while (true) {
            try {
                // it waits for the connection from client and once received
                // terminates
                if (connections >= MAX_CONNECTIONS) {
                    throw new IOException("Connection failed. Max connections established");
                }
                clientSocket = serverSocket.accept();
                connections++;
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
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    System.out.println("IOException: " + e.getMessage());
                }
            }
        }
    }

    private RedisServer() throws IOException {
        try {
            int port = DEFAULT_PORT;
            this.serverSocket = new ServerSocket(port);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
