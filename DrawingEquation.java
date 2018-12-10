import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.*;



public class DrawingEquation extends JComponent {
    private double [] xvalues;
    private double [] yvalues;
    private double xmin = 0.0;
    private double xmax = 0.0;
    private double ymin = 0.0;
    private double ymax = 0.0;
    private boolean removeDiscontinuity = false;
    private ArrayList<Pair> discontinuity = new ArrayList<Pair>(2);

    public  DrawingEquation() {
        JPanel panel = new JPanel();
        panel.setSize(800, 800);
        panel.setLocation(0, 0);
        panel.setVisible(true);
    }

    public void setRemoveDiscontinuity(boolean removal) {
        removeDiscontinuity = removal;
    }

    public void add_xy_values(double [] xval, double [] yval) {
        xvalues = xval;
        yvalues = yval;
        for(int i = 0; i < yvalues.length; i++) {
            if (ymin > yvalues[i]) {
                ymin = yvalues[i];
            }
            if (ymax < yvalues[i]) {
                ymax = yvalues[i];
            }
            if (xmin > xvalues[i]) {
                xmin = xvalues[i];
            }
            if (xmax < xvalues[i]) {
                xmax = xvalues[i];
            }
        }
        if (ymin > 0.0) {
            ymin = 0.0;
        }
        if (ymax < 0.0) {
            ymax = 0.0;
        }
    }

    public void add_xy_values(double x0, double x1, double y0, double y1, double [] xval, double [] yval) {
        xvalues = xval;
        yvalues = yval;
        for(int i = 0; i < yvalues.length; i++) {
            if (ymin > yvalues[i]) {
                ymin = yvalues[i];
            }
            if (ymax < yvalues[i]) {
                ymax = yvalues[i];
            }
            if (xmin > xvalues[i]) {
                xmin = xvalues[i];
            }
            if (xmax < xvalues[i]) {
                xmax = xvalues[i];
            }
        }
        if (ymin > 0.0) {
            ymin = 0.0;
        }
        if (ymax < 0.0) {
            ymax = 0.0;
        }
        if (ymin < y0) {
            ymin = y0;
        }
        if (ymax > y1) {
            ymax = y1;
        }
    }

    public void addDiscontuity(boolean remove, ArrayList disct) {
        removeDiscontinuity = remove;
        discontinuity = disct;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g1 = (Graphics2D) g;
        int offset = 10;
        int SCREEN_WIDTH = getWidth() - offset * 2;
        int SCREEN_HEIGHT = getHeight() - offset * 2;

        /**
         *  Convert xvalues and yvalues into screen positions
         */

        double yunit_pixel = SCREEN_HEIGHT / Math.abs(ymax - ymin);
        double xunit_pixel = SCREEN_WIDTH / Math.abs(xmax - xmin);

        int xorigin = (int) Math.round((0 - xmin) * xunit_pixel) + offset;
        int yorigin = SCREEN_HEIGHT - (int) Math.round((0 - ymin) * yunit_pixel) + offset;

        // Draw coordinates with lightGray
        g1.setStroke(new BasicStroke(2));
        g1.setColor(Color.lightGray);
        g1.drawLine(offset, yorigin, SCREEN_WIDTH, yorigin);
        g1.drawLine(SCREEN_WIDTH / 2, offset, SCREEN_WIDTH / 2, SCREEN_HEIGHT + offset);
        // Label coordinate (0, 0)
        g1.setColor(Color.black);
        //g1.drawString("0", SCREEN_WIDTH / 2 - 7, yorigin + 13);

        // Draw x axis ticks and scaled labeling
        for(int i = 0; i < xvalues.length; i++) {
            if (i % 10 == 0 ) {
                int xpos = (int) Math.round((xvalues[i] - xmin) * xunit_pixel) + offset;
                g1.drawLine(xpos, yorigin, xpos, yorigin + 5);
                g1.drawString(String.valueOf((int)xvalues[i]), xpos, yorigin - 10);
            }
        }

        // Draw y axis ticks and scaled labeling
        // Use a total of num_y_ticks (default = 10)
        // A total SCREEN_HEIGHT and
        int num_y_ticks = 15;
        double tick_interval = (ymax - ymin) / num_y_ticks;
        int [] y_tick_values = new int[num_y_ticks];
        for (int i = 0; i < num_y_ticks; i++) {
            y_tick_values[i] = (int) (ymin + i * tick_interval);
        }

        for(int j = 0; j < num_y_ticks; j++) {
            int ypos = SCREEN_HEIGHT - (int)((y_tick_values[j] - ymin) * yunit_pixel) + offset;
            g1.drawLine(SCREEN_WIDTH / 2, ypos, SCREEN_WIDTH / 2 + 5, ypos);
            g1.drawString(String.valueOf(y_tick_values[j]), SCREEN_WIDTH / 2 + 20, ypos);
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
            //double xpos = x * xscale;
            //double ypos = (ymax - yvalues[x]) * yunit_pixel;
            double xpos = (xvalues[x] - xmin) * xunit_pixel + offset;
            double ypos = SCREEN_HEIGHT - (yvalues[x] - ymin) * yunit_pixel + offset;
            p.addPoint((int)xpos, (int)ypos);

        }
        g2.drawPolyline(p.xpoints, p.ypoints, p.npoints);

        if (removeDiscontinuity) {
            Pair pair = new Pair();
            for(int i = 0; i < discontinuity.size(); i++)
                pair = discontinuity.get(i);
                double xpos = (pair.getX() - xmin) * xunit_pixel + offset / 2;;
                double ypos = SCREEN_HEIGHT - (pair.getY() - ymin) * yunit_pixel + offset;

                Ellipse2D.Double cir = new Ellipse2D.Double(xpos, ypos, 10, 10);
                g2.setColor(Color.blue);
                g2.draw(cir);
        }

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
