import java.net.*; // Socktse
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
     * Total number of worker thread to be created.
     */
    private static final int nThread = 2;

    /**
     * Array of the threads.
     */
    private static Thread[] clientThread = new Thread[nThread];
    /**
     * Chat box built in a GUI.
     * see {@code ChatGUI}
     */
    private ChatGUI chatGUI;

    /**
     * Main Server program.
     */
    public static void main() {
        new Server();

    }

    /**
     * Constructor for the server.
     */
    Server() {
        // Chat GUI display
        chatGUI = new ChatGUI();

        // Threads creating
        System.out.println("Server starting");
        try {// Socket manager : port 10000
            gestSock = new ServerSocket(10000);
            for (int i = 0; i < nThread; i++) {
                clientThread[i] = new Thread(this, String.valueOf(i));
                clientThread[i].start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
    }

    public static void main(String args[]) {
        new Server();
    }

    @Override
    public void run() {
        int idClientThread = Integer.parseInt(currentThread().getName());
        while (!endChat) {
            System.out.println("Thread " + currentThread().getName() + " : reset.");
            Socket socket;
            DataInputStream entree;
            DataOutputStream sortie;
            try {
                socket = gestSock.accept(); // Waiting for connection
                entree = new DataInputStream(socket.getInputStream());
                sortie = new DataOutputStream(socket.getOutputStream());
                // Data reading
                String namePlayer = entree.readUTF();
                System.out.println(namePlayer + " is connected");

                // Send data : unique id of the client.
                sortie.writeInt(idClientThread);

                // Read data from clients
                String message = "";
                while (!message.equals("end")) {
                    try {
                        message = entree.readUTF();
                        chatGUI.addTextToChat("[" + namePlayer + "]: " + message);
                        System.out.println("[" + namePlayer + "]: " + message);
                        if (message.equals("server-off")) {
                            message = "end";
                        }
                    } catch (EOFException | SocketException e) {
                        message = "end";
                    }
                }
                // Clean close of the session
                System.out.println(namePlayer + " has disconnected");
                sortie.close();
                entree.close();
                socket.close();
            } catch (IOException e) {// Quick cleaning
                // throw new RuntimeException();
                System.out.println("Failed to connect on thread: " + idClientThread + ",please retry.");
            }
        }
        closeServer(idClientThread);
    }

    /**
     * Closes the entire threads and shuts down the server.
     * 
     * @param idCloserThread : id of closer thread
     */
    public synchronized void closeServer(int idCloserThread) {
        endChat = true;
        for (int i = 0; i < nThread; i++) {
            if (i != idCloserThread) {
                clientThread[i].interrupt();
            }
        }
        try {
            gestSock.close();
            System.out.println("Server shut down.");
        } catch (IOException e) {
            // throw new RuntimeException(e);
        }

        clientThread[idCloserThread] = null;
    }
}