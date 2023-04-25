package Server.UI;

import javax.swing.*;
import java.awt.*;

public class ClientNameLabel extends JLabel {
    public ClientNameLabel(){
        super(" Connecting clients & Log ");
        setForeground(Color.BLUE);
        setFont(new Font("Serif",Font.BOLD, 20));
        setBounds(10,370,500,20);
    }
}
