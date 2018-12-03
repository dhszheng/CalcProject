import java.awt.*;
import javax.swing.*;



public class DrawingEquation extends JComponent {
    private double [] xvalues;
    private double [] yvalues;

    public  DrawingEquation() {
        JPanel panel = new JPanel();
        panel.setSize(800, 800);
        panel.setLocation(0, 0);
        panel.setVisible(true);
    }

    public void add_xy_values(double [] xval, double [] yval) {
        xvalues = xval;
        yvalues = yval;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g1 = (Graphics2D) g;
        int SCREEN_WIDTH = getWidth() - 20;
        int SCREEN_HEIGHT = getHeight() - 20;

        /**
         *  Convert xvalues and yvalues into screen positions
         */
        double ymin = 0.0;
        double ymax = 0.0;
        for(int i = 0; i < yvalues.length; i++) {
            if (ymin > yvalues[i]) {
                ymin = yvalues[i];
            }
            if (ymax < yvalues[i]) {
                ymax = yvalues[i];
            }
        }
        if (ymin > 0.0) {
            ymin = 0.0;
        }
        if (ymax < 0.0) {
            ymax = 0.0;
        }
        double yunit_pixel = SCREEN_HEIGHT / Math.abs(ymax - ymin);
        int yorigin = (int) Math.round(100.0 * (ymax - 0) * yunit_pixel) / 100;
        int xorigin = SCREEN_WIDTH / 2;

        // Draw coordinates with lightGray
        g1.setStroke(new BasicStroke(2));
        g1.setColor(Color.lightGray);
        g1.drawLine(0, yorigin, SCREEN_WIDTH, yorigin);
        g1.drawLine(SCREEN_WIDTH / 2, 0, SCREEN_WIDTH / 2, SCREEN_HEIGHT);
        // Label coordinate (0, 0)
        g1.setColor(Color.black);
        g1.drawString("0", SCREEN_WIDTH / 2 - 7, yorigin + 13);

        // Draw x axis ticks and scaled labeling
        for(int i = 0; i < xvalues.length; i++) {
            int xpos = i * (SCREEN_WIDTH / xvalues.length);
            if (i % 5 == 0 ) {
                g1.drawLine(xpos, yorigin, xpos, yorigin + 5);
                //g1.drawString(String.valueOf(xvalues[i]), xpos - 7, yorigin + 13);
            }
        }

        // Draw y axis ticks and scaled labeling
        int num_y_ticks = (int) Math.round(100.0 * SCREEN_HEIGHT / yunit_pixel) / 100;
        for(int j = 0; j < num_y_ticks; j++) {
            int ypos = SCREEN_HEIGHT - j * (int) Math.round(yunit_pixel * 100)/100;
            if (j % 5 == 0) {
                g1.drawLine(SCREEN_WIDTH / 2, ypos, SCREEN_WIDTH / 2 + 5, ypos);
                //g1.drawString(String.valueOf(yunit_pixel * j), SCREEN_WIDTH / 2 + 7, ypos);
            }
        }


        /**
         *  Coordinate scaling factor
         *  x = SCREEN_WITH / (number of x values)
         *  y = SCREEN_HEIGHT / (number of y values)
         */
        int xscale = SCREEN_WIDTH / xvalues.length;
        int yscale = SCREEN_HEIGHT / yvalues.length;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.red);
        //line1
        Polygon p = new Polygon();
        for (int x = 0; x < xvalues.length; x++) {
            double xpos = x * xscale;
            double ypos = (ymax - yvalues[x]) * yunit_pixel;
            p.addPoint((int)xpos, (int)ypos);

        }
        g2.drawPolyline(p.xpoints, p.ypoints, p.npoints);

    }

    public static void main(String[] args) {
        JPanel panel = new JPanel();
        panel.setSize(800, 800);
        panel.setLocation(0, 0);
        DrawingEquation draw = new DrawingEquation();
        panel.add(draw);
        panel.setVisible(true);
    }
}
