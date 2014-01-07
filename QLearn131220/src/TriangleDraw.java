import javax.swing.JFrame;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
/**
* 随机画一个三角形,并用随机的颜色填充
* @author Administrator
*
*/
public class TriangleDraw extends JFrame {
     /**
     * @param args
     */
    
     public TriangleDraw(){
              super("TriangleDraaw");
              setSize(400,400);
              setVisible(true);
             
     }
    
     public void paint(Graphics g){
              super.paint(g);
             
              Random random = new Random();
             
              int xPoint[] = new int[3];
              int yPoint[] = new int[3];
              for(int i = 0; i < xPoint.length; i ++){
                       xPoint[i] = random.nextInt(400);
                       yPoint[i] = random.nextInt(400);
                      
              }
             
              GeneralPath triangle = new GeneralPath();
              triangle.moveTo(xPoint[0], yPoint[0]);
              for(int i = 1; i < 3; i ++){
                       triangle.lineTo(xPoint[i], yPoint[i]);
              }                
              triangle.closePath();
             
             
              Color color1 = new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255));
              Color color2 = new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255));
             
              Graphics2D g2d = (Graphics2D)g;
              g2d.setPaint(new GradientPaint(random.nextInt(400),78,color1,random.nextInt(400),78,color2,true));
              g2d.fill(triangle);
     }
     public static void main(String[] args) {
              // TODO 自动生成方法存
              TriangleDraw application = new TriangleDraw();
              application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     }
}