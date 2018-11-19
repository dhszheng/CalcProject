import java.awt.*;
import javax.swing.JComponent;
import javax.swing.JFrame;


public class DrawingStuff extends JComponent {

public void paintComponent(Graphics g)
{   
     //w is x, and h is y (as in x/y values in a graph)
     int w = this.getWidth()/2;
     int h = this.getHeight()/2;

 Graphics2D g1 = (Graphics2D) g;
 g1.setStroke(new BasicStroke(2));
 g1.setColor(Color.black);
 g1.drawLine(0,h,w*2,h);
 g1.drawLine(w,0,w,h*2); 
 g1.drawString("0", w - 7, h + 13);


 Graphics2D g2 = (Graphics2D) g;
 g2.setStroke(new BasicStroke(2));
  g2.setColor(Color.red);
  //line1
  Polygon p = new Polygon();
  for (int x = 0; x <= 4; x++) {
      p.addPoint(w+x, h - ((x*x*x) + x - 3));

  }
  g2.drawPolyline(p.xpoints, p.ypoints, p.npoints);

  Polygon p1 = new Polygon();
  for (int x = -10; x <= 10; x++) {
      p1.addPoint(w + x, h - ((x*x*x)/100) - x + 10);
  }
  g2.drawPolyline(p1.xpoints, p1.ypoints, p1.npoints); 
}

public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setSize(800, 600);
    frame.setTitle("Graphs");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);  
    DrawingStuff draw = new DrawingStuff();
    frame.add(draw);
    frame.setVisible(true);
    }
}
