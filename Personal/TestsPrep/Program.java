package TestsPrep;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Program {

    private static final String _root = "O:/Florin.Teo@Inproted/TEALS_LWHS/LWHS CS.23-24 - Documents/General/AP CS-A/ap/unit1";

    private static Path _rootP = Paths.get(_root);
    private static Path _templateP = Paths.get(_rootP.toAbsolutePath() + "/.template");
    private static Path _headerP = Paths.get(_templateP.toAbsolutePath() + "/.header.html");
    private static Path _questionP = Paths.get(_templateP.toAbsolutePath() + "/.question.html");


    private static ArrayList<String> getQList() throws IOException {
        ArrayList<String> qList = new ArrayList<String>();
        for (Path qDir : Files.walk(_templateP, 1).toArray(Path[]::new)) {
            if (Files.isDirectory(qDir) && !qDir.getFileName().toString().startsWith(".")) {
                String qName = qDir.getFileName().toString();
                qList.add(qName);
            }
        }
        return qList;
    }

    private static ArrayList<String> shuffle(ArrayList<String> list) {
        ArrayList<String> sList = new ArrayList<String>();
        while(!list.isEmpty()) {
            String s = list.remove((int)(Math.random() * list.size()));
            sList.add(s);
        }
        return sList;
    }

    private static void genTest(String name, ArrayList<String> qList) throws IOException {
        Path testP = Paths.get(_rootP.toAbsolutePath() + "/" + name);
        Files.createDirectories(testP);
        genTestMeta(testP, qList);
    }

    private static void genTestMeta(Path testP, ArrayList<String> qList) throws IOException {
        Path testMetaP = Paths.get(testP.toAbsolutePath() + "/.meta");
        BufferedWriter bw = Files.newBufferedWriter(testMetaP);
        for(String q : qList) {
            bw.write(q + "\n");
        }
        bw.close();
    }

    public static void main(String[] args) throws IOException {
        ArrayList<String> qList = getQList();
        ArrayList<String> sqList = shuffle(qList);
        genTest("v1", sqList);
    }
}