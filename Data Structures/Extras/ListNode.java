package Extras;

public class ListNode {
    private int _data;
    public ListNode _next;
    public ListNode(int data, ListNode next) {
        _data = data;
        _next = next;
    }

    @Override
    public String toString() {
        return "" + _data;
    }
}