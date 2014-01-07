import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;


public class MainView implements ActionListener{

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame fr = new JFrame("ss");
		JButton b = new JButton("asssny");
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.getContentPane().add(b);
		fr.setSize(new Dimension(300,300));
		fr.setBounds(new Rectangle(100,200,300,400));
		b.addActionListener(new MainView());
		System.out.println(JFrame.EXIT_ON_CLOSE);
		fr.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getSource().getClass());
	}

}
