package com.payu.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.*;

@Component
public class ServerProxySocketListener implements Runnable{

    /**
     * Default class logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerProxySocketListener.class);

    /**
     * The size of the pool that handles the active connections. Since we are using permanent connection, this number
     * also determines the maximum number of active clients.
     */
    private static final int DEFAULT_POOL_SIZE = 20;

    /**
     * The port where the server will be listening to new connections
     */
    private final int port;

    /**
     * Server socket instance responsible of listen and accept incoming connections.
     */
    private ServerSocket serverSocket;

    /**
     * Flag that determines if the server is up and running or not.
     */
    private volatile boolean running;

    /**
     * Pool of threads that will process the active connections
     */
    private final ExecutorService threadPool;


    public ServerProxySocketListener(){

        threadPool = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
        this.port = 20001;
    }

    @Value("mock.host")
    private String mockHost;


    /**
     * Starts the server accepting new connections.
     */
    @Override
    public void run() {

        LOGGER.info("Starting ISO IT Server at port [{}]...", port);

        startServer();
        while (running) {

            try {
                final Socket client = serverSocket.accept();
                LOGGER.info("Connected to {}", client);

                threadPool.submit(()->{
                    if(client.isConnected()){
                        try {
                            sendMessageToPaymentNetWork(client.getInputStream(), client.getOutputStream());
                        } catch (IOException e) {
                            LOGGER.error("Error accepting new connections.", e);
                        }
                    }

                });

            } catch (IOException e) {

            }
        }


    }


    public void sendMessageToPaymentNetWork(InputStream in, OutputStream outStreamSrc)throws IOException{

        LOGGER.info("conexion to {}", 9099);

        Socket connection = new Socket(mockHost, 9099);
        BufferedOutputStream outStream = null;

        if(connection.isConnected()){

            outStream = new BufferedOutputStream(connection.getOutputStream());
            int count;
            byte[] buffer = new byte[8192]; // or 4096, or more

            sendMessageToPaymentNetWorkRun(connection.getInputStream(), outStreamSrc);

            while ((count = in.read(buffer)) > 0)
            {
                outStream.write(buffer, 0, count);
                outStream.flush();
            }



        }

    }


    public void sendMessageToPaymentNetWorkRun(InputStream in, OutputStream outStream)throws IOException{

        new Thread(){

            public void run(){
                try {
                    sendMessageToPaymentNetWork2(in,outStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void sendMessageToPaymentNetWork2(InputStream in, OutputStream outStream)throws IOException{

        LOGGER.info("aaaaaa to {}", 9099);

        int count;
        byte[] buffer = new byte[888192]; // or 4096, or more

        while ((count = in.read(buffer)) > 0)
        {
            LOGGER.info("responmdiendo a la fuente {}", 9099);
            outStream.write(buffer, 0, count);
            outStream.flush();
        }
        LOGGER.info("Saliendo{}", 9099);
    }


    /**
     * Start the server creating a new {@linkplain ServerSocket} instance bind to the configured port.
     */
    private void startServer() {

        try {
            LOGGER.info("Starting Server Proxy");
            serverSocket = new ServerSocket(port, DEFAULT_POOL_SIZE);
            serverSocket.setSoTimeout(0);
            running = true;
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + port, e);
        }
    }

    public void stop(boolean forceStop) {

        LOGGER.info("Stopping Davivienda ISO Mock server");

        running = false;

        if (serverSocket != null) {
            if (!serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException exception) {
                    LOGGER.error("Error closing server socket.", exception);
                }
            }
        }

        try {
            if (forceStop) {
                threadPool.shutdownNow();
            } else {
                threadPool.shutdown();
            }
        } catch (Exception exception) {
            LOGGER.error("Error while shutting down thread pool. Force stop [{}]", forceStop, exception);
        }

    }

    /**
     * Main method used to run an independent ISO Server instance
     *
     * @param args The list of arguments. Expected [portNumber] [poolSize]
     */
    public void init() {

        ServerProxySocketListener server = new ServerProxySocketListener();
        Thread serverThread = new Thread(server);
        serverThread.setDaemon(true);
        serverThread.start();
    }
}