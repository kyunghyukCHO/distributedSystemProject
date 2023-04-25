package Server.UI;

import javax.swing.*;
import java.awt.*;

public class FileNameLabel extends JLabel {
    public FileNameLabel(){
        super(" List of files uploaded to the server ");
        setForeground(Color.BLUE);
        setFont(new Font("Serif",Font.BOLD, 20));
        setBounds(10,80,500,20);
    }
}
