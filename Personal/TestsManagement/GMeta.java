package TestsManagement;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GMeta {
    private static final Gson _GSON = new GsonBuilder().setPrettyPrinting().create();

    private String _name;

    /**
     * Maps the name/index of a question with the specific question and it's choices in the chosen (same/shuffled) order.
     * I.e:  
     *     "_display": {
     *         "1": "Q3 dbace",
     *         "2": "Q2 bacde",
     *         "3": "Q1 dbace",
     *         "4": "Q4 dcabe"
     *     },
     */
    private Map<String, String> _display;
    private List<Question> _mcQuestions;
    private List<Question> _frQuestions;
    private List<Question> _appendix;
    private String _notes;
    private boolean _isAnonymized;

    private void reset() {
        _name = "";
        _display = new TreeMap<String, String>();
        _mcQuestions = new LinkedList<Question>();
        _frQuestions = new LinkedList<Question>();
        _appendix = new LinkedList<Question>();
        _notes = "";
        _isAnonymized = false;
    }

    public GMeta() {
        reset();
    }

    public GMeta(Path pMetaDir) throws IOException {
        Path pMeta = Paths.get(pMetaDir.toString(), ".meta");
        String jsonMeta = String.join("\n", Files.readAllLines(pMeta));
        GMeta loaded  = _GSON.fromJson(jsonMeta,GMeta.class);
        _name = loaded._name;
        _display = loaded._display;
        _mcQuestions = loaded._mcQuestions;
        _frQuestions = loaded._frQuestions;
        _appendix = loaded._appendix;
        _notes = loaded._notes;
        _isAnonymized = loaded._isAnonymized;
    }

    public GMeta(String name, List<Question> qList) {
        reset();
        _name = name;
        for(int i = 0; i < qList.size(); i++) {
            Question q = new Question(qList.get(i));
            switch(q.getType().toLowerCase()) {
                case "mcq":
                    _display.put(q.getName(), q.getMetaLine(false));
                    _mcQuestions.add(q);
                    break;
                case "frq":
                    _frQuestions.add(q);
                    break;
                case "apx":
                    _appendix.add(q);
                    break;
            }
        }
        _notes = "";
        _isAnonymized = false;
    }

    public String getName() {
        return _name;
    }

    public List<Question> getMCQuestions() {
        return _mcQuestions;
    }

    public List<Question> getFRQuestions() {
        return _frQuestions;
    }

    public List<Question> getAppendix() {
        return _appendix;
    }

    public List<Question> getQuestions() {
        List<Question> allQuestions = new LinkedList<Question>();
        allQuestions.addAll(_mcQuestions);
        allQuestions.addAll(_frQuestions);
        allQuestions.addAll(_appendix);
        return allQuestions;
    }

    public void adjustPath(String pathPrefix) {
        for(Question q : _mcQuestions) {
            q.adjustPath(pathPrefix);
        }
        for(Question q : _frQuestions) {
            q.adjustPath(pathPrefix);
        }
        for(Question q : _appendix) {
            q.adjustPath(pathPrefix);
        }
    }

    public String getPathPrefix() {
        return _mcQuestions.get(0).getPathPrefix();
    }

    public void anonymize(boolean randomize) {
        if (randomize) {
            _mcQuestions = Question.shuffle(_mcQuestions);
        }
        _display.clear();
        for(int i = 0; i < _mcQuestions.size(); i++) {
            Question q = _mcQuestions.get(i);
            _display.put("" + (i+1), q.getMetaLine(randomize));
        }
        _isAnonymized = true;
    }

    public void save(Path pMetaDir) throws IOException {
        if (!_name.equals(".") && !Files.exists(pMetaDir)) {
            Files.createDirectories(pMetaDir);
        }

        Path pMeta = Paths.get(pMetaDir.toString(), ".meta");
        BufferedWriter bw = Files.newBufferedWriter(pMeta);
        bw.write(_GSON.toJson(this));
        bw.close();
    }

    public int genMCQHtml(BufferedWriter bw, String format, boolean answers) throws IOException {
        int pxSum = 0;
        int nPages = 1;
        for (int i = 0; i < _mcQuestions.size(); i++) {
            Question q = _mcQuestions.get(i);
            String qID = _isAnonymized ? "" + (i+1) : q.getName();
            String qMetaLine = _display.get(qID);
            String hSection1Q = q.editMCQHtml(format, qID, qMetaLine, answers);
            int pxHeight = answers ? q.getPxHeightA() : q.getPxHeightQ();
            if (pxSum + pxHeight > WebDoc._MAX_PX_PER_PAGE) {
                nPages++;
                bw.write(WebDoc._PRINT_BREAK);
                bw.newLine();
                pxSum = pxHeight;
            } else {
                pxSum += pxHeight;
            }
            bw.write(hSection1Q);
            bw.newLine();
        }
        return nPages;
    }
}

