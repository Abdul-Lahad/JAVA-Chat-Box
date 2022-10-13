import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class test extends JFrame {
    JPanel panel=new JPanel();
    test(){

 
        this.setSize(650,650);
        this.setVisible(true);
        panel.setBackground(Color.GREEN);
        panel.setSize(200, 50);

    

         this.add(panel,BorderLayout.AFTER_LINE_ENDS);
        
    }
    
    public static void main(String[] args) {
        
    new test();
        
    }
}
