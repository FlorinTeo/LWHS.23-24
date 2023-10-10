package main;

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
    
    /**
     * Class fields:
     * TODO: Operator code for this node
     */
    private OpCode _operator = OpCode.UNKNOWN;
    
    /**
     * Class constructor. Builds a new operator node.
     * @param rawContent - the raw content stored in this node.
     */
    protected OpNode(String rawContent) {
        super(rawContent);
        // TODO: UNKNOWN initial value for this node's operator code
        // TODO: since the raw content is not parsed.

        // if rawContent is something that's not recognized (i.e "~")
        // then we'll ... throw an exception!
    }
    
    /**
     * OpNode factory method, returning a new operator node 
     * for the given raw content or null if the node could not be created.
     * @param rawContent - the raw content stored in this node.
     * @return the new operator node.
     */
    public static OpNode createNode(String rawContent) {
        // TODO: Tries to parse the raw content into a OpCode value
        // TODO: if successful, creates an OpNode for it and save the operator code value within,
        // TODO: otherwise returns null 
        return null;
    }
    
    /**
     * Gets the node's operator code.
     * @return - the operator code.
     */
    public OpCode getOpCode() {
        // TODO: returns the operator code (OpCode)
        return _operator;
    }
    
    /**
     * Evaluates this operator node, returning the result of the evaluation
     * as a new numerical node.
     * @return the result as a new numerical node.
     */
    public NumNode evaluate() {
        // TODO: precondition for evaluation is that this (operator) node is surrounded
        // TODO: by numerical nodes. Check and throw runtime exceptions if these are not true.
        // TODO: Otherwise get the two numerical (operand) nodes, execute the operator and
        // TODO: return the result as a new numerical node.
        return null;
    }

}
