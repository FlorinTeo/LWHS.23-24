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
    
    public <T extends Point> T parseT(String s, Class<T> realType) {
        s = s.trim();
        if (realType == Point.class) {
            return realType.cast(Point.parsePoint(s));
        } else {
            throw new RuntimeException("Unsupported type in graph parsing!");
        }
    }

    public <T extends Point> Graph<T> readGraph(String graphFile, Class<T> realType) throws FileNotFoundException {
        Scanner input = getScanner(graphFile);
        Map<String, List<String>> linksMap = new HashMap<String, List<String>>();
        Map<String, T> nodesMap = new HashMap<String, T>();
        while(input.hasNextLine()) {
            String line = input.nextLine();
            String[] tokens = line.split(">");
            if (tokens.length < 1) {
                input.close();
                throw new RuntimeException("Syntax error in parsing graph!");
            }
            String[] data = tokens[0].trim().split("\\s+:\\s+");
            String[] links = tokens.length > 1
                ? tokens[1].trim().split("\\s+")
                : new String[0];
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
                if (toNode != null) {
                    graph.addEdge(fromNode, toNode);
                }
            }
        }

        return graph;
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
