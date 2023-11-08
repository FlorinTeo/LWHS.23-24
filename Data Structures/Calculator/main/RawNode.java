package Calculator.main;

public class RawNode {
    
    protected String _rawContent;
    protected RawNode _prev;
    protected RawNode _next;
    
    /**
     * Class constructor. Builds a new generic raw content node.
     * @param rawContent - the raw content stored in this node.
     */
    protected RawNode(String rawContent) {
        _rawContent = rawContent;
        _prev = null;
        _next = null;
    }
    
    /**
     * Node factory method, returning a new node for the given raw content
     * or null if the node could not be created.
     * @param rawContent - the row content stored in this node.
     * @return the new node.
     */
    public static RawNode createNode(String rawContent) {
        return new RawNode(rawContent);
    }
    
    /**
     * Gets the node's raw content.
     * @return the raw content.
     */
    public String getRawContent() {
        return _rawContent;
    }
    
    /**
     * Gets the next node.
     * @return the reference to the next node, or null if none exists.
     */
    public RawNode getNext() {
        return _next;
    }
    
    /**
     * Gets the previous node.
     * @return the reference to the previous node, or null if none exists.
     */
    public RawNode getPrev() {
        return _prev;
    }
    
    /**
     * Adds another node right after this node.
     * @param other - the node to be added.
     * @return the node that's been added.
     */
    public RawNode addNext(RawNode other) {
        other._next = _next;
        other._prev = this;
        if (_next != null) {
            _next._prev = other;
        }
        _next = other;
        return other;
    }
    
    /**
     * Adds another node to the tail of the list.
     * @param other - the node to be added.
     * @return the new tail of the list.
     */
    public RawNode addTail(RawNode other) {
        if (_next == null) {
            return addNext(other);
        } else {
            return _next.addTail(other);
        }
    }
}
