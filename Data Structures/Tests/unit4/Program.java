package Tests.unit4;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Program {

    public static Map<String, Double> combine(Map<String, String> m1,
            Map<String, Double> m2) {
        Map<String, Double> map = new TreeMap<String, Double>();
        for (Map.Entry<String, String> kv : m1.entrySet()) {
            map.put(kv.getKey(), m2.get(kv.getValue()));
        }

        return map;
    }

    public static void main(String[] args) {
        Map<String, String> firstMap = new HashMap<String, String>();
        firstMap.put("zVal", "pi");
        firstMap.put("yVal", "e");
        firstMap.put("xVal", "g");
        System.out.println(firstMap);

        Map<String, Double> secondMap = new HashMap<String, Double>();
        secondMap.put("g", 9.81);
        secondMap.put("pi", 3.14);
        secondMap.put("e", 2.71);
        System.out.println(secondMap);

        Map<String, Double> result = combine(firstMap, secondMap);
        System.out.println(result.get("yVal"));
        System.out.println(result);

        Set<String> keys = result.keySet();
        System.out.println(keys);
    }
}