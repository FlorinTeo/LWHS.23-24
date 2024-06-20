package WebWordleClient.main;

import com.google.gson.Gson;

public class AnswerStats extends Answer {
    private boolean _isServer;
    private String _state;
    private int _count;
    private String[] _sessions;

    public AnswerStats(Answer answer, boolean isServer) {
        super(answer);
        Gson gson = new Gson();
        AnswerStats answerStats = gson.fromJson(this._content, AnswerStats.class);
        _state = answerStats._state;
        _count = answerStats._count;
        _sessions = answerStats._sessions;
        _isServer = isServer;
    }

    @Override
    public String toString() {
        String output = "";
        if (_httpCode >= 400) {
            output += String.format("Error: %s\n", _content);
        } else {
            if (_isServer) {
                output += String.format("Server state   : %s\n", _state);
                output += String.format("Sessions count : %d\n", _count);
            } else {
                output += String.format("Game state : %s\n", _state);
                output += String.format("Guesses    : %d\n", _count);
            }
            for(String session : _sessions) {
                output += String.format("        %s\n", session);
            }
        }
        output +="_____________";
        return output;
    }
}
