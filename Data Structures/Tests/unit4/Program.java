package Tests.unit4;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Program {
    public static boolean sameMaps(Map<Integer, String> m1, Map<Integer, String> m2) {
        for(Integer key : m1.keySet()) {
            String v1 = m1.get(key);
            String v2 = m2.get(key);
            if ((v1 == null && v2 != null) || (v1 != null && !v1.equals(v2))) {
                return false;
            }
        }
        return (m1.size() == m2.size());
    }

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

        // frq question 5
        Map<Integer, String> map1 = new HashMap<Integer, String>();
        map1.put(1, "abc");
        map1.put(2, "def");
        map1.put(3, null);
        Map<Integer, String> map2 = new HashMap<Integer, String>();
        map2.put(1, "abc");
        map2.put(2, "def");
        map2.put(4, "xyz");
        System.out.println(sameMaps(map1, map2));       
    }
}