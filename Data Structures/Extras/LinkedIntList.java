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

    public int size() {
        int n = 0;
        for (ListNode node = _front; node != null; node = node._next) {
            n++;
        }
        return n;
    }

    public void swap(int i) {
        int length = size();
        if (i < 0 || i > length-2) {
            throw new IndexOutOfBoundsException();
        }
        ListNode newFront = new ListNode(0, null);
        ListNode tail = newFront;
        ListNode cur = _front;
        for (int j = 0; j < length; j++) {
            if (j == i) {
                ListNode tmp = cur;
                cur = cur._next._next;
                j++;
                tail = tail._next = tmp._next;
                tail = tail._next = tmp;                

            } else {
                ListNode tmp = cur;
                cur = cur._next;
                tail = tail._next = tmp;
            }
        }
        tail._next = null;
        _front = newFront._next;
    }

    public int mystery(int k) {
        ListNode n1 = _front;
        ListNode n2 = _front;
        while(n1 != null) {
            n1 = n1._next;
            if (k > 0) {
                k--;
            } else {
                n2 = n2._next;
            }
        }
        if (k != 0 || n2 == null) {
            throw new IndexOutOfBoundsException();
        }
        return n2.getData();
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

    public void frontToTail() {
        if (_front != null && _front._next != null) {
            ListNode crt = _front;
            while(crt._next != null) {
                crt = crt._next;
            }
            crt._next = _front;
            _front = _front._next;
            crt._next._next = null;
        }
    }

    public void tailToFront() {
        if (_front != null && _front._next != null) {
            ListNode prev = null;
            ListNode crt = _front;
            while(crt._next != null) {
                prev = crt;
                crt = crt._next;
            }
            crt._next = _front;
            _front = crt;
            prev._next = null;
        }
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
