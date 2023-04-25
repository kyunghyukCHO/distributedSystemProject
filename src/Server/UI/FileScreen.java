package Server.UI;

import javax.swing.*;
import java.awt.*;

public class FileScreen extends JTextArea {
    public FileScreen(){
        setBounds(10, 150, 500 , 240);
        this.setDisabledTextColor(Color.GRAY);
        setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 1));
        setEnabled(false);
    }
}
