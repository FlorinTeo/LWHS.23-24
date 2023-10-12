package main;

public class RawNode {
    
    /**
     * Class fields:
     * TODO: Raw content in this node, as a string.
     * TODO: References to the previous and next node, or null respectively if nodes do not exist. 
     */
    
    /**
     * Class constructor. Builds a new generic raw content node.
     * @param rawContent - the raw content stored in this node.
     */
    protected RawNode(String rawContent) {
        // TODO: configures / initializes class fields.
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
        // TODO: returns the raw content
        return "";
    }
    
    /**
     * Gets the previous node.
     * @return the reference to the previous node, or null if none exists.
     */
    public RawNode getPrev() {
        // TODO: returns the reference to the previous node
        return null;
    }
    
    /**
     * Gets the next node.
     * @return the reference to the next node, or null if none exists.
     */
    public RawNode getNext() {
        // TODO: returns the reference to the next node
        return null;
    }
    
    /**
     * Adds another node right after this node.
     * @param other - the node to be added.
     * @return the node that's been added.
     */
    public RawNode addNext(RawNode other) {
        // TODO: code inserting the other node after this node
        return null;
    }
    
    /**
     * Adds another node to the tail of the list.
     * @param other - the node to be added.
     * @return the new tail of the list.
     */
    public RawNode addTail(RawNode other) {
        // TODO: code inserting the other node at the very end of
        // TODO: the list where this node is part of.
        return null;
    }
}
