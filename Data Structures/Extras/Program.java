package Extras;

public class Program {
    public static void main(String[] args) {
        LinkedIntList list = new LinkedIntList(1, 2, 3, 4, 5);
        System.out.println(list);
        //list.reverse2();
        list.swap(1);
        System.out.println(list);
        list.swap(3);
        System.out.println(list);
        list.swap(0);
        System.out.println(list);
        list.frontToTail();
        System.out.println(list);
        list.tailToFront();
        System.out.println(list);
        list = new LinkedIntList(33);
        list.frontToTail();
        System.out.println(list);
        list.tailToFront();
        System.out.println(list);
    }
}
