package Server.UI;

import javax.swing.*;
import java.awt.*;


public class ChatScreen extends JTextArea {
    public ChatScreen(){
        setBounds(10, 150, 500 , 300);
        this.setDisabledTextColor(Color.GRAY);
        setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 1));
        setEnabled(false);
    }
}
