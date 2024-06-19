package WebWordleClient.main;

import com.google.gson.Gson;

public class AnswerSession extends Answer {
    private String _sid;
    private String _message;

     public AnswerSession(Answer answer) {
        super(answer);
        Gson gson = new Gson();
        AnswerSession answerSession = gson.fromJson(this._content, AnswerSession.class);
        _sid = answerSession._sid;
        _message = answerSession._message;
    }

    public String getSessionId() {
        return _sid;
    }

    @Override
    public String toString() {
        String output = "";
        output += String.format("SessionId: %s\n", _sid);
        output += String.format("Message  : %s", _message);
        return output;
    }
}
