package Client.UI;


import javax.swing.*;
import java.awt.*;

// 연결된 유저의 이름을 보여줍니다.
public class fileNameField extends JTextField {
    public fileNameField() {
        super(" Selected Files : ");
        setBounds(90,400,500,30);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        this.setDisabledTextColor(Color.BLACK);
        setEnabled(false);
    }
}
