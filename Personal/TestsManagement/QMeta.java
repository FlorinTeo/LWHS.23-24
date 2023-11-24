package TestsManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Schema for the JSON Deserialization of Question/.meta file 
 */
public class QMeta {
    String name;
    String type;
    String notes;
    // fields for multiple-choice questions (mcq) 
    String question;
    Map<String, String> choices;
    String correct;
    String answer;
    // fields for multiple-choice bundles (mcb)
    String context;
    List<String> questions;
    // fields for free-response questions (frq) and appendix pages (apx)
    List<String> textPages;
    List<String> solutionPages;

    QMeta(QMeta qm) {
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