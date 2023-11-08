package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.NumCalc;

public class NumCalc_tests {

    @Test
    public void test_evaluate() {
        String[][] tests = {
                // expression, expected_result, expected_exception_message
                { "1 + 1", "2", null },
                { "2 ^ 3 + 5 * 7", "43", null },
                { "1+1", null, "Unrecognized token: 1+1" },
                { "1 + / 3", null, "Wrong operands for operator: /" },
                { "7 * 3 / 0", null, "Division by zero" },
                { "3 + ", null, "Missing operands for operator: +" }
        };

        NumCalc numCalc = new NumCalc();
        for(String[] test : tests) {
            try {
                String result = numCalc.evaluate(test[0]);
                assertEquals(test[1], result);
            } catch(Exception e) {
                assertEquals(test[2], e.getMessage());
            }
        }
    }
    
    @Test
    public void test_evaluationTrace() {
        NumCalc numCalc = new NumCalc();
        String result = numCalc.evaluate("4 * 8 - 6 / 3 ^ 0 + 7 % 5 * -2.4");
        assertEquals("21.2", result);
        String expectedTrace = 
                "4 * 8 - 6 / 1 + 7 % 5 * -2.4\n"
              + "32 - 6 / 1 + 7 % 5 * -2.4\n"
              + "32 - 6 + 7 % 5 * -2.4\n"
              + "32 - 6 + 2 * -2.4\n"
              + "32 - 6 + -4.8\n"
              + "26 + -4.8\n"
              + "21.2\n";
        String actualTrace = numCalc.toString();
        assertEquals(expectedTrace, actualTrace);
    }
    

}
