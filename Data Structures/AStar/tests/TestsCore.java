package AStar.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import AStar.main.Point;
import AStar.main.Graph;

public class TestsCore {
    
    private Scanner getScanner(String graphFile) throws FileNotFoundException {
        URL url = this.getClass().getResource(graphFile);
        File file = new File(url.getFile());
        String filePath = file.getAbsolutePath();
        try {
            filePath = java.net.URLDecoder.decode(filePath, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
        }
        System.out.println(file.getAbsolutePath());
        return new Scanner(new File(filePath));
    }
    
    public <T extends Comparable<T>> T parseT(String s, Class<T> realType) {
        if (realType == Integer.class) {
            return realType.cast(Integer.parseInt(s.trim()));
        } else if (realType == String.class) {
            return realType.cast(s);
        } else if (realType == Double.class) {
            return realType.cast(Double.parseDouble(s.trim()));
        } else if (realType == Character.class) {
            if (s.length() != 1) {
                throw new RuntimeException("Invalid format in graph parsing!");
            }
            return realType.cast(s.charAt(0));
        } else if (realType == Point.class) {
            return realType.cast(Point.parsePoint(s));
        } else {
            throw new RuntimeException("Unsupported type in graph parsing!");
        }
    }

    public <T extends Comparable<T>> Graph<T> readGraph(String graphFile, Class<T> realType) throws FileNotFoundException {
        Scanner input = getScanner(graphFile);
        Map<String, List<String>> linksMap = new HashMap<String, List<String>>();
        Map<String, T> nodesMap = new HashMap<String, T>();
        while(input.hasNextLine()) {
            String line = input.nextLine();
            String[] tokens = line.split(">");
            if (tokens.length != 2) {
                input.close();
                throw new RuntimeException("Syntax error in parsing graph!");
            }
            String[] data = tokens[0].trim().split("\\s+:\\s+");
            String[] links = tokens[1].trim().split("\\s+");
            T n = parseT(tokens[0], realType);
            linksMap.put(data[0], Arrays.asList(links));
            nodesMap.put(data[0], n);
        }
        input.close();
        
        Graph<T> graph = new Graph<T>();
        for(T n : nodesMap.values()) {
            graph.addNode(n);
        }
        
        for(Map.Entry<String, T> kvp : nodesMap.entrySet()) {
            T fromNode = kvp.getValue();
            for(String v : linksMap.get(kvp.getKey())) {
                T toNode= nodesMap.get(v);
                graph.addEdge(fromNode, toNode);
            }
        }

        return graph;
    }

    public Graph<String> readGraph(String graphFile) throws FileNotFoundException {
        return readGraph(graphFile, String.class);
    }
    
    public void assertSameGraph(String graphFile, Graph<?> g) throws FileNotFoundException {
        Scanner parser = getScanner(graphFile);
        Set<String> expected = new TreeSet<String>();
        Set<String> actual = new TreeSet<String>();
        while(parser.hasNextLine()) {
            String line = parser.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            expected.add(line);
        }
        for(String line : g.toString().split("\n")) {
            actual.add(line);
        }
        parser.close();
        assertEquals(expected.size(), actual.size());
        Iterator<String> iExpected = expected.iterator();
        Iterator<String> iActual = actual.iterator();
        while(iExpected.hasNext()) {
            assertSameNode(iExpected.next(), iActual.next());
        }
    }

    public void assertSameNode(String expectedNode, String actualNode) {
        Set<String> expectedTokens = new TreeSet<String>(Arrays.asList(expectedNode.split("\\s+")));
        Set<String> actualTokens = new TreeSet<String>(Arrays.asList(actualNode.split("\\s+")));
        assertTrue(expectedTokens.equals(actualTokens));
    }
}
