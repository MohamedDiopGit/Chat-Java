
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
    ChatGUI(String title) {
        setTitle("Chat box");
        JLabel chatTitle = new JLabel(title+" : Chat box", SwingConstants.CENTER);
        JLabel chatSubTitle = new JLabel("Message logs recording", SwingConstants.CENTER);
        textArea = new JTextArea(10, 20);
        textArea.setEditable(false);

        add(chatTitle, BorderLayout.NORTH);
        add(chatSubTitle, BorderLayout.SOUTH);
        JScrollPane sp = new JScrollPane(textArea);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //  Shuts down the server when exit
        setSize(300, 220);
        getContentPane().add(sp);
        setVisible(true);


    }

    public void getTextArea(){
        System.out.println("ici: " + textArea.getText());
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
