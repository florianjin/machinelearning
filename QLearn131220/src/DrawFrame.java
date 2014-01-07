import javax.swing.JFrame;



public class DrawFrame extends JFrame {
	// 活动图窗口大小
	public static final int DEFAULT_WIDTH = 1000;
	public static final int DEFAULT_HEIGHT = 800;
	public DrawFrame() {
		setTitle("Activity Diagram");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		ArrowLinePanel panel = new ArrowLinePanel();
		add(panel);
	}
	
	public static void main(String args[]) {
		DrawFrame frame = new DrawFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	
}