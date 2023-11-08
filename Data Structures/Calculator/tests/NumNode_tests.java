package Calculator.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import Calculator.main.NumNode;

public class NumNode_tests {

    @Test
    public void test_createNode() {
        NumNode num = NumNode.createNode("123");
        assertNotNull(num);
        assertEquals(123, num.getNumValue(), 0.00);
        
        num = NumNode.createNode("");
        assertNull(num);
        
        num = NumNode.createNode("-12.34");
        assertNotNull(num);
        assertEquals(-12.34, num.getNumValue(), 0.00);
        
        num = NumNode.createNode("abc");
        assertNull(num);
    }

}
