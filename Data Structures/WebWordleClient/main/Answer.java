package WebWordleClient.main;

public class Answer {
    protected int _httpCode;
    protected String _content;

    public Answer(int httpCode, String content) {
        _httpCode = httpCode;
        _content = content;
    }

    public Answer(Answer other) {
        _httpCode = other._httpCode;
        _content = other._content;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s", _httpCode, _content);
    }
}
