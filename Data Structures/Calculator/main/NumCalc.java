package main;

public class NumCalc {
    
    /**
     * Class fields:
     * TODO: Operators precedence table, as a double array of operator codes (OpNode.OpCode).
     * TODO: Expression list: a double-linked list containing the numbers and operators to be evaluated.
     * TODO: Tracing list: a double-linked list containing raw nodes, each holding a trace frame as a string. 
     */
    
    /**
     * Class constructor.
     */
    public NumCalc() {
        // TODO: initialize the calculator
    }
    
    /**
     * Takes the expression strings and builds the internal
     * double-linked list storing the expression nodes.
     * @param exprStrings - components of the expression string 
     */
    private void buildExprList(String[] exprStrings) {
        // TODO: For each string in the expression, try creating either
        // TODO: a numeric node or an operator node.
        // TODO: Whichever succeeds first, is added to the tail of the expression list.
        // TODO: If neither nodes could be created, thrown a runtime exception.
    }
    
    /**
     * Evaluates the expression stored in the double linked list
     * and returns the final result. 
     * @return - evaluation result.
     */
    private String evalExprList() {
        // TODO: For each row of operators in the precedence table,
        // TODO: Scan the expression lists and check each operator node.
        // TODO: If the operator code is found in the row of operators,
        // TODO: evaluate it, and replace the operator node and its operands
        // TODO: with the node for the result. After each such reduction of the list
        // TODO: add the entire expression list as a frame into the trace frames list.
        // TODO: When all loops are done, expression list should contain only one numerical node,
        // TODO: which is the final result. Method returns the numerical value, as a string.
        return "";
    }
    
    /**
     * Constructs a new trace frame string from the expression list
     * and adds it to the trace list.
     */
    private void addTraceFrame() {
        // TODO: Builds a trace frame from the expression list:
        // TODO: Goes through the expression list nodes and adds their raw content
        // TODO: to a string accumulator, separated by one space character.
        // TODO: The resulting string is added as a new raw node to the tail of the tracing list. 
    }
    
    /**
     * Takes a raw expression and returns the final evaluation result.
     * @param expression as entered by the user.
     * @return - evaluation result.
     */
    public String evaluate(String expression) {
        // TODO: Resets the expression list and the tracing list,
        // TODO: Splits the expression string around the " " character, into its individual parts
        // TODO: Builds the expression list,
        // TODO: Evaluates the expression and returns the result of the evaluation.
        return "";
    }
    
    /**
     * Gives the evaluation trace of the last evaluated expression,
     * as a multi-line string.
     */
    @Override
    public String toString() {
        // TODO: Goes through the tracing list nodes adding the node raw content to
        // TODO: an accumulator string, separated by "\n" (each trace frame on a new line)
        // TODO: then returns the accumulated result.
        return "";
    }
}
