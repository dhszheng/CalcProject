import java.awt.*;

import javax.swing.JFrame; 
import javax.swing.JOptionPane; 
import javax.swing.JPanel;


// hello!
// **********************  Input the Equation  **********************************
public class MathParser {


	public boolean inputEquation(){ // input equation and handle input errors
		
   		String equation = JOptionPane.showInputDialog("equation");
   		MathEvaluator m = new MathEvaluator(equation);
   		
   		
   		m.addVariable("x", ((0.0)+xb));
   		m.getValue();
   		//double value_test = m.getValue();
   		//System.out.println("is equation null?   " + value_test);
   		
   		if (m.getValue()== null){JOptionPane.showMessageDialog (null, "EQUATION STRING NOT INPUT PROPERLY", "EQUATION ERROR", JOptionPane.INFORMATION_MESSAGE);
 		
   		 return false; // equation not input correctly
   		}// end if statement
   		
   		return true;
		
	}

    public static void main(String[] args) {

        System.out.println(args[0]); 
    }
} 