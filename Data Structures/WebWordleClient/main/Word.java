package WebWordleClient.main;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;

/**
 * Defines a Wordle word as scraped from: https://github.com/steve-kasica/wordle-words
 */
public class Word implements Comparable<Word> {
    private String _word;
    private Double _occurrence;
    private Integer _day;

    public Word(String csvLine) {
        // each line is expected to contain 3 fields!
        String[] csvParts = csvLine.split(",");
        _word = csvParts[0].toUpperCase();
        _occurrence = Doubles.tryParse(csvParts[1]);
        _day = csvParts.length > 2 ? Ints.tryParse(csvParts[2]) : null;
    }

    public String getWord() {
        return _word;
    }

    @Override
    public int compareTo(Word o) {
        return -(int)Math.signum(_occurrence - o._occurrence);
    }

    @Override
    public String toString() {
        return String.format("[%s, %.6e, %d]", _word, _occurrence, _day);
    }
}
