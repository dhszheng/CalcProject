import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Polynomial {

    private double[] coeffs;
    private int degree = 0;     // degree of polynomial (-1 if not polynomia)

    /*
    The coefficients are listed in order from highest to lowest degree.
       The degree is the size of the array - 1.

       Example: 6x^3 - 1.6x^2 + 3.6 is represented as {6.0, -1.6, 0.0, 3.6}

     */
    public Polynomial(double[] coeffs) {
        this.coeffs = coeffs;
        degree = coeffs.length - 1;
    }

    /*
    Construct coefficients of polynomial from string
     */
    public Polynomial(String equation) {
        // Initialize this polynomial to zero
        int nTerms = 0;
        Term [] termList = new Term[1];

        // Setup the basic regular expressions for matching terms
        String xPattern     = "x(\\^([0-9]+))?";
        Pattern termPattern =
                Pattern.compile("\\s*(" + xPattern + ")" + "|"
                        + "\\s*([-+]?[0-9]+(\\.[0-9]*)?)[*\\s]?(" + xPattern + ")?");
        Pattern signPattern = Pattern.compile("\\s*([-+])\\s*");

        // Match a sequence of signs and terms
        Matcher matcher;  // reassigned as needed for matching the patterns
        int sign = 1;     // incoming sign from the operator
        int maxPower = 0;
        while (true) {
            Term term = new Term();

            // The next thing in 'equation' should be a term
            matcher = termPattern.matcher(equation);
            boolean result = matcher.lookingAt();
            if (!result) {
                // syntax error: stop with what he have
                break;
            }


            // Uncomment this to show the capture groups
	        for (int k = 0; k < 9; k++)
	            System.out.println("group " + k + ": " + matcher.group(k));
	            System.out.println("");


            // extract the coefficient and power from the captured groups
            if (matcher.group(1) != null) {
                // group 1 contains the first alternation, which matches
                // just "x^<power>" without a coefficient
                term.coeff = sign;
                int thePower = 0;
                if (matcher.group(2) == null) {
                    // the power is 1
                    thePower = 1;
                }
                else {
                    // the power is in group 3
                    thePower = Integer.valueOf(matcher.group(3));
                }
                term.power = thePower;
                if (maxPower < thePower) {
                    maxPower = thePower;
                }
            }
            else {
                // otherwise, the second alternation matches, which has an
                // explicit coefficient but an optional "x" term
                term.coeff = sign*Double.valueOf(matcher.group(4));
                // get the exponent
                int thePower = 0;
                if (matcher.group(6) == null) {
                    // the "x" term is omitted; take it as x^0
                    thePower = 0;
                }
                else if (matcher.group(7) == null) {
                    // the exponent is omitted, take it as 1
                    thePower = 1;
                }
                else {
                    thePower = Integer.valueOf(matcher.group(8));
                }
                term.power = thePower;
                if (maxPower < thePower) {
                    maxPower = thePower;
                }
            }

            // update the position in the input string
            equation = equation.substring(matcher.end());

            // add the term
            if (nTerms >= termList.length) {
                Term newTerms[] = new Term[2*nTerms];
                for (int k = 0; k < termList.length; k++)
                    newTerms[k] = termList[k];
                termList = newTerms;
            }
            termList[nTerms++] = term;


            // If there is a "+" or "-", take it as a binary operator
            // (which indicates there is another term coming)
            matcher = signPattern.matcher(equation);
            if (matcher.lookingAt()) {
                sign = (matcher.group(1).equals("-") ? -1 : 1);
                equation = equation.substring(matcher.end());
            }
            else {
                break;
            }
        }

        /* convert and sort termList
        The term has coefficient, power and sign. After parsing, the termList are
        {[6.0, 3, 1], [1.6, 2, -1], [3.6, 0, 1]. There is no tern for 0^x. Need to create
        this as [0.0]
         Example: 6x^3 - 1.6x^2 + 3.6 is represented as {6.0, -1.6, 0.0, 3.6}
         */
        System.out.println("nTerms: " + termList.length);
        double [] theCoeffs = new double[maxPower + 1];
        for (int i = 0; i < termList.length - 1; i++) {
            int idx = maxPower - termList[i].getPower();
            theCoeffs[idx] = termList[i].getCoeff();
            String out = "Idx: " + idx + "      Coeff: " + termList[i].getCoeff() + "    Power: " + termList[i].getPower();
            System.out.println(out);
        }
        this.coeffs = theCoeffs;
        this.degree = maxPower;
        for (int i = 0; i <= maxPower; i++) {
            String out = "Power: " + (maxPower - i);
            out += "; Coeff: " + theCoeffs[i];
            System.out.println(out);
        }
    }

    public double getCoeff(int x) {
        return coeffs[x];
    }

    /*
    the degree is the size of the coefficients - 1
    the length of the array is not the degree of the polynomial if leading zero coefficients
    */
    private void doDegree() {
        degree = this.coeffs.length;
    }

    public int getDegree() {
        return degree;
    }

    // Given an x value, calculate the result
    public double evaluate(double x) {
        double res = 0.0;
        for (int i = 0; i < coeffs.length; i++) {
            res += coeffs[i] * Math.pow(x, degree - i);
        }
        return res;
    }

    /*
    Generate the derivative of the Polynomial function
     */
    public Polynomial derivative() {
        double[] new_coeffs = new double[coeffs.length - 1];
        for (int i = 0; i < new_coeffs.length; i++) {
            new_coeffs[i] = coeffs[i] * (degree - i);
        }
        return new Polynomial(new_coeffs);
    }

    /*
    Returns a string representation for the polynomial function.
    Example:
    x^2 - 4x + 4
    2x - 3
    // use for loop from high degree to 0 to process each coefficient
    If coefficient is 1, omit it.
    If -1, use - (instead of +)
     */
    //
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        //if (degree == -1) return "0";
        //else if (degree == 0) return "" + coeffs[0];
        //else if (degree == 1) return coeffs[1] + "x + " + coeffs[0];
        int power = degree;
        String ret = "";
        for (int i = 0; i < coeffs.length; i++) {
            power = degree - i;
            if (power < 0) {
                ret = "0";
            } else if (power == 0) {
                if (coeffs[i] > 0) {
                    ret += " + " + df.format(coeffs[i]);
                }
                else if (coeffs[i] < 0) {
                    ret += " " + df.format(coeffs[i]);
                }
            } else if (power == 1) {
                if (coeffs[i] > 0) {
                    ret += " + " + df.format(coeffs[i]) + "x";
                }
                else if (coeffs[i] < 0) {
                    ret += " " + df.format(coeffs[i]) + "x";
                }
            } else {
                if (coeffs[i] > 0) {
                    ret += " + " + df.format(coeffs[i]) + "x^" + power;
                }
                else if (coeffs[i] < 0) {
                    ret += " " + df.format(coeffs[i]) + "x^" + power;
                }
            }
        }
        return ret;
    }


    public String toString2() {
        DecimalFormat df = new DecimalFormat("#.##");
        String ret = "";
        for (int i = coeffs.length - 1; i >= 0; i--) {
            if (coeffs[i] == 1) ret += "+";
            else if (coeffs[i] == -1) ret += "-";
            else if (coeffs[i]>0) ret = ret + "+" + df.format(coeffs[i]);
            else if (coeffs[i] == 0) continue;
            else ret = ret + df.format(coeffs[i]);
            if (i == 1) ret += "x";
            else if (i != 0) ret += "x^" + i;
        }
        // If first is a + sign, remove it
        if (ret.charAt(0)=='+') {
            ret = ret.substring(1, ret.length());
        }
        ret = "y=" + ret;
        String out = "";
        for (int i = 0; i < coeffs.length; i++) {
            out += " " + i + ":" + String.valueOf(coeffs[i]);
        }
        System.out.println("Coeefs: " + ret + " ---" + out);
        return ret;
    }

    /*
    Adds the polynomial with another polynomial
    Note that you can't predict the degree in advance.
	For example: (x^2 + 5x - 5)  + (-x^2 -5x + 5) = 0
     */
    //
    public Polynomial plus(Polynomial other) {
        int lower;
        double[] new_coeffs;
        boolean other_higer_degree;
        if (this.coeffs.length > other.coeffs.length) {
            other_higer_degree = false;
            lower = other.coeffs.length;
            new_coeffs = new double[this.coeffs.length];
        } else {
            other_higer_degree = true;
            lower = this.coeffs.length;
            new_coeffs = new double[other.coeffs.length];
        }
        for (int i = 0; i < lower; i++) {
            new_coeffs[i] = this.coeffs[i] + other.coeffs[i];
        }
        if (other_higer_degree) {
            for (int i = lower; i < new_coeffs.length; i++) {
                new_coeffs[i] = other.coeffs[i];
            }
        } else {
            for (int i = lower; i < new_coeffs.length; i++) {
                new_coeffs[i] = this.coeffs[i];
            }
        }
        return new Polynomial(new_coeffs);
    }

    /*
    subtracts a polynomial from the current polynomial
    First times -1 for the other, then add them together
     */
    //
    public Polynomial minus(Polynomial other) {
        for (int i = 0; i < other.coeffs.length; i++) {
            other.coeffs[i] = -other.coeffs[i];
        }
        return this.plus(other);
    }

    // multiplies this polynomial by another
    public Polynomial times(Polynomial other) {
        int new_degree = this.coeffs.length + other.coeffs.length - 1;
        double[] new_coeffs = new double[new_degree];
        // populate new_coeffs with 0s
        for (int i = 0; i < new_degree; i++) {
            new_coeffs[i] = 0;
        }
        for (int i = 0; i < this.coeffs.length; i++) {
            for (int j = 0; j < other.coeffs.length; j++) {
                int current_degree = i + j;
                new_coeffs[current_degree] += (this.coeffs[i] * other.coeffs[j]);
            }
        }
        return new Polynomial(new_coeffs);
    }


    protected class Term {
        private double coeff = 0.0;
        private int power = 0;
        private int sign = 1;

        public Term() {
            super();
            coeff = 0.0;
            power = 0;
            sign = 1;
        }

        public Term(double coeff, int power, int sign) {
            this.coeff = coeff;
            this.power = power;
            this.sign = sign;
        }
        public double getCoeff() {
            return coeff;
        }

        public int getPower() {
            return power;
        }

        public int getSign() {
            return sign;
        }
    }

}