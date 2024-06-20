package WebWordleClient.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * The database of Wordle words loaded from a csv file
 */
public class WordleDb {
    private static final String _WORDLEDB_PATH = "WebWordleClient/data/wordle.csv";
    private ArrayList<Word> _words;

    public WordleDb() {
        _words = new ArrayList<Word>();
    }

    public void load() {
        File wordleFile = new File(_WORDLEDB_PATH);
        System.out.printf("Wordle database ['%s'] loading .... ", wordleFile.getAbsolutePath());
        Scanner wordleDbReader = null;
        int count = 0;
        try {
            wordleDbReader = new Scanner(new File(_WORDLEDB_PATH));
            while(wordleDbReader.hasNextLine()) {
                String line = wordleDbReader.nextLine();
                count++;
                // skip the header
                if (count == 1) {
                    continue;
                }
                Word word = new Word(line);
                _words.add(word);
            }
        } catch (Exception e) {
            System.out.printf("[csvLine %d] %s\n", count, e.getMessage());
        } finally {
            if (wordleDbReader != null) {
                wordleDbReader.close();
            }
        }
        Collections.sort(_words);
        System.out.printf("DONE: [%d] words\n", count);
    }
}
