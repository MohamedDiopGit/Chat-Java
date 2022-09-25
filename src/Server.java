import java.net.*; // Socktse
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*; // Streams

import static java.lang.Thread.currentThread;

/**
 * {@code Server} : Main class that creates a server for communication between
 * multiple clients with multithreading.
 */
public class Server implements Runnable {
    /**
     * Boolean to know if the chat room is closed or not.
     */
    private boolean endChat = false;
    /**
     * Server socket center.
     */
    private ServerSocket gestSock;
    /**
     * Total number of worker thread (maximum of clients) to be created.
     */
    private static final int nThread = 3;

    /**
     * Array of the threads.
     */
    private static Thread[] clientThread = new Thread[nThread];
    /**
     * Array of the sockets.
     */
    private static Socket[] sockets = new Socket[nThread];
    /**
     * Array of the out streams for broadcasting messages.
     */
    private static DataOutputStream[] outs = new DataOutputStream[nThread];
    /**
     * Chat box built in a GUI.
     * see {@code ChatGUI}
     */
    private ChatGUI chatGUI;

    /**
     * Data formatter to send the data with messages
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss z");

    /**
     * Main Server program.
     */
    public static void main(String args[]) {
        System.out.println("Running server...");
        new Server();
    }

    /**
     * Constructor for the server.
     */
    Server() {
        // Chat GUI display
        chatGUI = new ChatGUI(); // Default chat GUI : server side

        // Threads creating
        try {// Socket manager : port 10000
            gestSock = new ServerSocket(10000);
            for (int i = 0; i < nThread; i++) {
                clientThread[i] = new Thread(this, String.valueOf(i));
                clientThread[i].start();
            }
            chatGUI.addTextToChat("Info: " + nThread + " thread(s) created.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int idClientThread = Integer.parseInt(currentThread().getName());
        while (!endChat) {
            DataInputStream entree;
            try {
                sockets[idClientThread] = gestSock.accept(); // Waiting for connection
                entree = new DataInputStream(sockets[idClientThread].getInputStream());
                outs[idClientThread] = new DataOutputStream(sockets[idClientThread].getOutputStream());
                // Data reading
                String pseudoClient = entree.readUTF();

                chatGUI.addTextToChat(getUtcDateTime() + " [" + pseudoClient + "]: " + " is connected");

                // Send data : unique id of the client.
                outs[idClientThread].writeInt(idClientThread);

                // Read data from clients
                String message = "";
                while (!message.equals("end")) {
                    try {
                        message = entree.readUTF();
                        chatGUI.addTextToChat(getUtcDateTime() + " :[" + pseudoClient + "]: " + message);

                        sendToAll(pseudoClient, message); // Broadcast to the others connected clients

                        if (message.equals("server-off")) {
                            message = "end";
                        }
                    } catch (EOFException | SocketException e) {
                        message = "end";
                    }
                }
                // Clean close of the session

                chatGUI.addTextToChat(getUtcDateTime() + " [" + pseudoClient + "]: " + " has disconnected.");
                outs[idClientThread].close();
                entree.close();
                sockets[idClientThread].close();
            } catch (IOException e) {// Quick cleaning
                // throw new RuntimeException();
                System.out.println("Failed to connect on thread: " + idClientThread + ",please retry.");
            }
        }
    }

    public void sendToAll(String pseudoClient, String message) {
        String messageComplete = getUtcDateTime() + " [" + pseudoClient + "]: " + message;
        for (int i = 0; i < nThread; i++) {
            try {
                if (outs[i] != null) {
                    outs[i].writeUTF(messageComplete);
                }
            } catch (IOException e) {
                // throw new RuntimeException(e);
            }
        }
    }

    public static String getUtcDateTime() {
        return ZonedDateTime.now(ZoneId.of("Etc/UTC")).format(FORMATTER);
    }
}