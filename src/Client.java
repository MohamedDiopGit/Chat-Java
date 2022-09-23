import java.net.* ;
import java.io.* ;
import java.util.Scanner;

/**
 * {@code Client} : Client program that makes a connection with a {@code Server} via socket, referenced by ip address and port.
 * @see Server
 */
public class Client {

    /**
     * Default constructor for the client session.
     */
    Client() {
        String address = "127.0.0.1";
        Scanner scanner = new Scanner(System.in);
        System.out.print("Client pseudo: ");
        String nameDefault = scanner.nextLine();
        int idClient;
        try {// Open sockets and streams
            Socket sock = new Socket(address, 10000);
            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            DataInputStream in = new DataInputStream(sock.getInputStream());

            // Get information from the server
            out.writeUTF(nameDefault);
            idClient = in.readInt(); // id of client : reception
            System.out.println("Player n°: " + idClient);

            // Send messages to the server
            String message = "";
            while (!message.equals("end")) {
                System.out.print("--> ");
                message = scanner.nextLine();

                try {
                    out.writeUTF(message);
                } catch (SocketException e) {  // Server off
                    System.out.println("Server offline...");
                    message = "end";
                }
            }
            System.out.println("Chat closed.");
            in.close(); // Close stream
            out.close();
            sock.close(); // Close socket
        } catch (UnknownHostException e) {
            System.out.println(address + " unreachable.");
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Server is currently offline: disconnection.");
        }
    }

    /**
     *  Runs the Client main program to connect with a server and send message.
     * @param args : Specify the ip address and port for connection. (localhost: 127.0.0.1)
     */
    public static void main(String [] args ) {
        new Client();
    }
} 