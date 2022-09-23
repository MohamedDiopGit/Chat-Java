
import javax.swing.*;
import java.awt.*;

/**
 * {@code ChatGUI} : Class that displays the chat box for the server. Acts like
 * a log terminal.
 */
public class ChatGUI extends JFrame {
    /**
     * Text area for the chat
     */
    JTextArea textArea;

    /**
     * Constructor by default for the chat GUI
     */
    ChatGUI() {
        setTitle("Server logs");
        JLabel chatTitle = new JLabel("Server : Chat box", SwingConstants.CENTER);
        JLabel chatSubTitle = new JLabel("Message logs recording", SwingConstants.CENTER);
        textArea = new JTextArea(10, 20);
        add(chatTitle, BorderLayout.NORTH);
        add(chatSubTitle, BorderLayout.SOUTH);
        JScrollPane sp = new JScrollPane(textArea);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 220);
        getContentPane().add(sp);
        setVisible(true);
    }

    /**
     * It displays the message from a client to the server chat box, in the text
     * area.
     * 
     * @param message : {@code String} message to display.
     */
    public synchronized void addTextToChat(String message) {
        textArea.append(message + "\n");
    }
}
