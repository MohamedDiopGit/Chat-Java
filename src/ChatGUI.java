import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

class ChatBot {
    JFrame frame;
    JTextArea chatArea;
    JTextField chatBox;
    Border border;
    JScrollPane scroll;

    public ChatBot() {
        JPanel gui = new JPanel(new BorderLayout(5,5));
        frame = new JFrame("Product Bot");
        frame.setContentPane(gui);
        chatArea = new JTextArea(10, 50);
        chatBox = new JTextField();
        scroll = new JScrollPane(chatArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        border = BorderFactory.createLineBorder(Color.BLUE, 1);
        chatBox.setBorder(border);

        JLabel bot = new JLabel(
                "Hello! I am a \"Product Bot\"! that answers product related queries! " +
                        "Ask me by typing below. Type \"QUIT\" to end the program.");
        chatArea.append("Chats: \n");
        chatBox.setText("Chat Box");

        gui.add(chatBox, BorderLayout.PAGE_START);
        gui.add(scroll);
        gui.add(bot, BorderLayout.PAGE_END);
        gui.setBorder(new EmptyBorder(5,5,5,5));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new ChatBot();
    }