package TestsManagement;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Question {
    private static final Gson _GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Schema for the Question/.meta file 
     */
    public class QMeta {
        private String name;
        private String type;
        private String question;
        private Map<String, String> choices;
        private String correct;
        private String answer;
        private String notes;
        private List<String> textPages;
        private List<String> solutionPages;

        public QMeta(QMeta qm) {
            name = qm.name;
            type = qm.type;
            question = qm.question;
            // deep copy the map of choices
            choices = (qm.choices != null) ? new TreeMap<String, String>(qm.choices) : null;
            correct = qm.correct;
            answer = qm.answer;
            notes = qm.notes;
            textPages = (qm.textPages != null) ? new ArrayList<String>(qm.textPages) : null;
            solutionPages = (qm.solutionPages != null) ? new ArrayList<String>(qm.solutionPages) : null;
        }
    }

    private QMeta _meta;
    private int _pxHeightQ;
    private int _pxHeightA;

     /**
     * Utility method to shuffle a given list into another one.
     * Method does not modify its {list} parameter.
     * @param list - list to be shuffled.
     * @return shuffled list.
     */
    public static <T> List<T> shuffle(List<T> list) {
        LinkedList<T> sList = new LinkedList<T>();
        for (T item : list) {
            if (Math.random() < .5) {
                sList.addFirst(item);
            } else {
                sList.addLast(item);
            }
        }
        return sList;
    }

    /**
     * Deep cloning constructor.
     * @param q - Question object to be cloned.
     */
    public Question(Question q) {
        _meta = new QMeta(q._meta);
        _pxHeightQ = q._pxHeightQ;
        _pxHeightA = q._pxHeightA;
    }

    public Question(Path pQuestion) throws IOException {
        Path pMeta = Paths.get(pQuestion.toString(), ".meta");
        String jsonMeta = String.join("\n", Files.readAllLines(pMeta));
        _meta = _GSON.fromJson(jsonMeta, QMeta.class);
        loadPxHeight(pQuestion.toString());
    }

    private int pngHeight(Path pPng) throws IOException {
        return ImageIO.read(pPng.toFile()).getHeight();
    }

    private void loadPxHeight(String relPath) throws IOException {
        if (_meta.type.equalsIgnoreCase("mcq")) {
            _pxHeightQ = pngHeight(Paths.get(relPath, _meta.question));
            _pxHeightA = pngHeight(Paths.get(relPath, _meta.answer));

            for(String choice : _meta.choices.values()) {
                int pxHeightC = pngHeight(Paths.get(relPath, choice));
                _pxHeightQ += pxHeightC;
                _pxHeightA += pxHeightC;
            }
        } else if (_meta.type.equalsIgnoreCase("frq")) {
            _pxHeightQ = 0;
            for(String textPage : _meta.textPages) {
                _pxHeightQ += pngHeight(Paths.get(relPath, textPage));
            }
            _pxHeightA = 0;
            for(String solPage : _meta.solutionPages) {
                _pxHeightA += pngHeight(Paths.get(relPath, solPage));
            }
        } else if (_meta.type.equalsIgnoreCase("apx")) {
            _pxHeightQ = 0;
            for(String textPage : _meta.textPages) {
                _pxHeightQ += pngHeight(Paths.get(relPath, textPage));
            }
        }
    }

    public String getName() {
        return _meta.name;
    }

    public String getType() {
        return _meta.type;
    }

    public String getMetaLine(boolean shuffle) {
        List<String> choices = new LinkedList<String>(_meta.choices.keySet());
        if (shuffle) {
            choices = shuffle(choices);
        }
        return _meta.name + " " + String.join("", choices);
    }

    public int getPxHeightQ() {
        return _pxHeightQ;
    }

    public int getPxHeightA() {
        return _pxHeightA;
    }

    public String getPathPrefix() {
        String pathPrefix = "";
        int iQFile = _meta.question.indexOf(_meta.name);
        if (iQFile != -1) {
            pathPrefix = _meta.question.substring(0, iQFile);
        }
        return pathPrefix;
    }

    public void adjustPath(String pathPrefix) {
        switch(_meta.type.toLowerCase()) {
            case "mcq":
                String qFile = Paths.get(_meta.question).toFile().getName();
                String aFile = Paths.get(_meta.answer).toFile().getName();
                _meta.question = String.format("%s%s/%s", pathPrefix, _meta.name, qFile);
                _meta.answer = String.format("%s%s/%s", pathPrefix, _meta.name, aFile);
                for(Map.Entry<String, String> kvp : _meta.choices.entrySet()) {
                    // choice file name
                    String cFile = Paths.get(kvp.getValue()).toFile().getName();
                    _meta.choices.put(kvp.getKey(), String.format("%s%s/%s", pathPrefix, _meta.name, cFile));
                }
                break;
            case "frq":
            case "apx":
                for(int i = 0; i < _meta.textPages.size(); i++) {
                    String tpFile = Paths.get(_meta.textPages.get(i)).toFile().getName();
                    _meta.textPages.set(i, String.format("%s%s/%s", pathPrefix, _meta.name, tpFile));
                }
                if (_meta.type.equalsIgnoreCase("apx")) {
                    break;
                }
                for(int i = 0; i < _meta.solutionPages.size(); i++) {
                    String spFile = Paths.get(_meta.solutionPages.get(i)).toFile().getName();
                    _meta.solutionPages.set(i, String.format("%s%s/%s", pathPrefix, _meta.name, spFile));
                }
                break;
        }
    }

    public String editMCQHtml(String hSection1Q, String qID, String metaLine, boolean isAnswer) {
        String metaChoices = metaLine.split(" ")[1];
        hSection1Q = hSection1Q
            .replaceFirst("#QID#", qID)
            .replaceFirst("#QPNG#", isAnswer ? _meta.answer : _meta.question);
        for(char c : metaChoices.toCharArray()) {
            String cPng = _meta.choices.get(""+c);
            hSection1Q = isAnswer && _meta.correct.equals(""+c)
                ? hSection1Q.replaceFirst("#ANS#", "ansTd")
                : hSection1Q.replaceFirst("#ANS#", "refTd");
            hSection1Q = hSection1Q.replaceFirst("#CPNG#", cPng);
        }
        return hSection1Q;
    }

    public int genFRQHtml(BufferedWriter bw, String format, String qID, boolean solutions) {
        return 0;
    }

    public int genApxHtml(BufferedWriter bw, String format, String qID) {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("%s:pxQ=%d,pxA=%d", _meta.name, _pxHeightQ, _pxHeightA);
    }
}
