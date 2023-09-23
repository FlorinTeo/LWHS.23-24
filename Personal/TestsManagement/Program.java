package TestsManagement;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Program {

    // Test generator singleton
    private static Generator _generator;

    //"O:/Florin.Teo@Inproted/TEALS_LWHS/LWHS CS.23-24 - Documents/General/AP CS-A/ap/unit1"
    //"D:/ODrive/TEALS_LWHS/LWHS CS.23-24 - Documents/General/AP CS-A/ap/unit1"
    private static final String _root = "D:/ODrive/TEALS_LWHS/LWHS CS.23-24 - Documents/General/AP CS-A/ap/unit1";

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
            
            ArrayList<String> sAList = new ArrayList<String>(Arrays.asList(aList));
            if (shuffle) {
                sAList = shuffle(sAList);
            }

            for(String a: sAList) {
                bw.write(a);
            }
            bw.write("\n");
        }
        bw.close();
    }

    private static final String _choiceLine = "    <tr><td class=\"refText\">#N#.</td><td style=\"width: 200px; text-align: right;\">(A) (B) (C) (D) (E)</td></tr>";
    private static void genTestIndex(String testName) throws IOException {
        List<String> metaList = loadMeta(testName);
        String tHeader = loadTemplate(".header.html");
        String tQuestion = loadTemplate(".question.html");

        Path testIndexP = Paths.get(_root, testName, "index.html");
        BufferedWriter bw = Files.newBufferedWriter(testIndexP);
        tHeader = tHeader.replaceAll("#VER#", testName).replace("#NQ#", ""+metaList.size());
        for (int i = 0; i < metaList.size(); i++) {
            String choice = _choiceLine.replace("#N#", ""+(i+1));
            tHeader = tHeader.replace("#ANS#", choice + "\n    #ANS#");
        }
        tHeader = tHeader.replace("    #ANS#", "");
        bw.write(tHeader);

        for (int i = 0; i < metaList.size(); i++) {
            String metaLine = metaList.get(i);
            String[] metaParts = metaLine.split(" ");
            String hQuestion = tQuestion.replaceFirst("#N#", "" + (i+1));
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
        System.out.println("____/Tests Management Console \\____");
        Scanner input = new Scanner(System.in);
        do {
            System.out.print("Command > ");
            Scanner parser = new Scanner(input.nextLine().trim());
            String cmd = parser.next().toLowerCase();
            if (cmd.isEmpty()) {
                continue;
            }
            if (cmd.equals("exit") || cmd.equals("quit")) {
                break;
            }
            try {
                switch(cmd.toLowerCase()) {
                    case "help":
                    case "?":
                        processHelp();
                        break;
                    case "legacy":
                        processLegacy();
                        break;
                    case "root":
                        processSetRoot(parser.nextLine());
                        break;
                    case "root?":
                        processGetRoot();
                        break;
                    case "questions?":
                        processGetQuestions();
                        break;
                    case "gen-root":
                        processGenRoot();
                        break;
                    default:
                        throw new RuntimeException("Unrecognized command!");
                }
            } catch (Exception e) {
                System.out.printf("##ERR##: %s\n", e.getMessage());
            }
            parser.close();
        } while(true);
        input.close();
        System.out.println("____           ____");
        System.out.println("    \\ Goodbye /");
    }

    private static void processHelp() {
        System.out.println("? | help:\n  This help.");
        System.out.println("exit | quit:\n  Exits the program.");
        System.out.println("legacy:\n  Legacy command doing it all.");
        System.out.println("root {path_to_folder}:\n  Targets the test generator to path_to_folder.");
        System.out.println("root?:\n  Prints the current test generator folder.");
        System.out.println("questions?\n  Prints out stats on questions loaded from the root\\.template");
        System.out.println("gen-root:\n  Generates the index.html for the all questions in root\\.template");
    }

    private static void processLegacy() throws IOException {
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

    private static void processSetRoot(String root) throws IOException {
        root = root.trim();
        if (root.startsWith("\"")) {
            root = root.substring(1, root.length()-1);
        }
        _generator = new Generator(root);
        System.out.println("DONE");
    }

    private static void processGetRoot() {
        if (_generator == null) {
            System.out.println("(null)");
        } else {
            System.out.println(_generator.getRoot());
        }
    }

    private static void processGetQuestions() {
        if (_generator == null) {
            System.out.printf("No questions loaded from root.\n");
        } else {
            System.out.printf("Questions loaded from root:\n%s\n", _generator.getQuestions());
        }
    }

    private static void processGenRoot() throws IOException {
        _generator.genRootIndex();
        System.out.println("DONE");
    }
}