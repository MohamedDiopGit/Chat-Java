import javax.swing.*;
import java.net.*;
import java.io.*;

/**
 * {@code Client} : Client program that makes a connection with a {@code Server}
 * via socket, referenced by ip address and port.
 * 
 * @see Server
 */
public class Client implements Runnable {
    /**
     * Chat box.
     */
    private ChatGUI chatGUI;
    /**
     * Message to send.
     */
    private String message = "";
    /**
     * Input Stream for collecting data from server.
     */
    private DataInputStream in;
    /**
     * Thread which reads the incoming data of the server and displays it on the
     * chat box.
     */
    private Thread chatReader = new Thread(this);

    /**
     * Default constructor for the client session.
     */
    Client() {
        setClientParameters();
        // runClient("localhost", 10000,"Mohamed");
    }

    /**
     * Sets the client parameters for the client session.
     */
    public void setClientParameters() {
        JTextField addressField = new JTextField();
        JTextField portField = new JTextField();
        JTextField pseudoField = new JTextField();
        Object[] message = {
                "Ip Address:", addressField,
                "Port:", portField,
                "Pseudo:", pseudoField
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Set connection to the server",
                JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) { // Check if something is entered
            String address = addressField.getText();
            int port = Integer.valueOf(portField.getText());
            String pseudo = pseudoField.getText();
            runClient(address, port, pseudo);
        } else {
            JOptionPane.showMessageDialog(null, "Nothing selected. Retry later.",
                    "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Runs the Client main program to connect with a server and send message.
     * 
     * @param args : useless paramter.
     */
    public static void main(String[] args) {
        System.out.println("Running client...");
        new Client();
    }

    /**
     * Establishes a connection with the server and allows to send messages
     * 
     * @param address
     * @param port
     * @param pseudo
     */
    private void runClient(String address, int port, String pseudo) {
        chatGUI = new ChatGUI(pseudo);

        int idClient;
        try {

            System.out.print("Trying to connect to " + address + " port:" + port + "...");
            Socket sock = new Socket(address, port);
            System.out.println("done.");

            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            chatGUI.setOutputStream(out);
            in = new DataInputStream(sock.getInputStream());

            // Get information from the server
            out.writeUTF(pseudo);
            idClient = in.readInt(); // id of client : reception

            System.out.println("Client id: " + idClient);

            // Read message from the server
            chatReader.start();

        } catch (UnknownHostException e) {
            // System.out.println(" failed."); // Terminal output
            JOptionPane.showMessageDialog(null, address + ":" + port + " unreachable. Retry later.",
                    "ERROR", JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
            // e.printStackTrace();
            // System.out.println(address + " unreachable. Retry later.");
            JOptionPane.showMessageDialog(null, address + ":" + port + " unreachable. Retry later.",
                    "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Thread's method to read the incoming data and put in on the chat box GUI.
     */
    @Override
    public void run() {
        String messageReceived;
        while (!message.equals("end")) {
            try {
                messageReceived = in.readUTF();
                chatGUI.addTextToChat(messageReceived);
            } catch (IOException e) { // Server off
                chatGUI.addTextToChat("Server offline... disconnected.");
                message = "end";
            }
        }
        chatReader = null;
    }

}