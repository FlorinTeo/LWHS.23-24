package TestsManagement;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WebDoc {

    private static final String _TAG_STYLE = "<!--======== STYLE ========-->";
    private String _style;

    private static final String _TAG_ANSWERS = "<!--======== ANSWERS BOOKLET ========-->";
    private String _answers;
    private static final String _TAG_ANSWERS_MCQ = "<!--~~~~~~~~ ANSWER MCQ ~~~~~~~~-->";
    private String _answersMCQ;
    private static final String _TAG_ANSWERS_FRQ = "<!--~~~~~~~~ ANSWER FRQ ~~~~~~~~-->";
    private String _answersFRQ;

    private static final String _TAG_SECTION1 = "<!--======== SECTION 1 (MCQ) ========-->";
    private String _section1;
    private static final String _TAG_SECTION1_MCQ = "<!--~~~~~~~~ MULTIPLE CHOICE QUESTION (MCQ) ~~~~~~~~-->";
    private String _section1MCQ;

    private static final String _TAG_SECTION2 = "<!--======== SECTION 2 (FRQ) ========-->";
    private String _section2;
    private static final String _TAG_SECTION2_PAGE = "<!--~~~~~~~~ SECTION 2 PAGE ~~~~~~~~-->";
    private String _section2Page;

    private static final String _TAG_APPENDIX = "<!--======== APPENDIX ========-->";
    private String _appendix;
    private static final String _TAG_APPENDIX_PAGE = "<!--~~~~~~~~ APPENDIX PAGE ~~~~~~~~-->";
    private String _appendixPage;

    // #region private methods
    private String loadEmptyLines(Queue<String> qLines) {
        String emptyLines = "";
        while(!qLines.isEmpty() && qLines.peek().isBlank()) {
            emptyLines += "\n";
            qLines.remove();
        }
        if (qLines.isEmpty()) {
            throw new RuntimeException("Invalid .index[eof] format.");
        }
        return emptyLines;
    }

    private String loadBlock(String tag, Queue<String> qLines) {
        loadEmptyLines(qLines);
        if (qLines.peek().contains(tag)) {
            String block = qLines.remove() + "\n";
            while(!qLines.isEmpty()) {
                String line = qLines.remove();
                block += line + "\n";
                if (line.contains(tag)) {
                    return block;
                }
            }
        }
        throw new RuntimeException("Invalid .index[style] format.");
    }

    private void loadAnswers(Queue<String> qLines) {
        _answers = loadEmptyLines(qLines);
        if (qLines.peek().contains(_TAG_ANSWERS)) {
            _answers += qLines.remove() + "\n";
            while(!qLines.isEmpty()) {
                String line = qLines.peek();
                if (line.contains(_TAG_ANSWERS_MCQ)) {
                    _answersMCQ = loadBlock(_TAG_ANSWERS_MCQ, qLines);
                    _answers += _TAG_ANSWERS_MCQ + "\n";
                } else if (line.contains(_TAG_ANSWERS_FRQ)) {
                    _answersFRQ = loadBlock(_TAG_ANSWERS_FRQ, qLines);
                    _answers += _TAG_ANSWERS_FRQ + "\n";
                } else {
                    _answers += qLines.remove() + "\n";
                }
                if (line.contains(_TAG_ANSWERS)) {
                    return;
                }    
            }
        }
        throw new RuntimeException("Invalid .index[answers] format.");
    }

    private void loadSection1(Queue<String> qLines) {
        _section1 = loadEmptyLines(qLines);
        if (qLines.peek().contains(_TAG_SECTION1)) {
            _section1 += qLines.remove() + "\n";
            while(!qLines.isEmpty()) {
                String line = qLines.peek();
                if (line.contains(_TAG_SECTION1_MCQ)) {
                    _section1MCQ = loadBlock(_TAG_SECTION1_MCQ, qLines);
                    _section1 += _TAG_SECTION1_MCQ + "\n";
                } else {
                    _section1 += qLines.remove() + "\n";
                }
                if (line.contains(_TAG_SECTION1)) {
                    return;
                }    
            }
        }
        throw new RuntimeException("Invalid .index[section1] format");
    }

    private void loadSection2(Queue<String> qLines) {
        _section2 = loadEmptyLines(qLines);
        if (qLines.peek().contains(_TAG_SECTION2)) {
            _section2 += qLines.remove() + "\n";
            while(!qLines.isEmpty()) {
                String line = qLines.peek();
                if (line.contains(_TAG_SECTION2_PAGE)) {
                    _section2Page = loadBlock(_TAG_SECTION2_PAGE, qLines);
                    _section2 += _TAG_SECTION2_PAGE + "\n";
                } else {
                    _section2 += qLines.remove() + "\n";
                }
                if (line.contains(_TAG_SECTION2)) {
                    return;
                }    
            }
        }
        throw new RuntimeException("Invalid .index[section2] format");
    }

    private void loadAppendix(Queue<String> qLines) {
        _appendix = loadEmptyLines(qLines);
        if (qLines.peek().contains(_TAG_APPENDIX)) {
            _appendix += qLines.remove() + "\n";
            while(!qLines.isEmpty()) {
                String line = qLines.peek();
                if (line.contains(_TAG_APPENDIX_PAGE)) {
                    _appendixPage = loadBlock(_TAG_APPENDIX_PAGE, qLines);
                    _appendix += _TAG_APPENDIX_PAGE + "\n";
                } else {
                    _appendix += qLines.remove() + "\n";
                }
                if (line.contains(_TAG_APPENDIX)) {
                    return;
                }    
            }
        }
        throw new RuntimeException("Invalid .index[section2] format");
    }
    // #endregion private methods

    public WebDoc(List<String> lines) {
        Queue<String> qLines = new LinkedList<String>(lines);
        _style = loadBlock(_TAG_STYLE, qLines);
        loadAnswers(qLines);
        loadSection1(qLines);
        loadSection2(qLines);
        loadAppendix(qLines);
    }

    @Override
    public String toString() {
        String output = "";
        output += "########################" + _TAG_STYLE + "\n" + _style;
        output += "\n\n\n";
        output += "########################" + _TAG_ANSWERS + "\n" + _answers;
        output += "\n\n\n";
        output += "########################" + _TAG_ANSWERS_MCQ + "\n" + _answersMCQ;
        output += "\n\n\n";
        output += "########################" + _TAG_ANSWERS_FRQ + "\n" + _answersFRQ;
        output += "\n\n\n";
        output += "########################" + _TAG_SECTION1 + "\n" + _section1;
        output += "\n\n\n";
        output += "########################" + _TAG_SECTION1_MCQ + "\n" + _section1MCQ;
        output += "\n\n\n";
        output += "########################" + _TAG_SECTION2 + "\n" + _section2;
        output += "\n\n\n";
        output += "########################" + _TAG_SECTION2_PAGE + "\n" + _section2Page;
        output += "\n\n\n";
        output += "########################" + _TAG_APPENDIX + "\n" + _appendix;
        output += "\n\n\n";
        output += "########################" + _TAG_APPENDIX_PAGE + "\n" + _appendixPage;
        return output;
    }
}
