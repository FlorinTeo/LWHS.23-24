package TestsManagement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Generator {
    private Path _pRoot;
    private Map<String, Question> _qMap;
    private List<Question> _qList;
    private WebDoc _webDoc;

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
     * Constructs a Generator object targetting the given {root} folder. The root is
     * expected to contain a ".template" subfolder, containing ".index.html" template and Questions subfolder,
     * each with their own ".meta" and ".png" files.
     * @param root - working folder for this generator.
     * @throws IOException
     */
    public Generator(String root) throws IOException {
        _pRoot = Paths.get(root);
        Path pTemplate = Paths.get(root, ".template");
        Path phIndex = Paths.get(root,".template", ".index.html");

        if (!Files.isDirectory(_pRoot) || !Files.isDirectory(pTemplate)) {
            throw new IOException("Template folder is missing or invalid!");
        }

        loadQuestions(pTemplate);
        _webDoc = new WebDoc(Files.readAllLines(phIndex));
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
     * Generates .meta and index.html for the full set of questions loaded in this generator.
     * @throws IOException
     */
    public void genRoot(boolean regenMeta) throws IOException {
        GMeta mRoot;
        if (regenMeta) {
            mRoot = new GMeta(".", _qList);
            mRoot.adjustPath(".template/");
            mRoot.save(_pRoot);
        } else {
             mRoot = new GMeta(_pRoot);
        }
        _webDoc.genIndexHtml(mRoot, _pRoot);
    }

    /**
     * Generates .meta and index.html files for the given test
     * @throws IOException
     */
    public void genTest(String testName, String[] qIDs, boolean regenMeta) throws IOException {
        Path pTest = Paths.get(_pRoot.toString(), testName);
        GMeta mTest;
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
            mTest = new GMeta(testName, qList);
            mTest.adjustPath("../.template/");
            mTest.anonymize(false);
            mTest.save(pTest);
        } else {
             mTest = new GMeta(pTest);
        }
        _webDoc.genTestHtml(mTest, pTest);
    }

    /**
    * Generates .meta and index.html files for each variant of the given test
    * @throws IOException
    */
    public void genTestVariants(String testName, String[] vIDs, boolean regenMeta) throws IOException {
        Path pTest = Paths.get(_pRoot.toString(), testName);
        GMeta mTest = new GMeta(pTest);
        for(int i = 0; i < vIDs.length; i++) {
            Path pVariant = Paths.get(pTest.toString(), vIDs[i]);
            GMeta mVariant;
            if (regenMeta) {
                mVariant = new GMeta(mTest.getName() + "." + vIDs[i], mTest.getQuestions());
                mVariant.adjustPath("../../.template/");
                mVariant.anonymize(true);
                mVariant.save(pVariant);
            } else {
                mVariant = new GMeta(pVariant);
            }
            _webDoc.genTestHtml(mVariant, pVariant);
        }
    }
}
