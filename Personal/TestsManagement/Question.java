package TestsManagement;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Question {
    /**
     * Schema for the Question/.meta file 
     */
    public class QuestionMeta {
        private String name;
        private String question;
        private Map<String, String> choices;
        private String correct;
        private String answer;
        private String notes;

        public QuestionMeta(QuestionMeta qm) {
            name = qm.name;
            question = qm.question;
            // deep copy the map of choices
            choices = new TreeMap<String, String>(qm.choices);
            correct = qm.correct;
            answer = qm.answer;
            notes = qm.notes;
        }
    }

    private static final Gson _GSON = new GsonBuilder().setPrettyPrinting().create();
    private QuestionMeta _meta;
    private int _pxHeight;

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
        _meta = new QuestionMeta(q._meta);
        _pxHeight = q._pxHeight;
    }

    public Question(Path pQuestion) throws IOException {
        Path pMeta = Paths.get(pQuestion.toString(), ".meta");
        String jsonMeta = String.join("\n", Files.readAllLines(pMeta));
        _meta = _GSON.fromJson(jsonMeta, QuestionMeta.class);
        loadPxHeight(pQuestion);
    }

    private void loadPxHeight(Path pQuestion) throws IOException {
        Path pPng = Paths.get(pQuestion.toString(), _meta.question);
        BufferedImage bi = ImageIO.read(pPng.toFile());
        _pxHeight = bi.getHeight();
        for(String choice : _meta.choices.values()) {
            pPng = Paths.get(pQuestion.toString(), choice);
            bi = ImageIO.read(pPng.toFile());
            _pxHeight += bi.getHeight();
        }
    }

    public String getName() {
        return _meta.name;
    }

    public String getMetaLine(boolean shuffle) {
        List<String> choices = new LinkedList<String>(_meta.choices.keySet());
        if (shuffle) {
            choices = shuffle(choices);
        }
        return _meta.name + " " + String.join("", choices);
    }

    public int getPxHeight() {
        return _pxHeight;
    }

    public void adjustPath(String prefix) {
        _meta.question = String.format("%s%s/%s", prefix, _meta.name, _meta.question);
        _meta.answer = String.format("%s%s/%s", prefix, _meta.name, _meta.answer);
        for(Map.Entry<String, String> kvp : _meta.choices.entrySet()) {
            _meta.choices.put(kvp.getKey(), String.format("%s%s/%s", prefix, _meta.name, kvp.getValue()));
        }
    }

    public String editHtml(String hSection1Q, String qID, String metaLine, boolean isAnswer) {
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

    @Override
    public String toString() {
        return String.format("%s:%d", _meta.name, _pxHeight);
    }
}
