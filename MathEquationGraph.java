import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.*;


public class MathEquationGraph extends JFrame implements ActionListener {
    private JLabel greeting = new JLabel("Please enter your equation:");
    private JTextField equation = new JTextField(50);
    private JButton graphButton = new JButton("Graph it");
    private JTextField message = new JTextField(60);
    private DrawingEquation graph = new DrawingEquation();

    // layout
    public MathEquationGraph() {
        // layout components
        JPanel topPanel = new JPanel();

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        topPanel.setLayout(gridbag);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        gridbag.setConstraints(greeting, c);
        topPanel.add(greeting);
        gridbag.setConstraints(equation, c);
        topPanel.add(equation);
        String default_equation = "x * x - 10 * x + 6";
        equation.setText(default_equation);
        c.gridwidth = GridBagConstraints.REMAINDER; //end of row
        gridbag.setConstraints(graphButton, c);
        topPanel.add(graphButton);

        c.weightx = 0.0;                      //reset to the default
        gridbag.setConstraints(message, c);   // add row
        topPanel.add(message);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        /*
        Draw the default equation with 100 poins
         */
        int npoints = 100;
        int xmin = -10;
        int xmax = 10;
        double xunit = (double) (xmax - xmin) / (double) (npoints + 1);
        double [] xvalues = new double[npoints];
        double [] yvalues = new double[npoints];
        MathEvaluator m = new MathEvaluator(default_equation);
        for (int i = 0; i < npoints; i++) {
            double x = xmin + xunit * i;
            m.addVariable("x", x);
            xvalues[i] = x;
            yvalues[i] = Double.valueOf(m.getValue());
        }
        //message.setText(Arrays.toString(yvalues));

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
            //message.setText(equationValue);

            int xmin = -10;
            int xmax = 10;
            int npoints = 100;
            double xunit = (double) (xmax - xmin) / (double) (npoints + 1);
            double [] xvalues = new double[npoints];
            double [] yvalues = new double[npoints];
            MathEvaluator m = new MathEvaluator(equationText);
            for (int i = 0; i < npoints; i++) {
                double x = xmin + xunit * i;
                m.addVariable("x", x);
                xvalues[i] = x;
                yvalues[i] = Double.valueOf(m.getValue());
            }
            message.setText(equationText);
            graph.add_xy_values(xvalues, yvalues);
            graph.repaint();
            /*
            MathEvaluator m = new MathEvaluator(equationText);
            m.addVariable("x", 15.1d);
            Double eqValue = m.getValue();
            Double v = m.getVariable("x");
            message.setText("x=" + v.toString() + ": " + eqValue.toString());
            */
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
