package com.redis.rediserver.server;

import com.redis.rediserver.RequestUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RedisServer {

    // basically server sockets are low level communications
    // they work on TCP/UDP level
    private ServerSocket serverSocket;
    private static final Integer DEFAULT_PORT = 6379;
    private int connections = 0;

    private static final int MAX_CONNECTIONS = 2;

    private static RedisServer redisServer;

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
                // it waits for the connection from client and once received
                // terminates
                if (connections > MAX_CONNECTIONS) {
                    throw new IOException("Connection failed. Max connections established");
                }
                System.out.println("Checking for connections...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection established...");
                System.out.println(clientSocket.getPort() + " " + clientSocket.getLocalPort() + " " + serverSocket.getLocalPort());
                connections++;
                Thread thread = new Thread(() -> {
                    try {
                        System.out.println("Thread is getting created " + this);
                        handleClientConnection(clientSocket);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                thread.setName("handling connection : " + connections);
                thread.start();
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
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
                System.out.println("IOException: " + e.getMessage());
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
