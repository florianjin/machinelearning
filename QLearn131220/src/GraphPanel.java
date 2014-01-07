import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class GraphPanel extends JComponent {

	/**
	 * @param args
	 */
	int num;
	Graphics2D g;
	ArrayList<Double> His;//����
	double maxD=100;//�������ģ����ڼ���Y��ѹ����
	double minD=0;//������С�ģ����ڼ���Y��ѹ����
	int length;//��Ҫ�ĳ��ȣ���x����Ϊ0��ʼ
	Point2D o;//ԭ��
	Point2D rightX;//X��ĩ��
	Point2D topY;//Y��ĩ��
	Line2D xLine;//X��
	Line2D yLine;//Y��
	String xHint;//X���ǩ
	String yHint;//Y���ǩ
	double maxX;//X�᳤��
	double maxY;//Y�᳤��
	double xScale=1;//X���ϵ����ݴ�Сѹ����
	double yScale=1;//Y���ϵ����ݴ�Сѹ����
	boolean OuterScale;//�Ƿ�Ҫ�Զ�����ѹ����
	boolean OuterLength;//�Ƿ�Ҫ�Զ���His�ĳ�����ΪX�����ݳ���
	private void getMaxD(){
		maxD=-100000000;minD=100000000;
		double x;
		for(int i=0;i<length;i++)
		{
			x=His.get(i);
			if(x>maxD) maxD=x;
			if(x<minD) minD=x;
		}
	}
	private void autoScale(){
		if(!OuterLength) length=His.size();
		getMaxD();
		if(OuterScale) return;
		xScale = maxX*0.9/length;
		yScale = maxY*0.9/(maxD-minD);
	}
	private void paintDataPoint(double x, double y){
		Point2D p = new Point2D.Double(x*xScale+o.getX(), o.getY()-(y-minD)*yScale);
		Point2D p1 = new Point2D.Double(p.getX()+1, p.getY());
		Line2D l = new Line2D.Double(p, p1);
		g.draw(l);
	}
	private void paintHisData(){
		autoScale();
		for(int i=0;i<length;i++){
			paintDataPoint(i,His.get(i));
		}
	}
	public void initialize(){
		double w = this.getWidth();
		double h = this.getHeight();
		//System.out.println(w+" "+h);
		o = new Point2D.Double(w*0.1, h*0.9);
		rightX = new Point2D.Double(w*0.9, o.getY());
		xLine = new Line2D.Double(o, rightX);
		topY = new Point2D.Double(o.getX(), h*0.1);
		yLine = new Line2D.Double(o, topY);
		xHint="ѵ������";
		yHint="Vֵƫ��";
		maxX = rightX.getX()-o.getX();
		maxY = o.getY()-topY.getY();
	}
	public GraphPanel(){
		
		//��ʼ��ʱ��û�п�͸�
	}
	private void paintOuter(){
		initialize();
		g.setPaint(Color.WHITE);
		Rectangle2D rect = new Rectangle2D.Double(0,0,this.getWidth(),this.getHeight());
		g.fill(rect);
		g.setPaint(Color.BLACK);
		//System.out.println(xLine.getX1()+" "+ xLine.getY1());
		g.draw(xLine);
		g.draw(yLine);
		GeneralPath xArrow = new GeneralPath();
		xArrow.moveTo(rightX.getX(), rightX.getY()-5);
		xArrow.lineTo(rightX.getX(), rightX.getY()+5);
		xArrow.lineTo(rightX.getX()+9, rightX.getY());
		xArrow.closePath();
		g.fill(xArrow);
		GeneralPath yArrow = new GeneralPath();
		yArrow.moveTo(topY.getX()-5, topY.getY());
		yArrow.lineTo(topY.getX()+5, topY.getY());
		yArrow.lineTo(topY.getX(), topY.getY()-9);
		yArrow.closePath();
		g.fill(yArrow);
		g.drawString(xHint, (int)(o.getX()+maxX/2.5), (int)(o.getY()+20));
	}
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		this.g=g2d;
		g2d.setPaint(new Color(0,128,128));
		//System.out.println(num++);
		paintOuter();
		paintHisData();
		//int red = (int)(Math.random()*255);
		//System.out.println(red);
		//g.fillRect(0, 0, this.getWidth(), this.getHeight());
		//System.out.println(this.getX()+" "+this.getY()+" "+this.getWidth()+ " "+this.getHeight());
		//System.out.println(g.getColor());
	}
	public Dimension getPreferredSize(){
		return new Dimension(400,400);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				
				AscenQLearning ql = new AscenQLearning();
				ql.fillRoutePool();
				ql.clearForClassic();
				LearningRecord lr = ql.runNormal(ql.new TrainMethod1());
				
				JFrame fr = new JFrame("ss");
				JButton b = new JButton("asssny");
				GraphPanel gp = new GraphPanel();
				gp.His=lr.VDisHis;
				fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				fr.getContentPane().add(gp);
				Toolkit kit = Toolkit.getDefaultToolkit();
				Dimension scrD = kit.getScreenSize();
				//System.out.println(scrD);
				fr.setSize(new Dimension(300,300));
				fr.setBounds(new Rectangle(100,200,300,400));
				b.addActionListener(new MainView());
				//System.out.println(JFrame.EXIT_ON_CLOSE);
				//fr.setExtendedState(Frame.MAXIMIZED_BOTH);
				fr.pack();
				fr.setVisible(true);
			}
		});
	}

}
