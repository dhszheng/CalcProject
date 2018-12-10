import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;


public class MathEquationGraph extends JFrame implements ActionListener {
    private JLabel greeting = new JLabel("Equation:");
    private JTextField equation = new JTextField(15);
    private JTextField denominator = new JTextField(15);
    private JButton graphButton = new JButton("Graph it");
    private JLabel min_label = new JLabel("Min x value:");
    private JTextField min_xvalue = new JTextField(8);
    private JLabel max_label = new JLabel("Max x value:");
    private JTextField max_xvalue = new JTextField(8);
    private JLabel ymin_label = new JLabel("Min y value:");
    private JTextField ymin_value = new JTextField(8);
    private JLabel ymax_label = new JLabel("Max y value:");
    private JTextField ymax_value = new JTextField(8);
    //private JCheckBox rational = new JCheckBox("Rational", false);
    private JTextField message = new JTextField(50);
    private DrawingEquation graph = new DrawingEquation();

    private JRadioButton asis = new JRadioButton("As is");
    private JRadioButton der1 = new JRadioButton("First derivative");
    private JRadioButton der2 = new JRadioButton("Second derivative");
    private JRadioButton rational = new JRadioButton("Rational");
    private ButtonGroup bGroup = new ButtonGroup();


    // layout
    public MathEquationGraph() {
        // layout components
        /**
         * Make 2 JPanels. Top Panel for input. Bottom Panel for plot.
         * Put top panel at North and plot panel at Center using BorderLayout
         */
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();

        p1.setLayout(new FlowLayout());
        p2.setLayout(new FlowLayout());

        p1.add(greeting);
        p1.add(equation);
        String default_equation = "x^2 - 7*x + 12";
        equation.setText(default_equation);
        bGroup.add(asis);
        bGroup.add(der1);
        bGroup.add(der2);
        bGroup.add(rational);
        p1.add(asis);
        p1.add(der1);
        p1.add(der2);
        p1.add(rational);
        p1.add(denominator);

        p2.add(min_label);
        p2.add(min_xvalue);
        min_xvalue.setText("-10");
        p2.add(max_label);
        p2.add(max_xvalue);
        max_xvalue.setText("10");
        p2.add(ymin_label);
        p2.add(ymin_value);
        p2.add(ymax_label);
        p2.add(ymax_value);

        p2.add(graphButton);

        topPanel.add(p1, BorderLayout.NORTH);
        topPanel.add(p2, BorderLayout.CENTER);
        topPanel.add(message, BorderLayout.SOUTH);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        /*
        Draw the default equation with 100 points
         */
        int xmin = -10;
        int xmax = 10;
        int npoints = 10 * (xmax - xmin + 1);
        double xunit = (double) (xmax - xmin + 1) / npoints;

        double [] xvalues = new double[npoints];
        double [] yvalues = new double[npoints];
        MathEvaluator m = new MathEvaluator(default_equation);
        for (int i = 0; i < npoints; i++) {
            double x = xmin + xunit * i;
            m.addVariable("x", x);
            xvalues[i] = x;
            yvalues[i] = Double.valueOf(m.getValue());
        }

        //DrawingEquation graph = new DrawingEquation();
        graph.add_xy_values(xvalues, yvalues);
        getContentPane().add(graph, BorderLayout.CENTER);
        graph.setVisible(true);

        graphButton.addActionListener(this);

    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == graphButton) {
            String equationText = equation.getText();
            String denomText = denominator.getText();
            int xmin = Integer.valueOf(min_xvalue.getText());
            int xmax = Integer.valueOf(max_xvalue.getText());
            int npoints = 10 * (xmax - xmin + 1);
            double xunit = (double) (xmax - xmin + 1) / npoints;
            double [] xvalues = new double[npoints];
            double [] yvalues = new double[npoints];
            double ymin = 0.0;
            double ymax = 0.0;

            //reset removeDiscontinuity flag
            graph.setRemoveDiscontinuity(false);

            boolean isAsis = asis.isSelected();
            boolean isDer1 = der1.isSelected();
            boolean isDer2 = der2.isSelected();
            boolean isRational = rational.isSelected();

            String msg = "As is selected";
            if (isDer1) {
                msg = "First derivatie";
            }
            else if (isDer2) {
                msg = "Second derivative";
            }
            else if (isRational) {
                msg = "Rational";
            }
            message.setText(msg);
            /*
            If rational, find out if there is removable discontinuity
             */
            if (isRational) {
                if (denomText.isEmpty()) {
                    message.setText("Denominator is not provided. Please enter into box next to Rational button");
                }
                else {
                    message.setText("(" + equationText + ") / (" + denomText + ")");
                    Polynomial numator = new Polynomial(equationText);
                    Polynomial denom = new Polynomial(denomText);

                    // at which x, denominator == 0
                    ArrayList<Pair> discont = new ArrayList<Pair>(2);
                    boolean removeDiscontinuity = false;
                    for (int x = xmin; x < xmax; x++) {
                        double y = denom.evaluate(x);
                        if (Math.abs(y - 0.0) < 0.00001) {
                            // go to left and right of x to see if equation values are similar
                            double x1 = x - 0.001;
                            double x2 = x + 0.001;
                            double y1 = numator.evaluate(x1) / denom.evaluate(x1);
                            double y2 = numator.evaluate(x2) / denom.evaluate(x2);
                            if (Math.abs(y1 - y2) < 1000) {
                                double y_mean = (y1 + y2) / 2;
                                discont.add(new Pair(x, y_mean));
                                removeDiscontinuity = true;
                                System.out.println("x= " + x + ";    y_mean=" + y_mean);
                            }
                        }
                        System.out.println("x= " + x + ";    y=" + y);
                    }
                    if (removeDiscontinuity) {
                        graph.addDiscontuity(removeDiscontinuity, discont);
                    }

                    // generate x, y values for plotting
                    for (int i = 0; i < npoints; i++) {
                        double x = xmin + xunit * i;
                        xvalues[i] = x;
                        double yval = 0;
                        if (denom.evaluate(x) != 0.0) {
                            yval = numator.evaluate(x) / denom.evaluate(x);
                        }
                        //System.out.println("x= " + x + ";    y=" + yval);
                        yvalues[i] = yval;
                        if (ymin > yval) {
                            ymin = yval;
                        }
                        if (ymax < yval) {
                            ymax = yval;
                        }
                    }
                }

            } else if (isAsis) {
                message.setText("Equation: " + equationText);

                MathEvaluator m = new MathEvaluator(equationText);
                for (int i = 0; i < npoints; i++) {
                    double x = xmin + xunit * i;
                    m.addVariable("x", x);
                    xvalues[i] = x;
                    double yval = Double.valueOf(m.getValue());
                    yvalues[i] = yval;
                    if (ymin > yval) {
                        ymin = yval;
                    }
                    if (ymax < yval) {
                        ymax = yval;
                    }
                }
            }
            else if (isDer1) {
                Polynomial p = new Polynomial(equationText);
                Polynomial first_der = p.derivative();
                message.setText(first_der.toString());

                for (int i = 0; i < npoints; i++) {
                    double x = xmin + xunit * i;
                    double yval = first_der.evaluate(x);
                    xvalues[i] = x;
                    yvalues[i] = yval;
                    if (ymin > yval) {
                        ymin = yval;
                    }
                    if (ymax < yval) {
                        ymax = yval;
                    }
                }
            }
            else if (isDer2) {
                Polynomial p = new Polynomial(equationText);
                Polynomial first_der = p.derivative();
                Polynomial second_der = first_der.derivative();
                message.setText(second_der.toString());

                for (int i = 0; i < npoints; i++) {
                    double x = xmin + xunit * i;
                    double yval = second_der.evaluate(x);
                    xvalues[i] = x;
                    yvalues[i] = yval;
                    if (ymin > yval) {
                        ymin = yval;
                    }
                    if (ymax < yval) {
                        ymax = yval;
                    }
                }
            }


            if (!ymin_value.getText().isEmpty()) {
                ymin = Double.valueOf(ymin_value.getText());
            }
            if (!ymax_value.getText().isEmpty()) {
                ymax = Double.valueOf(ymax_value.getText());
            }
            graph.add_xy_values(xmin, xmax, ymin, ymax, xvalues, yvalues);
            graph.repaint();

        }
    }

    public static void main(String [] args) {
        JFrame frame = new MathEquationGraph();
        frame.setTitle("Math Equation Graphing");
        frame.setSize(800, 800);
        //frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
