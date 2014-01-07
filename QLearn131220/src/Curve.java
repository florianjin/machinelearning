
import javax.swing.*;
import java.awt.*;


public class Curve {
	int[] arr = {16, 34, 47, 52, 53, 54, 70, 71, 105, 105, 105, 124, 124, 127};
    int x = 70;
    int y = 70;

    public static void main (String[] args) {
       Curve gui = new Curve ();
       gui.go();
   }

   public void go() {
       JFrame frame = new JFrame();
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       MyDrawPanel drawPanel = new MyDrawPanel();       

       frame.getContentPane().add(drawPanel);
       frame.setSize(300,300);
       frame.setVisible(true);
       drawPanel.repaint();
    
   }// close go() method


    class MyDrawPanel extends JPanel {
    
       public void paintComponent(Graphics g) {
    	   Graphics2D g2 = (Graphics2D)g;
          //g.setColor(Color.white);
          //g.fillRect(0,0,this.getWidth(), this.getHeight());
          g2.setColor(Color.black);
          //g.fillOval(x,y,40,40);
          ;
       }
    } // close inner class
} // close outer class