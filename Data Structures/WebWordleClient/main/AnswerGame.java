package WebWordleClient.main;

import com.google.gson.Gson;

public class AnswerGame extends Answer {
    private String _sid;
    private String _word;
    private String _message;
    private boolean _isReveal;
    
     public AnswerGame(Answer answer, boolean isReveal) {
        super(answer);
        Gson gson = new Gson();
        AnswerGame answerGame = gson.fromJson(this._content, AnswerGame.class);
        _sid = answerGame._sid;
        _word = answerGame._word;
        _message = answerGame._message;
        _isReveal = isReveal;
    }

    @Override
    public String toString() {
        String output = "";
        if (_httpCode >= 400) {
            output += String.format("Error: %s", _content);
        } else if (_isReveal) {
            output += String.format("Secret word is : %s\n", _word);
            output +=                      "_________ GAME SPOILED _________";
        } else {
            output += String.format("Word checked : %s\n", _word);
            output += String.format("Hints        : %s\n", _message);
            if (this._httpCode == 200) {
                output += String.format("_________ GAME SOLVED _________");
            } else {
                output += String.format("__________ TRY AGAIN __________");
            }
        }
        return output;
    }
}
