package IntTree;

public class IntTreeNode {
    private int _data;
    public IntTreeNode _left;
    public IntTreeNode _right;
    
    public IntTreeNode(int data, IntTreeNode left, IntTreeNode right) {
        _data = data;
        _left = left;
        _right = right;
    }
    
    public int getData() {
        return _data;
    }
    
    public int sumAll() {
        int sum = _data;
        if (_left != null) {
            sum += _left.sumAll();
        } if (_right != null) {
            sum += _right.sumAll();
        }
        return sum;
    }
    
    // Region: other fields and methods not shown
    public void insert(int number) {
        if (_data > number) {
            if (_left == null) {
                _left = new IntTreeNode(number, null, null);
            } else {
                _left.insert(number);
            }
        } else {
            if (_right == null) {
                _right = new IntTreeNode(number, null, null);
            } else {
                _right.insert(number);
            }
        }
    }
    
    public boolean contains(int number) {
        IntTree._count++;
        if (_data == number) {
            return true;
        }
        return (_left != null && _left.contains(number))
            || (_right != null && _right.contains(number));
    }

    public String toString(String indent) {
        String output = indent + _data;
        if (_left != null) {
            output += "\n" + _left.toString(indent + "  ");
        }
        
        if (_right != null) {
            output += "\n" + _right.toString(indent + "  ");
        }
        
        return output;
    }
    // EndRegion: other fields and methods not shown
}
