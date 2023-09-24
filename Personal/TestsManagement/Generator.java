package TestsManagement;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Generator {
    /**
    * Schema for the root & test .meta files
    */
    private class GeneratorMeta {
        private String name;
        private Map<String, String> display;
        private List<Question> questions;
        private String notes;
        boolean indexByName;
        boolean isRoot;

        private void shuffle() {
            questions = Question.shuffle(questions);
            display.clear();
            for(int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                display.put(indexByName ? q.getName() : "" + (i+1), q.getMetaLine(true));
            }
        }
    }

    private static final String _PRINT_BREAK = "<div style=\"break-after:page\"></div><br>";
    private static final int _MAX_PX_PER_PAGE = 800;
    private static final Gson _GSON = new GsonBuilder().setPrettyPrinting().create();

    private Path _pRoot;
    private Map<String, Question> _qMap;
    private List<Question> _qList;
    private String _hTemplateStyle;
    private String _hTemplateBooklet;
    private String _hTemplateSection1Header;
    private String _hTemplateSection1Question;

    /**
     * Loads the list of questions found in the ".template" sub-folder.
     * @throws IOException
     */
    private void loadQuestions(Path pTemplate) throws IOException {
        _qList = new LinkedList<Question>();
        _qMap = new TreeMap<String, Question>();
        for (Path qDir : Files.walk(pTemplate, 1).toArray(Path[]::new)) {
            if (Files.isDirectory(qDir) && !qDir.getFileName().toString().startsWith(".")) {
                Question question = new Question(qDir);
                _qList.add(question);
                _qMap.put(question.getName(),question);
            }
        }
    }

    /**
     * Loads the GeneratorMeta that is found at the given path.
     * @param pMeta - Path where the Generator .meta file is expected to be found.
     * @return The GeneratorMeta object loaded from the .meta file.
     * @throws IOException
     */
    private GeneratorMeta loadMeta(Path pMeta) throws IOException {
        String jsonMeta = String.join("\n", Files.readAllLines(pMeta));
        GeneratorMeta gMeta  = _GSON.fromJson(jsonMeta, GeneratorMeta.class);
        gMeta.questions.clear();
        for(Map.Entry<String, String> kvp : gMeta.display.entrySet()) {
            String qName = kvp.getValue().split(" ")[0];
            Question q = new Question(_qMap.get(qName));
            gMeta.questions.add(q);
        }
        return gMeta;
    }

    /**
     * Constructs a Generator object targetting the given {root} folder. The root is
     * expected to contain a ".template" subfolder, containing ".html" templates and Questions subfolder,
     * each with their own ".meta" and ".png" files.
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

        loadQuestions(pTemplate);
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
     * Generates the GeneratorMeta object and .meta file in the given path. The questions in the test are indexed
     * by either an ordinal number {1, 2, 3, ...} if preserveName=false, or their original name {Q1, Q2, ..}
     * if preserveName=true. The order of the questions in the meta.display map matches the order in meta.questions.
     * @param pMeta - Path to the .meta file.
     * @param qList - List of questions to be included.
     * @param preserveName - True if questions should preserve their names, false if an ordinal should be used instead.
     * @return The GeneratorMeta object.
     * @throws IOException
     */
    private GeneratorMeta genMeta(Path pMeta, List<Question> qList, boolean preserveName ) throws IOException {
        GeneratorMeta gMeta = new GeneratorMeta();
        gMeta.indexByName = preserveName;
        gMeta.isRoot = _pRoot.toFile().getName().equals(pMeta.getParent().toFile().getName());
        gMeta.name = gMeta.isRoot ? "." : pMeta.getParent().toFile().getName();
        gMeta.display = new HashMap<String, String>();
        gMeta.questions = new LinkedList<Question>();
        for(int i = 0; i < qList.size(); i++) {
            Question q = new Question(qList.get(i));
            gMeta.display.put(preserveName ? q.getName() : "" + (i+1), q.getMetaLine(false));
            gMeta.questions.add(q);
        }
        return gMeta;
    }

    private void saveMeta(Path pMeta, GeneratorMeta gMeta) throws IOException {
        if (!gMeta.isRoot || !Files.exists(pMeta.getParent())) {
            Files.createDirectories(pMeta.getParent());
        }
        BufferedWriter bw = Files.newBufferedWriter(pMeta);
        bw.write(_GSON.toJson(gMeta));
        bw.close();
    }

    private void genIndexHtml(Path pIndex, GeneratorMeta tMeta, boolean includeBooklet) throws IOException {
        BufferedWriter bw = Files.newBufferedWriter(pIndex);
        bw.write(_hTemplateStyle);
        if (includeBooklet) {
            //genBookletHtml(bw, tMeta);
        }
        genSection1Html(bw, tMeta);
        bw.close();
    }

    /**
     * Generates the index.html file in the given path. The questions and their answers are read from the metaLines and
     * are expected to exist in the .template folder.
     * @param pIndex - Path to the index.html file.
     * @param metaLines - Meta lines indicating what questions and what answers should be indexed.
     * @throws IOException
     */
    private void genSection1Html(BufferedWriter bwIndex, GeneratorMeta tMeta) throws IOException {
        String hSection1H = _hTemplateSection1Header
            .replaceAll("#TNAME#", tMeta.isRoot ? "." : tMeta.name)
            .replace("#QNUM#", "" + tMeta.questions.size());
        bwIndex.write(hSection1H);
        bwIndex.newLine();

        int pxSum = 0;
        for (int i = 0; i < tMeta.questions.size(); i++) {
            Question q = tMeta.questions.get(i);
            String qID = tMeta.indexByName ? q.getName() : "" + (i+1);
            String qMetaLine = tMeta.display.get(qID);
            String hSection1Q = q.editHtml(_hTemplateSection1Question, qID, qMetaLine);
            if (pxSum + q.getPxHeight() > _MAX_PX_PER_PAGE) {
                bwIndex.write(_PRINT_BREAK);
                bwIndex.newLine();
                pxSum = q.getPxHeight();
            } else {
                pxSum += q.getPxHeight();
            }
            bwIndex.write(hSection1Q);
            bwIndex.newLine();
        }
    }

    /**
     * Adjusts all paths in the GeneratorMeta object by prefixing them with the given pathPrefix
     * @param gMeta - GeneratorMeta object to be adjusted.
     * @param pathPrefix - Path prefix to be inserted in front of all paths.
     */
    private void adjustPaths(GeneratorMeta gMeta, String pathPrefix) {
        for(Question q : gMeta.questions) {
            q.adjustPath(pathPrefix);
        }
    }

    /**
     * Generates .meta and index.html for the full set of questions loaded in this generator.
     * @throws IOException
     */
    public void resetRoot(boolean regenMeta) throws IOException {
        Path pMeta = Paths.get(_pRoot.toString(), ".meta");
        GeneratorMeta gMeta;
        if (regenMeta) {
            gMeta = genMeta(pMeta, _qList, true);
        } else {
            gMeta = loadMeta(pMeta);
        }
        adjustPaths(gMeta, ".template/");
        saveMeta(pMeta, gMeta);
        Path pIndex = Paths.get(_pRoot.toString(), "index.html");
        genIndexHtml(pIndex, gMeta, false);
    }

    /**
     * Generates .meta and index.html files for the given test
     * @throws IOException
     */
    public void resetTest(String testName, String[] qIDs, boolean regenMeta) throws IOException {
        Path pMeta = Paths.get(_pRoot.toString(), testName, ".meta");
        GeneratorMeta gMeta;
        if (regenMeta) {
            List<Question> qList;
            if (qIDs.length == 0) {
                qList = _qList;
            } else {
                HashSet<Question> qSet = new HashSet<Question>();
                for(String qID : qIDs) {
                    if (!_qMap.containsKey(qID) || qSet.contains(qID)) {
                        throw new IllegalArgumentException(String.format("Question %s is non-existent or duplicated!", qID));
                    }
                    qSet.add(_qMap.get(qID));
                }
                qList = new LinkedList<Question>(qSet);
            }
            gMeta = genMeta(pMeta, qList, false);
        } else {
            gMeta = loadMeta(pMeta);
        }
        adjustPaths(gMeta, "../.template/");
        saveMeta(pMeta, gMeta);
        Path pIndex = Paths.get(_pRoot.toString(), testName, "index.html");
        genIndexHtml(pIndex, gMeta, true);
    }

    /**
    * Generates .meta and index.html files for each variant of the given test
    * @throws IOException
    */
    public void resetTestVariants(String testName, String[] vIDs, boolean regenMeta) throws IOException {
        Path pTMeta = Paths.get(_pRoot.toString(), testName, ".meta");
        GeneratorMeta gTMeta = loadMeta(pTMeta);

        for(int i = 0; i < vIDs.length; i++) {
            Path pVMeta = Paths.get(_pRoot.toString(), testName, vIDs[i], ".meta");
            GeneratorMeta gVMeta;
            if (regenMeta) {
                gVMeta = genMeta(pVMeta, gTMeta.questions, false);
                // Adjust variant name to include the test name
                gVMeta.name = String.format("%s.%s", gTMeta.name, gVMeta.name);
                gVMeta.shuffle();
            } else {
                gVMeta = loadMeta(pVMeta);
            }
            adjustPaths(gVMeta, "../../.template/");
            saveMeta(pVMeta, gVMeta);
            Path pVIndex = Paths.get(_pRoot.toString(), testName, vIDs[i], "index.html");
            genIndexHtml(pVIndex, gVMeta, true);
        }
    }
}
