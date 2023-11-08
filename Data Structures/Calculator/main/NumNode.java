package Calculator.main;

public class NumNode extends RawNode {
    
    private double _numContent;

    /**
     * Class constructor. Builds a new numerical node.
     * @param rawContent - the raw content stored in this node.
     */
    protected NumNode(String numContent) {
        super(numContent);
    }
    
    /**
     * NumNode factory method, returning a new numerical node 
     * for the given raw content or null if the node could not be created.
     * @param rawContent - the raw content stored in this node.
     * @return the new numerical node.
     */
    public static NumNode createNode(String rawContent) {
        NumNode numNode;
        try {
            double numContent = Double.parseDouble(rawContent);
            numNode = new NumNode(rawContent);
            numNode._numContent = numContent;
        } catch (Exception e) {
            numNode = null;
        }
        
        return numNode;
    }
    
    /**
     * Gets the node's numerical value.
     * @return
     */
    public double getNumValue() {
        return _numContent;
    }
}
