package Topics;

public class DemoStringBuilder {
    public static void main(String[] args) {
        String s = "This is a demo";
        System.out.println(s);
        s += " of Builder";
        System.out.println(s);
        s = s.substring(0, 18)
          + "String"
          + s.substring(18);
        System.out.println(s);

        StringBuilder sb = new StringBuilder("This is a demo");
        sb.append(" of Builder");
        System.out.println(sb);
        sb.insert(18, "String");
        System.out.println(sb);
    }
}
