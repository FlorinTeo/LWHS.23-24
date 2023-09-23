package TestsManagement;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Generator {
    private Path _pRoot;
    private List<Question> _qList;
    private String _hTemplateStyle;
    private String _hTemplateBooklet;
    private String _hTemplateSection1Header;
    private String _hTemplateSection1Question;

    /**
     * Loads the list of questions found in the ".template" sub-folder.
     * @throws IOException
     */
    private void loadQList(Path pTemplate) throws IOException {
        _qList = new LinkedList<Question>();
        for (Path qDir : Files.walk(pTemplate, 1).toArray(Path[]::new)) {
            if (Files.isDirectory(qDir) && !qDir.getFileName().toString().startsWith(".")) {
                Question question = new Question(qDir);
                _qList.add(question);
            }
        }
    }

    /**
     * Constructs a TestsGenerator object targetting the given {root} folder. The root is
     * expected to contain a ".template" subfolder.
     * @param root - working folder for this generator.
     * @throws IOException
     */
    public Generator(String root) throws IOException {
        _pRoot = Paths.get(root);
        Path pTemplate = Paths.get(root, ".template");
        Path phStyle = Paths.get(root, ".template", ".style.html");
        Path phBooklet = Paths.get(root, ".template", ".booklet.html");
        Path phSection1H = Paths.get(root, ".template", ".section1H.html");
        Path phSection1Q = Paths.get(root, ".template", ".section1Q.html");

        if (!Files.isDirectory(_pRoot) || !Files.isDirectory(pTemplate)) {
            throw new IOException("Template folder is missing or invalid!");
        }

        loadQList(pTemplate);
        _hTemplateStyle = String.join("\n", Files.readAllLines(phStyle));
        _hTemplateBooklet = String.join("\n", Files.readAllLines(phBooklet));
        _hTemplateSection1Header = String.join("\n", Files.readAllLines(phSection1H));
        _hTemplateSection1Question = String.join("\n", Files.readAllLines(phSection1Q));
    }

    /**
     * Gets the absolute path to root.
     * @return absolute path to root.
     */
    public String getRoot() {
        return _pRoot.toAbsolutePath().toString();
    }

    /**
     * Gets statistics on the set of questions loaded in this generator.
     * @return printable string with questions stats.
     */
    public String getQuestions() {
        String output = "";
        output += String.format("Count: %d\n", _qList.size());
        output += _qList.toString();
        return output;
    }

    /**
     * Generates the .meta file in the given path. The file will only contain the
     * given list of questions, randomized or not, with their original name or an ordinal number.
    // //  * @param pMeta - Path to the .meta file.
     * @param questions - List of questions to be included.
     * @param randomize - True if list should be randomized, false otherwise.
     * @param preserveName - True if questions should preserve their names, false if an ordinal should be used instead.
     * @return The list of lines in the generated .meta file.
     * @throws IOException
     */
    private List<String> genMeta(Path pMeta, List<Question> qList, boolean randomize, boolean preserveName ) throws IOException {
        List<String> metaLines = new LinkedList<String>();
        BufferedWriter bw = Files.newBufferedWriter(pMeta);
        for(Question question : qList) {
            String metaLine = question.getMetaLine();
            metaLines.add(metaLine);
            bw.write(metaLine);
            bw.newLine();
        }
        bw.close();
        return metaLines;
    }

    /**
     * Writes the styling information in the index.html file.
     * @param bwIndex - index writer.
     * @throws IOException
     */
    private void genIndexStyle(BufferedWriter bwIndex) throws IOException {
        bwIndex.write(_hTemplateStyle);
        bwIndex.newLine();
    }
    
    /**
     * Generates the index.html file in the given path. The questions and their answers are read from the metaLines and
     * are expected to exist in the .template folder.
     * @param pIndex - Path to the index.html file.
     * @param metaLines - Meta lines indicating what questions and what answers should be indexed.
     * @throws IOException
     */
    private void genIndexSection1(BufferedWriter bwIndex, List<String> metaLines, boolean isRoot) throws IOException {
        String hSection1H = _hTemplateSection1Header.replaceAll("#VER#", "").replace("#NQ#", ""+ _qList.size());
        bwIndex.write(hSection1H);
        bwIndex.newLine();
        for (int i = 0; i < metaLines.size(); i++) {
            String[] metaParts = metaLines.get(i).split(" ");
            String hQuestion = _hTemplateSection1Question.replaceFirst("#N#", "" + metaParts[0]);
            if (isRoot) {
                hQuestion = hQuestion.replaceAll("\\.\\./", "");
            }
            hQuestion = hQuestion.replaceAll("Q#", metaParts[0]);
            for(char c : metaParts[1].toCharArray()) {
                hQuestion = hQuestion.replaceFirst(metaParts[0]+"#", metaParts[0] + c);
            }
            bwIndex.write(hQuestion);
            bwIndex.newLine();
        }
    }

    /**
     * Generates .meta and index.html for the full set of questions loaded in this generator.
     * @throws IOException
     */
    public void genRootIndex() throws IOException {
        Path pMeta = Paths.get(_pRoot.toString(), ".meta");
        Path pIndex = Paths.get(_pRoot.toString(), "index.html");

        List<String> metaLines = genMeta(pMeta, _qList, false, false);
        BufferedWriter bw = Files.newBufferedWriter(pIndex);
        genIndexStyle(bw);
        genIndexSection1(bw, metaLines, true);
        bw.close();
    }
}
