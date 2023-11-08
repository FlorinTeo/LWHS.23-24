package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import main.NumNode;
import main.OpNode;
import main.OpNode.OpCode;

public class OpNode_tests {

    @Test
    public void test_createNode() {
        String[] opStrings =  {"+", "-", "*", "/", "%", "^" };
        OpNode.OpCode[] opCodes = {
                OpCode.ADDITION, 
                OpCode.SUBTRACTION,
                OpCode.MULTIPLICATION, 
                OpCode.DIVISION,
                OpCode.MODULO,
                OpCode.POWER };
        OpNode opNode;
        for (int i = 0; i < opStrings.length; i++) {
            opNode = OpNode.createNode(opStrings[i]);
            assertNotNull(opNode);
            assertEquals(opCodes[i], opNode.getOpCode());
        }
        
        opNode = OpNode.createNode("abc");
        assertNull(opNode);
    }
    
    @Test
    public void test_evaluate() {
        String[][] tests = {
                // operand1, operator, "operand2", expected_result, expected_exception_message
                { "1", "+", "2", "3", null },
                { "1", "-", "2", "-1", null },
                { "3", "*", "4", "12", null },
                { "4", "/", "-5", "-0.8", null },
                { "7", "%", "5", "2", null },
                { "4", "^", "2", "16", null },
                { "8", "/", "0", null,"Division by zero" }
        };
        
        for (String[] test : tests) {
            NumNode n1 = NumNode.createNode(test[0]);
            OpNode op = OpNode.createNode(test[1]);
            NumNode n2 = NumNode.createNode(test[2]);
            n1.addNext(op);
            op.addNext(n2);
            try {
                NumNode result = op.evaluate();
                assertNotNull(result);
                double expectedResult = Double.parseDouble(test[3]);
                double actualResult = result.getNumValue();
                assertEquals(expectedResult, actualResult, 0.00);
            } catch (RuntimeException e) {
                assertNull(test[3]);
                assertEquals(test[4], e.getMessage());
            }
        }
    }
}
