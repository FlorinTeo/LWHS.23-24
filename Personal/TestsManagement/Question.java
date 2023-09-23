package TestsManagement;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

public class Question {
    private static Gson _gson = new Gson();

    /**
     * Schema for the Question/.meta file 
     */
    public class QuestionMeta {
        private String name;
        private String question;
        private Map<String, String> choices;
        private String correct;
        private String notes;
    }

    private QuestionMeta _meta;
    private int _pxHeight;

     /**
     * Utility method to shuffle a given list into another one.
     * Method does not modify its {list} parameter.
     * @param list - list to be shuffled.
     * @return shuffled list.
     */
    public static List<String> shuffle(List<String> list) {
        LinkedList<String> sList = new LinkedList<String>();
        for (String item : list) {
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
    private Question(Question q) {
        _meta = q._meta;
        // deep copy the map of choices
        for(Map.Entry<String, String> kvp : q._meta.choices.entrySet()) {
            _meta.choices.put(kvp.getKey(), kvp.getValue());
        }
        _pxHeight = q._pxHeight;
    }

    public Question(Path pQuestion) throws IOException {
        Path pMeta = Paths.get(pQuestion.toString(), ".meta");
        String jsonMeta = String.join("\n", Files.readAllLines(pMeta));
        _meta = _gson.fromJson(jsonMeta, QuestionMeta.class);
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

    public String getMetaLine() {
        return _meta.name + " " + String.join("", _meta.choices.keySet());
    }

    public void shuffle() {
        List<String> choiceKeys = new LinkedList<String>(_meta.choices.keySet());
        Map<String, String> newChoices = new HashMap<String, String>();
        choiceKeys = shuffle(choiceKeys);
        for (String choiceKey : choiceKeys) {
            newChoices.put(choiceKey, _meta.choices.get(choiceKey));
        }
        _meta.choices = newChoices;
    }

    @Override
    public String toString() {
        return _meta.name;
    }
}
