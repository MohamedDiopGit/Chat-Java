import java.net.* ;
import java.io.* ;
import java.util.Scanner;

public class Client {
    public static void main(String [] args ) {
        String address = "127.0.0.1";
        String nameDefault = "GigaChad";
        int idClient;
        try {// Open sockets and streams
            Socket sock = new Socket(address,10000);
            DataOutputStream out =new DataOutputStream(sock.getOutputStream());
            DataInputStream in = new DataInputStream(sock.getInputStream());
            if (args.length > 0) // Send name
                out.writeUTF(args[0]);
            else
                out.writeUTF(nameDefault);
            idClient= in.readInt(); // number reception
            System.out.println("Player n°: "+idClient);

            String message="";
            Scanner scanner = new Scanner(System.in);
            while(!message.equals("end")){
                message = scanner.nextLine();
                out.writeUTF(message);
            }
            System.out.println("Chat closed.");
            in.close(); // Close stream
            out.close();
            sock.close() ; // Close socket
        } catch (UnknownHostException e) {System.out.println(address + " unreachable.");}
        catch (IOException e) {e.printStackTrace();}
    }
} 