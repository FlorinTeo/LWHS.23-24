package TestsPrep;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Program {

    //"O:/Florin.Teo@Inproted/TEALS_LWHS/LWHS CS.23-24 - Documents/General/AP CS-A/ap/unit1"
    //"D:/ODrive/TEALS_LWHS/LWHS CS.23-24 - Documents/General/AP CS-A/ap/unit1"
    private static final String _root = "O:/Florin.Teo@Inproted/TEALS_LWHS/LWHS CS.23-24 - Documents/General/AP CS-A/ap/unit1";

    private static Path _rootP = Paths.get(_root);
    private static Path _templateP = Paths.get(_rootP.toAbsolutePath().toString(), ".template");

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

    private static void genTestMeta(String testName, boolean shuffle) throws IOException {
        List<String> qList = shuffle ? shuffle(getQList()) : getQList();
        Path testP = Paths.get(_root , testName);
        Files.createDirectories(testP);
        Path testMetaP = Paths.get(_root, testName, ".meta");
        BufferedWriter bw = Files.newBufferedWriter(testMetaP);
        String[] aList = {"a", "b", "c", "d", "e"};
        for(String q : qList) {
            bw.write(q+" ");
            ArrayList<String> sAList = shuffle(new ArrayList<String>(Arrays.asList(aList)));
            for(String a: sAList) {
                bw.write(a);
            }
            bw.write("\n");
        }
        bw.close();
    }

    private static void genTestIndex(String testName) throws IOException {
        List<String> metaList = loadMeta(testName);
        String tHeader = loadTemplate(".header.html");
        String tQuestion = loadTemplate(".question.html");

        Path testIndexP = Paths.get(_root, testName, "index.html");
        BufferedWriter bw = Files.newBufferedWriter(testIndexP);
        tHeader = tHeader.replace("#VER#", testName).replace("#NQ#", ""+metaList.size());
        bw.write(tHeader);
        int nQ = 1;
        for (String metaLine : metaList) {
            String[] metaParts = metaLine.split(" ");
            String hQuestion = tQuestion.replaceFirst("#N#", "" + nQ++);
            hQuestion = hQuestion.replaceAll("Q#", metaParts[0]);
            for(char c : metaParts[1].toCharArray()) {
                hQuestion = hQuestion.replaceFirst(metaParts[0]+"#", metaParts[0] + c);
            }
            bw.write(hQuestion);
            bw.newLine();
        }
        bw.close();
    }

    private static List<String> loadMeta(String testName) throws IOException {
        Path testMetaP = Paths.get(_root, testName, ".meta");
        return Files.readAllLines(testMetaP);
    }

    private static String loadTemplate(String templateFile) throws IOException {
        Path fileP = Paths.get(_templateP.toAbsolutePath().toString(), templateFile);
        List<String> lines = Files.readAllLines(fileP);
        return String.join("\n", lines);
    }

    public static void main(String[] args) throws IOException {
        genTestMeta("ref", false);
        genTestIndex("ref");

        genTestMeta("v1", true);
        genTestIndex("v1");

        genTestMeta("v2", true);
        genTestIndex("v2");

        genTestMeta("v3", true);
        genTestIndex("v3");

        genTestMeta("v4", true);
        genTestIndex("v4");
    }
}