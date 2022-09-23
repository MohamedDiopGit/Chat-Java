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
    private static final int nThread = 1;

    /**
     * Array of the threads.
     */
    private static Thread[] clientThread = new Thread[nThread];

    private static Socket[] sockets = new Socket[nThread];
    private static DataOutputStream[] outs = new DataOutputStream[nThread];
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
        chatGUI = new ChatGUI("Server");

        // Threads creating
        System.out.println("Server started");
        try {// Socket manager : port 10000
            gestSock = new ServerSocket(10000);
            for (int i = 0; i < nThread; i++) {
                clientThread[i] = new Thread(this, String.valueOf(i));
                clientThread[i].start();
            }
            System.out.println("Info: "+nThread+" thread(s) created.");
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
            // System.out.println("Thread " + currentThread().getName() + " : reset.");
            DataInputStream entree;
            DataOutputStream sortie;
            try {
                sockets[idClientThread] = gestSock.accept(); // Waiting for connection
                entree = new DataInputStream(sockets[idClientThread].getInputStream());
                outs[idClientThread] = new DataOutputStream(sockets[idClientThread].getOutputStream());
                // Data reading
                String pseudoClient = entree.readUTF();
                System.out.println(pseudoClient + " is connected");

                // Send data : unique id of the client.
                outs[idClientThread].writeInt(idClientThread);

                // Read data from clients
                String message = "";
                while (!message.equals("end")) {
                    try {
                        message = entree.readUTF();
                        chatGUI.addTextToChat("[" + pseudoClient + "]: " + message);
                        //System.out.println("[" + pseudoClient + "]: " + message);

                        sendToAll(pseudoClient,message); // Broadcast to the others connected clients

                        if (message.equals("server-off")) {
                            message = "end";
                        }
                    } catch (EOFException | SocketException e) {
                        message = "end";
                    }
                }
                // Clean close of the session
                System.out.println(pseudoClient + " has disconnected.");
                outs[idClientThread].close();
                entree.close();
                sockets[idClientThread].close();
            } catch (IOException e) {// Quick cleaning
                // throw new RuntimeException();
                System.out.println("Failed to connect on thread: " + idClientThread + ",please retry.");
            }
        }
    }


    public void sendToAll(String pseudo, String message){
        String messageComplete = "[" + pseudo + "]: " + message;
        for(int i=0; i<nThread; i++){
            try {
                if(outs[i] != null){
                    outs[i].writeUTF(messageComplete);
                }
            } catch (IOException e) {
                //throw new RuntimeException(e);
            }
        }
    }
}