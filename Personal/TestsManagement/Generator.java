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
        // Free-form descriptive note, if ever useful
        // private String notes;
        private boolean isRoot;

        private void shuffle() {
            questions = Question.shuffle(questions);
            display.clear();
            for(int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                display.put(isRoot ? q.getName() : "" + (i+1), q.getMetaLine(true));
            }
        }
    }

    private static final String _PRINT_BREAK = "<div style=\"break-after:page\"></div><br>";
    private static final int _MAX_PX_PER_PAGE = 880;
    private static final Gson _GSON = new GsonBuilder().setPrettyPrinting().create();

    private Path _pRoot;
    private Map<String, Question> _qMap;
    private List<Question> _qList;
    private String _hTemplateStyle;
    private String _hTemplateBooklet;
    private String _hTemplateBookletAns;
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
        Path phBookletAns = Paths.get(root, ".template", ".bookletAns.html");
        Path phSection1H = Paths.get(root, ".template", ".section1H.html");
        Path phSection1Q = Paths.get(root, ".template", ".section1Q.html");

        if (!Files.isDirectory(_pRoot) || !Files.isDirectory(pTemplate)) {
            throw new IOException("Template folder is missing or invalid!");
        }

        loadQuestions(pTemplate);
        _hTemplateStyle = String.join("\n", Files.readAllLines(phStyle));
        _hTemplateBooklet = String.join("\n", Files.readAllLines(phBooklet));
        _hTemplateBookletAns = String.join("\n", Files.readAllLines(phBookletAns));
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
    private GeneratorMeta genMeta(Path pMeta, List<Question> qList, String qPathPrefix ) throws IOException {
        GeneratorMeta gMeta = new GeneratorMeta();
        gMeta.isRoot = _pRoot.toFile().getName().equals(pMeta.getParent().toFile().getName());
        gMeta.name = gMeta.isRoot ? "." : pMeta.getParent().toFile().getName();
        gMeta.display = new HashMap<String, String>();
        gMeta.questions = new LinkedList<Question>();
        for(int i = 0; i < qList.size(); i++) {
            Question q = new Question(qList.get(i));
            gMeta.display.put(gMeta.isRoot ? q.getName() : "" + (i+1), q.getMetaLine(false));
            gMeta.questions.add(q);
            q.adjustPath(qPathPrefix);
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

    private void genIndexHtml(Path pIndex, GeneratorMeta gMeta, String gridPath) throws IOException {
        BufferedWriter bw = Files.newBufferedWriter(pIndex);
        // fill in the styling portion
        bw.write(_hTemplateStyle);
        // optionally fill in the booklet
        if (!gMeta.isRoot) {
            genBookletHtml(bw, gMeta, gridPath);
        }
        // finally fill in the questions
        genSection1Html(bw, gMeta, false);
        bw.close();
    }

    private void genAnswersHtml(Path pAnswers, GeneratorMeta gMeta) throws IOException {
        BufferedWriter bw = Files.newBufferedWriter(pAnswers);
        bw.write(_hTemplateStyle);
        genSection1Html(bw, gMeta, true);
        bw.close();
    }

    private void genBookletHtml(BufferedWriter bwIndex, GeneratorMeta gMeta, String gridPath) throws IOException {
        String hBooklet = _hTemplateBooklet
            .replaceAll("#TNAME#", gMeta.isRoot ? "." : gMeta.name)
            .replaceAll("#GRIDPATH#", gridPath);
        for (int i = 0; i < gMeta.questions.size(); i++) {
            String hAnswer = _hTemplateBookletAns.replace("#N#", "" + (i+1));
            hBooklet = hBooklet.replace("#ANS#", hAnswer + "\n    #ANS#");
        }
        hBooklet = hBooklet.replace("\n    #ANS#", "");
        bwIndex.write(hBooklet);
    }

    /**
     * Generates the index.html file in the given path. The questions and their answers are read from the metaLines and
     * are expected to exist in the .template folder.
     * @param pIndex - Path to the index.html file.
     * @param metaLines - Meta lines indicating what questions and what answers should be indexed.
     * @throws IOException
     */
    private void genSection1Html(BufferedWriter bwIndex, GeneratorMeta gMeta, boolean isAnswer) throws IOException {
        String hSection1H = _hTemplateSection1Header
            .replaceAll("#TNAME#", gMeta.isRoot ? "." : gMeta.name)
            .replace("#QNUM#", "" + gMeta.questions.size());
        bwIndex.write(hSection1H);
        bwIndex.newLine();

        int pxSum = 0;
        for (int i = 0; i < gMeta.questions.size(); i++) {
            Question q = gMeta.questions.get(i);
            String qID = gMeta.isRoot ? q.getName() : "" + (i+1);
            String qMetaLine = gMeta.display.get(qID);
            String hSection1Q = q.editHtml(_hTemplateSection1Question, qID, qMetaLine, isAnswer);
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
     * Generates .meta and index.html for the full set of questions loaded in this generator.
     * @throws IOException
     */
    public void genRoot(boolean regenMeta) throws IOException {
        Path pMeta = Paths.get(_pRoot.toString(), ".meta");
        GeneratorMeta gMeta;
        if (regenMeta) {
            gMeta = genMeta(pMeta, _qList, ".template/");
        } else {
            gMeta = loadMeta(pMeta);
        }
        //adjustPaths(gMeta, ".template/");
        saveMeta(pMeta, gMeta);
        Path pIndex = Paths.get(_pRoot.toString(), "index.html");
        genIndexHtml(pIndex, gMeta,".template/");
        Path pAnswers = Paths.get(_pRoot.toString(), "answers.html");
        genAnswersHtml(pAnswers, gMeta);
    }

    /**
     * Generates .meta and index.html files for the given test
     * @throws IOException
     */
    public void genTest(String testName, String[] qIDs, boolean regenMeta) throws IOException {
        Path pMeta = Paths.get(_pRoot.toString(), testName, ".meta");
        GeneratorMeta gMeta;
        if (regenMeta) {
            List<Question> qList;
            if (qIDs.length == 0) {
                qList = _qList;
            } else {
                HashSet<Question> qSet = new HashSet<Question>();
                // make sure there are no duplicates in the qIDs
                for(String qID : qIDs) {
                    if (!_qMap.containsKey(qID) || qSet.contains((Object)qID)) {
                        throw new IllegalArgumentException(String.format("Question %s is non-existent or duplicated!", qID));
                    }
                    qSet.add(_qMap.get(qID));
                }
                qList = new LinkedList<Question>(qSet);
            }
            gMeta = genMeta(pMeta, qList, "../.template/");
        } else {
            gMeta = loadMeta(pMeta);
        }
        saveMeta(pMeta, gMeta);
        Path pIndex = Paths.get(_pRoot.toString(), testName, "index.html");
        genIndexHtml(pIndex, gMeta, "../.template/");
        Path pAnswers = Paths.get(_pRoot.toString(), testName, "answers.html");
        genAnswersHtml(pAnswers, gMeta);
    }

    /**
    * Generates .meta and index.html files for each variant of the given test
    * @throws IOException
    */
    public void genTestVariants(String testName, String[] vIDs, boolean regenMeta) throws IOException {
        Path pTMeta = Paths.get(_pRoot.toString(), testName, ".meta");
        GeneratorMeta gTMeta = loadMeta(pTMeta);

        for(int i = 0; i < vIDs.length; i++) {
            Path pVMeta = Paths.get(_pRoot.toString(), testName, vIDs[i], ".meta");
            GeneratorMeta gVMeta;
            if (regenMeta) {
                gVMeta = genMeta(pVMeta, gTMeta.questions, "../../.template/");
                // Adjust variant name to include the test name
                gVMeta.name = String.format("%s.%s", gTMeta.name, gVMeta.name);
                gVMeta.shuffle();
            } else {
                gVMeta = loadMeta(pVMeta);
            }
            saveMeta(pVMeta, gVMeta);
            Path pVIndex = Paths.get(_pRoot.toString(), testName, vIDs[i], "index.html");
            genIndexHtml(pVIndex, gVMeta, "../../.template/");
            Path pVAnswers = Paths.get(_pRoot.toString(), testName, vIDs[i], "answers.html");
            genAnswersHtml(pVAnswers, gVMeta);
        }
    }
}
