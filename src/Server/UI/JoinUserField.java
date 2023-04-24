package Server.UI;


import javax.swing.*;
import java.awt.*;

// 연결된 유저의 이름을 보여줍니다.
public class JoinUserField extends JTextField {
    public JoinUserField() {
        super("Connected User : ");
        setBounds(10,70,480,30);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        this.setDisabledTextColor(Color.BLACK);
        setEnabled(false);
    }
}
