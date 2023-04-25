package Client.UI;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class initDesign {

    private JFrame frame;
    private ipLabel iplabel;
    private ipTextField iptextfield;
    private idLabel idlabel;
    private nameLabel namelabel;
    private idTextField idtextfield;
    private nameTextField nametextfield;
    private Connectbtn connectbtn;
    private String ip=null;
    private String id=null;
    private String name=null;
    public void makeFrame() {
        drawFrame();
        drawIpSection();
        drawIdSection();
        drawNameSection();
        drawConnectBtn();
        frame.repaint();
        Event();
    }

    private void drawFrame() {
        frame = new JFrame();
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    private void drawIpSection() {
        drawipLabel();
        drawipTextField();
    }

    private void drawipLabel() {
        iplabel = new ipLabel();
        frame.add(iplabel);
    }

    private void drawipTextField() {
        iptextfield = new ipTextField();
        frame.add(iptextfield);
    }

    private void drawIdSection() {
        drawidLabel();
        drawidTextField();
    }

    private void drawidLabel() {
        idlabel = new idLabel();
        frame.add(idlabel);
    }

    private void drawidTextField() {
        idtextfield = new idTextField();
        frame.add(idtextfield);
    }

    private void drawNameSection() {
        drawnameLabel();
        drawnameTextField();
    }

    private void drawnameLabel() {
        namelabel = new nameLabel();
        frame.add(namelabel);
    }

    private void drawnameTextField() {
        nametextfield = new nameTextField();
        frame.add(nametextfield);
    }


    private void drawConnectBtn() {
        connectbtn = new Connectbtn();
        frame.add(connectbtn);
    }

    private void Event() {
        connectbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ip=iptextfield.getText();
                id=idtextfield.getText();
                name=nametextfield.getText();
                frame.dispose();
            }
        });
    }

    public String getID() {
        return this.id;
    }

    public String getIP() {
        return this.ip;
    }
    public String getName(){ return this.name;}

}

