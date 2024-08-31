package info.kgeorgiy.ja.zhimoedov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HelloUDPServer implements HelloServer {
    private DatagramSocket datagramSocket;
    private ExecutorService executorService;

    private class TaskExecutor implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    byte[] bufGet = new byte[datagramSocket.getReceiveBufferSize()];
                    DatagramPacket packetGet = new DatagramPacket(bufGet, bufGet.length);
                    datagramSocket.receive(packetGet);
                    String messageGet = new String(packetGet.getData(), packetGet.getOffset(), packetGet.getLength(),
                            StandardCharsets.UTF_8);
                    byte[] messageAnswer = ("Hello, " + messageGet).getBytes();
                    DatagramPacket packetAnswer = new DatagramPacket(messageAnswer, packetGet.getOffset(),
                            messageAnswer.length, packetGet.getSocketAddress());
                    datagramSocket.send(packetAnswer);
                } catch (IOException ignored) {
                }
            }
        }

    }

    @Override
    public void start(int port, int threads) {
        try {
            datagramSocket = new DatagramSocket(port);
            executorService = Executors.newFixedThreadPool(threads);
            for (int i = 0; i < threads; ++i) {
                executorService.submit(new TaskExecutor());
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(1, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() {
        datagramSocket.close();
        shutdownAndAwaitTermination(executorService);
    }

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.err.println("Format need be: HelloUDPServer port threads");
            return;
        }
        if (Arrays.stream(args).anyMatch(Objects::isNull)) {
            System.err.println("Arguments must be not NULL!");
        }
        try {
            int port = Integer.parseInt(args[0]);
            int threads = Integer.parseInt(args[1]);
            HelloServer helloUDPServer = new HelloUDPServer();
            helloUDPServer.start(port, threads);
            Scanner sc = new Scanner(System.in);
            System.out.println("Press enter to close");
            sc.nextLine();
            sc.close();
            helloUDPServer.close();
        } catch (NumberFormatException e) {
            System.err.println("port, threads must be number");
        }
    }
}
