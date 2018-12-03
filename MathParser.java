import java.awt.*;

import javax.swing.JFrame; 
import javax.swing.JOptionPane; 
import javax.swing.JPanel;


// **********************  Input the Equation  **********************************
public class MathParser {


	public static boolean inputEquation() { // input equation and handle input errors
		
        // for example "-5-6/(-2) + sqr(15+x)"
   		String equation = JOptionPane.showInputDialog("equation");
   		MathEvaluator m = new MathEvaluator(equation);
   		System.out.println("you just enter equation as: " + equation); 
   		//m.addVariable("x", ((0.0)+xb));
        m.addVariable("x", 15.1d);
   		m.getValue();
   		//double value_test = m.getValue();
   		//System.out.println("is equation null?   " + value_test);
   		
   		if (m.getValue()== null) {
            JOptionPane.showMessageDialog (null, "EQUATION STRING NOT INPUT PROPERLY", "EQUATION ERROR", JOptionPane.INFORMATION_MESSAGE);
   		    return false; // equation not input correctly
   		}// end if statement
   		
   		return true;
		
	} 

    public static void main(String[] args) { 
        boolean isCorrectEuqation = inputEquation(); 
        System.out.println(isCorrectEuqation);  
    }
} 
