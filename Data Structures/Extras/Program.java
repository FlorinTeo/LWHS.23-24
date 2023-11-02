package Extras;

public class Program {
    public static void main(String[] args) {
        LinkedIntList list = new LinkedIntList(1, 2, 3, 4, 5);
        System.out.println(list);
        list.reverse2();
        System.out.println(list);
    }
}
