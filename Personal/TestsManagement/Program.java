package TestsManagement;

import java.io.IOException;
import java.util.Scanner;

public class Program {

    // Test generator singleton
    private static Generator _generator;

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
                        processResetRoot(true);
                        break;
                    case "refresh-root":
                        processResetRoot(false);
                        break;
                    case "gen-test":
                        processResetTest(parser, true);
                        break;
                    case "refresh-test":
                        processResetTest(parser, false);
                        break;
                    case "gen-variants":
                        processResetVariants(parser, true);
                        break;
                    case "refresh-variants":
                        processResetVariants(parser, false);
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
        System.out.println("____________           ____________");
        System.out.println("            \\ Goodbye /");
    }

    private static void processHelp() {
        System.out.println("? | help:\n  This help.");
        System.out.println("exit | quit:\n  Exits the program.");
        System.out.println("root {path_to_folder}:\n  Targets the test generator to path_to_folder.");
        System.out.println("root?:\n  Prints the current test generator folder.");
        System.out.println("questions?\n  Prints out stats on questions loaded from the root\\.template.");
        System.out.println("gen-root:\n  Generates the .meta and index.html for the all questions in root\\.template.");
        System.out.println("refresh-root:\n  Loads pre-existing generator .meta file and reconstructs the index.html.");
        System.out.println("gen-test {testName} {Questions_csv}:\n  Generates .meta and index.html files for the given questions in root\\test\\...");
        System.out.println("refresh-test {testName}:\n  Loads pre-existing .meta and reconstructs the index.html in given root\\test\\...");
        System.out.println("gen-variants {testName} {nVariants}:\n  Generates .meta and index.html files {nVariants} of {testName}");
        System.out.println("refresh-variants {testName} {Variants_csv}:\n  Loads pre-existing .meta and reconstructs the index.html for the given {Variants_csv}");
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

    private static void processResetRoot(boolean regenMeta) throws IOException {
        _generator.resetRoot(regenMeta);
        System.out.println("DONE");
    }

    private static void processResetTest(Scanner argParser, boolean regenMeta) throws IOException {
        if (!argParser.hasNext()) {
            throw new IllegalArgumentException("Missing or invalid test name!");
        }
        String testName = argParser.next();
        String[] qIDs = {};
        if (argParser.hasNext()) {
            qIDs = argParser.next().split(",");
        }
        _generator.resetTest(testName, qIDs, regenMeta);
        System.out.println("DONE");
    }

    private static void processResetVariants(Scanner argParser, boolean regenMeta) throws IOException {
        if (!argParser.hasNext()) {
            throw new IllegalArgumentException("Missing or invalid test name!");
        }
        String testName = argParser.next();
        String[] vIDs = {};
        if (!argParser.hasNext()) {
            throw new IllegalArgumentException("Missing or invalid variant arguments!");
        }
        if (regenMeta) {
            if (!argParser.hasNextInt()) {
                throw new IllegalArgumentException("Missing or invalid variants count!");
            }
            vIDs = new String[argParser.nextInt()];
            for (int i = 0; i < vIDs.length; i++) {
                vIDs[i] = String.format("v%d", i+1);
            }
        } else {
            vIDs = argParser.next().split(",");
        }
        
        _generator.resetTestVariants(testName, vIDs, regenMeta);
        System.out.println("DONE");
    }
}