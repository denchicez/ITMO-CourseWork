package info.kgeorgiy.ja.zhimoedov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class HelloUDPClient implements HelloClient {
    public static class PacketSender {
        private final String host, prefix;
        private final int port;
        private static final int TIMEOUT = 10;
        private final Phaser phaser;

        public PacketSender(String host, int port, String prefix, Phaser phaser) {
            this.host = host;
            this.port = port;
            this.prefix = prefix;
            this.phaser = phaser;
        }

        public void doRequest(int thread, int request, InetAddress address,
                              DatagramSocket socketClient) throws IOException {
            String messageString = prefix + (thread + 1) + "_" + (request + 1);
            String needMessageString = "Hello, " + messageString;
            byte[] messageBytes = messageString.getBytes();
            DatagramPacket packetSend = new DatagramPacket(messageBytes, messageBytes.length,
                    address, port);
            socketClient.send(packetSend);
            byte[] buf = new byte[socketClient.getReceiveBufferSize()];
            DatagramPacket packetGet = new DatagramPacket(buf, buf.length);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    socketClient.receive(packetGet);
                    String receivedMessage = new String(packetGet.getData(), packetGet.getOffset(),
                            packetGet.getLength(), StandardCharsets.UTF_8);
                    if (!receivedMessage.equals(needMessageString)) {
                        continue;
                    }
                    break;
                } catch (IOException e) {
                    socketClient.send(packetSend);
                }
            }
        }

        public void send(int thread, int requests) {
            try {
                InetAddress address = InetAddress.getByName(host);
                try (DatagramSocket socket = new DatagramSocket()) {
                    socket.setSoTimeout(TIMEOUT);
                    for (int request = 0; request < requests; request++) {
                        while (true) {
                            try {
                                doRequest(thread, request, address, socket);
                                break;
                            } catch (IOException ignored) {
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Can't convert host to inetAddress");
            }
            phaser.arrive();
        }
    }

    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        Phaser phaser = new Phaser(1);
        PacketSender sender = new PacketSender(host, port, prefix, phaser);
        ExecutorService tasksSender = Executors.newFixedThreadPool(threads);
        for (int thread = 0; thread < threads; thread++) {
            final int finalThread = thread;
            phaser.register();
            tasksSender.submit(() -> sender.send(finalThread, requests));
        }
        phaser.arriveAndAwaitAdvance();
        tasksSender.shutdown();
    }

    public static void main(String[] args) {
        if (args == null || args.length != 5) {
            System.err.println("Format need be: HelloUDPClient host port prefix threads requests");
            return;
        }
        if (Arrays.stream(args).anyMatch(Objects::isNull)) {
            System.err.println("Arguments must be not NULL!");
        }
        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            String prefix = args[2];
            int threads = Integer.parseInt(args[3]);
            int requests = Integer.parseInt(args[4]);
            HelloClient helloUDPClient = new HelloUDPClient();
            helloUDPClient.run(host, port, prefix, threads, requests);
        } catch (NumberFormatException e) {
            System.err.println("port, threads, requests must be number");
        }
    }
}
