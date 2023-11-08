package main;

import java.util.Arrays;

public class NumCalc {
    
    private RawNode _head = null;
    private RawNode _trace = null;
    
    /**
     * Operator precedence table:
     * @see <a href="https://refreshjava.com/java/operator-precedence">operator-precedence</a>
     */
    private OpNode.OpCode[][] _opPrecedence = {
            { OpNode.OpCode.POWER },
            { OpNode.OpCode.MULTIPLICATION, OpNode.OpCode.DIVISION, OpNode.OpCode.MODULO },
            { OpNode.OpCode.ADDITION, OpNode.OpCode.SUBTRACTION }
    };
    
    /**
     * Class constructor.
     */
    public NumCalc() {
        // initialize the calculator
    }
    
    /**
     * Takes the expression strings and builds the internal
     * double-linked list storing the expression nodes.
     * @param exprStrings - components of the expression string 
     */
    private void buildExprList(String[] exprStrings) {
        for(String exprString : exprStrings) {
            RawNode newNode = NumNode.createNode(exprString);
            if (newNode == null) {
                newNode = OpNode.createNode(exprString);
            }
            
            if (newNode != null) {
                if (_head == null) {
                    _head = newNode;
                } else {
                    _head.addTail(newNode);
                }
            } else {
                throw new RuntimeException("Unrecognized token: " + exprString);
            }
        }
    }
    
    /**
     * Evaluates the expression stored in the double linked list
     * and returns the final result. 
     * @return - evaluation result.
     */
    private String evalExprList() {
        for (OpNode.OpCode[] ops: _opPrecedence) {
            for(RawNode node = _head; node != null; node = node._next) {
                // skip nodes which are not operators
                if (!(node instanceof OpNode)) {
                    continue;
                }
                
                OpNode opNode = (OpNode)node;
                
                // skip operator nodes which are not in the current precedence row
                if (!Arrays.asList(ops).contains(opNode.getOpCode())) {
                    continue;
                }
                
                // evaluate the operator
                NumNode resultNode = opNode.evaluate();
                
                // since evaluation succeeded, it means operator
                // has valid _prev and _next numerical operand nodes.
                
                // set the _prev, _next link of the new result node
                resultNode._prev = opNode._prev._prev;
                resultNode._next = opNode._next._next;
                
                // link the new result node into the list
                if (resultNode._prev != null) {
                    resultNode._prev._next = resultNode;
                } else {
                    _head = resultNode;
                }
                if (resultNode._next != null) {
                    resultNode._next._prev = resultNode;
                }
                
                // since an operator has just been evaluated and the
                // expression list changed, add a trace frame.
                addTraceFrame();
            }
        }
        
        return _head.getRawContent();
    }
    
    /**
     * Constructs a new trace frame string from the expression list
     * and adds it to the trace list.
     */
    private void addTraceFrame() {
        String traceFrame = "";
        for(RawNode node = _head; node != null; node = node.getNext()) {
            traceFrame += node.getRawContent() + " ";
        }
        
        RawNode newTraceNode = RawNode.createNode(traceFrame.trim());
        if (_trace == null) {
            _trace = newTraceNode;
        } else {
            _trace.addTail(newTraceNode);
        }
    }
    
    /**
     * Takes a raw expression and returns the final evaluation result.
     * @param expression as entered by the user.
     * @return - evaluation result.
     */
    public String evaluate(String expression) {
        _head = null;
        _trace = null;
        String[] exprParts = expression.split(" ");
        buildExprList(exprParts);
        return evalExprList();
    }
    
    /**
     * Gives the evaluation trace of the last evaluated expression,
     * as a multi-line string.
     */
    @Override
    public String toString() {
        String output = "";
        for(RawNode node = _trace; node != null; node = node.getNext()) {
            output += node.getRawContent() + "\n";
        }
        
        return output;
    }
}
