package main;

import java.text.DecimalFormat;

public class OpNode extends RawNode {
    
    public enum OpCode {
        UNKNOWN,
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION,
        POWER,
        MODULO
    }
    
    private OpCode _opCode;
    
    /**
     * Class constructor. Builds a new operator node.
     * @param rawContent - the raw content stored in this node.
     */
    protected OpNode(String rawContent) {
        super(rawContent);
        _opCode = OpCode.UNKNOWN;
    }
    
    /**
     * OpNode factory method, returning a new operator node 
     * for the given raw content or null if the node could not be created.
     * @param rawContent - the raw content stored in this node.
     * @return the new operator node.
     */
    public static OpNode createNode(String rawContent) {
        OpNode opNode = new OpNode(rawContent);
        switch(rawContent) {
        case "+":
            opNode._opCode = OpCode.ADDITION;
            break;
        case "-":
            opNode._opCode = OpCode.SUBTRACTION;
            break;
        case "*":
            opNode._opCode = OpCode.MULTIPLICATION;
            break;
        case "/":
            opNode._opCode = OpCode.DIVISION;
            break;
        case "^":
            opNode._opCode = OpCode.POWER;
            break;
        case "%":
            opNode._opCode = OpCode.MODULO;
            break;
        default:
            opNode = null;
        }
        
        return opNode;
    }
    
    /**
     * Gets the node's operator code.
     * @return - the operator code.
     */
    public OpCode getOpCode() {
        return _opCode;
    }
    
    /**
     * Evaluates this operator node, returning the result of the evaluation
     * as a new numerical node.
     * @return the result as a new numerical node.
     */
    public NumNode evaluate() {
        if (_prev == null || _next == null) {
            throw new RuntimeException("Missing operands for operator: " + _rawContent);
        }
        
        if (!(_prev instanceof NumNode) || !(_next instanceof NumNode)) {
            throw new RuntimeException("Wrong operands for operator: " + _rawContent);
        }
        
        NumNode num1 = (NumNode)_prev;
        NumNode num2 = (NumNode)_next;
        
        return evalExprNodes(num1, num2);
    }
    
    /**
     * Calculates the result of this operator node given the two operand numerical nodes.
     * @param num1 - first operand.
     * @param num2 - second operand.
     * @return result of the operation as a new numerical node.
     */
    private NumNode evalExprNodes(NumNode num1, NumNode num2) {
        double result;
        switch(_opCode) {
        case POWER:
            result = Math.pow(num1.getNumValue(), num2.getNumValue());
            break;
        case MULTIPLICATION:
            result = num1.getNumValue() * num2.getNumValue();
            break;
        case DIVISION:
            if (num2.getNumValue() == 0) {
                throw new RuntimeException("Division by zero");
            }
            result = num1.getNumValue() / num2.getNumValue();
            break;
        case MODULO:
            result = num1.getNumValue() % num2.getNumValue();
            break;
        case ADDITION:
            result = num1.getNumValue() + num2.getNumValue();
            break;
        case SUBTRACTION:
            result = num1.getNumValue() - num2.getNumValue();
            break;
        default:
            throw new RuntimeException("Invalid operator node " + _opCode);
        }
        
        DecimalFormat df = new DecimalFormat("#0.######");
        String resultString = df.format(result);
        return NumNode.createNode(resultString);
    }

}
