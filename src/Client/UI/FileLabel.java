package Client.UI;


import javax.swing.*;
import java.awt.*;

public class FileLabel extends JLabel {
    public FileLabel(){
        super(" 파일 이름을 입력한 후 ENTER 를 누르세요. SEND 버튼을 누를 시 업로드 됩니다. ");
        setForeground(Color.BLUE);
        setFont(new Font("Serif",Font.BOLD, 15));
        setBounds(90,450,4000,30);
    }
}

