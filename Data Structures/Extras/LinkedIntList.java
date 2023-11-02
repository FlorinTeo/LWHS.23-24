package Extras;

public class LinkedIntList {
    private ListNode _front;

    public LinkedIntList(int... numbers) {
        ListNode tail = _front;
        for(int n : numbers) {
            if (_front == null) {
                _front = new ListNode(n, null);
                tail = _front;
            } else {
                tail._next = new ListNode(n, null);
                tail = tail._next;
            }
        }
    }

    public void reverse() {
        reverse(_front)._next = null;
    }

    public ListNode reverse(ListNode n) {
        if (n == null || n._next == null) {
            _front = n;
        } else {
            reverse(n._next)._next = n;
        }
        return n;
    }

    public void reverse2() {
        if (_front == null || _front._next == null) {
            return;
        }

        ListNode backN = null;
        ListNode crtN = _front;
        ListNode fwdN = _front._next;
        while(fwdN != null) {
            crtN._next = backN;
            backN = crtN;
            crtN = fwdN;
            fwdN = fwdN._next;
        }

        crtN._next = backN;
        _front = crtN;
    }

    @Override
    public String toString() {
        String output = "";
        for(ListNode n = _front; n != null; n = n._next) {
            output += n.toString() + " ";
        }
        return output;
    }
}
