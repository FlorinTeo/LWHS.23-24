package TestsManagement;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WebDoc {
    public static final String _PRINT_BREAK = "<div style=\"break-after:page\"></div><br>";
    public static final int _MAX_PX_PER_PAGE = 880;
    public static final int _FRQ_ANSWER_PAGES = 4;

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

    // #region: [private methods] Loading HTML web document template
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
    // #endregion: [private methods] Loading web document template

    // #region: [private methods] Writing HTML web document parts
    private void genBookletHtml(BufferedWriter bw, GMeta gMeta) throws IOException {
        String bkHtml = _answers
            .replaceAll("#TNAME#", gMeta.getName())
            .replace("#QNUM#", "" + gMeta.getQuestions().size());

        int iMCQ = bkHtml.indexOf(_TAG_ANSWERS_MCQ);
        int iFRQ = bkHtml.indexOf(_TAG_ANSWERS_FRQ);
        bw.write(bkHtml.substring(0, iMCQ));
        bw.newLine();       
        //Write mcq answer lines
        for (int i = 0; i < gMeta.getQuestions().size(); i++) {
            String bkMCQLine = _answersMCQ.replace("#N#", "" + (i + 1));
            bw.write(bkMCQLine);
        }

        bw.write(bkHtml.substring(iMCQ + _TAG_ANSWERS_MCQ.length(), iFRQ));
        bw.newLine();
        //Write frq answer pages
        for (int i = 0; i < _FRQ_ANSWER_PAGES; i++) {
            String bkFRQPage = _answersFRQ.replace("#GRIDPATH#", gMeta.getPathPrefix());
            bw.write(bkFRQPage);
        }

        bw.write(bkHtml.substring(iFRQ + _TAG_ANSWERS_FRQ.length()));
        System.out.printf("ansBk pages = 6\n");
    }

    private void genSection1Html(BufferedWriter bw, GMeta gMeta, boolean answers) throws IOException {
        String s1Html = _section1
            .replaceAll("#TNAME#", gMeta.getName())
            .replace("#QNUM#", "" + gMeta.getQuestions().size());

        int iMCQ = s1Html.indexOf(_TAG_SECTION1_MCQ);
        bw.write(s1Html.substring(0, iMCQ));
        bw.newLine();       
        int nPages = gMeta.genMCQHtml(bw, _section1MCQ, answers);
        bw.write(s1Html.substring(iMCQ + _TAG_SECTION1_MCQ.length()));
        System.out.printf("mcq pages = %d\n", nPages);
    }
    // #endregion: [private methods] Writing HTML web document parts

    public WebDoc(List<String> lines) {
        Queue<String> qLines = new LinkedList<String>(lines);
        _style = loadBlock(_TAG_STYLE, qLines);
        loadAnswers(qLines);
        loadSection1(qLines);
        loadSection2(qLines);
        loadAppendix(qLines);
    }

    public void genIndexHtml(GMeta gMeta, Path pRoot) throws IOException {
        Path pIndexHtml = Paths.get(pRoot.toString(), "index.html");
        BufferedWriter bw = Files.newBufferedWriter(pIndexHtml);
        // fill in the styling portion
        bw.write(_style);
        // fill in the section 1 answers
        genSection1Html(bw, gMeta, true);
        // TODO: fill in the section 2 pages
        // TODO: fill in the appendix pages
        bw.close();
    }

    public void genTestHtml(GMeta gMeta, Path pTest) throws IOException {
        Path pTestHtml = Paths.get(pTest.toString(), "test.html");
        BufferedWriter bw = Files.newBufferedWriter(pTestHtml);
        // fill in the styling portion
        bw.write(_style);
        // fill in the booklet
        genBookletHtml(bw, gMeta);
        // fill in the section 1 questions
        genSection1Html(bw, gMeta, false);
        // TODO: fill in the section 2 pages
        // TODO: fill in the appendix pages
        bw.close();

        Path pAnswersHtml = Paths.get(pTest.toString(), "answers.html");
        bw = Files.newBufferedWriter(pAnswersHtml);
        // fill in the styling portion
        bw.write(_style);
        // fill in the section 1 answers
        genSection1Html(bw, gMeta, true);
        // TODO: fill in the section 2 answers
        bw.close();
    }
}
