import java.net.* ; // Sockets
import java.io.* ; // Streams

import static java.lang.Thread.currentThread;

public class Server implements Runnable{
    private boolean endChat = false;
    private ServerSocket gestSock;
    private static final int nThread = 2;
    private static Thread[] clientThread = new Thread[nThread];

    public static void main(String [] args) {
        new Server();

    }
    Server(){
        System.out.println("Server starting") ;
        try {// Socket manager : port 10000
            gestSock = new ServerSocket(10000);
            for (int i = 0; i < nThread; i++) {
                clientThread[i] = new Thread(this, String.valueOf(i));
                clientThread[i].start();
            }
        } catch (IOException e) {e.printStackTrace( );}
        //gestSock.close();
    }
    @Override
    public void run() {
        int idClientThread = Integer.parseInt(currentThread().getName());
        while (!endChat) {
            System.out.println("Thread " + currentThread().getName() + " : reset.");
            Socket socket; // Wait
            DataInputStream entree;
            DataOutputStream sortie;
            try {
                socket = gestSock.accept();
                entree = new DataInputStream(socket.getInputStream());
                sortie = new DataOutputStream(socket.getOutputStream());
                // Data reading
                String namePlayer = entree.readUTF();
                System.out.println(namePlayer + " is connected");
                // Send data : 0 for instance
                sortie.writeInt(idClientThread);

                String message = "";
                while (!message.equals("end")) {
                    try {
                        message = entree.readUTF();
                        System.out.println("[" + namePlayer + "]: " + message);
                    } catch (EOFException | SocketException e) {
                        message = "end";
                    }
                }
                System.out.println(namePlayer + " has disconnected");
                // Quick cleaning
                sortie.close();
                entree.close();
                socket.close();
            } catch (IOException e) {// Quick cleaning
                throw new RuntimeException();
            }
        }
        clientThread[idClientThread] = null;
    }
}