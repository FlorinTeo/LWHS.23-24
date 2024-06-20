package WebWordleClient.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class Program {
    private static String _sessionID = null;

    // i.e: "http://lwhs.westus2.cloudapp.azure.com:8080/web-wordle/api?cmd=new&name=MyName" (&name parameter is optional)
    private static final String _URL_NEW = "http://lwhs.westus2.cloudapp.azure.com:8080/web-wordle/api?cmd=new%s";
    
    // i.e: "http://lwhs.westus2.cloudapp.azure.com:8080/web-wordle/api?cmd=check&sid=8C537D99&word=SLATE"
    private static final String _URL_CHECK = "http://lwhs.westus2.cloudapp.azure.com:8080/web-wordle/api?sid=%s&cmd=check&word=%s";

    // i.e: "http://lwhs.westus2.cloudapp.azure.com:8080/web-wordle/api?cmd=reveal&sid=8C537D99"
    private static final String _URL_REVEAL = "http://lwhs.westus2.cloudapp.azure.com:8080/web-wordle/api?sid=%s&cmd=reveal";

    // i.e: "http://lwhs.westus2.cloudapp.azure.com:8080/web-wordle/api?cmd=stats&sid=8C537D99" (&sid parameter is optional)
    private static final String _URL_STATS = "http://lwhs.westus2.cloudapp.azure.com:8080/web-wordle/api?cmd=stats%s";

    // i.e: "http://lwhs.westus2.cloudapp.azure.com:8080/web-wordle/api?sid=8C537D99&cmd=close"
    private static final String _URL_CLOSE = "http://lwhs.westus2.cloudapp.azure.com:8080/web-wordle/api?sid=%s&cmd=close";

    // i.e: "http://lwhs.westus2.cloudapp.azure.com:8080/web-wordle/api?cmd=reset&pwd={wouldn't you like to know it :-)}";
    private static final String _URL_RESET = "http://lwhs.westus2.cloudapp.azure.com:8080/web-wordle/api?cmd=reset&pwd=%s";

    public static void main(String[] args) {
        System.out.println("Hello to WebWorldleClient!");
        Scanner console = new Scanner(System.in);
        System.out.printf("Command or ? for help > ");
        args = console.nextLine().split(" ");
        while(!args[0].equalsIgnoreCase("QUIT")) {
            try {
                if (args[0].equalsIgnoreCase("NEW")) {
                    mainNew(args);
                } else if (args[0].equalsIgnoreCase("CHECK")) {
                    mainCheck(args);
                } else if (args[0].equalsIgnoreCase("REVEAL")) {
                    mainReveal(args);
                } else if (args[0].equalsIgnoreCase("STATS")) {
                    mainStats(args);
                } else if (args[0].equalsIgnoreCase("CLOSE")) {
                    mainClose(args);
                } else if (args[0].equalsIgnoreCase("SERVER")) {
                    mainServer(args);
                } else if (args[0].equalsIgnoreCase("RESET")) {
                    mainReset(args);
                } else if (args[0].equalsIgnoreCase("?")) {
                    mainHelp(args);
                } else {
                    throw new RuntimeException("Unrecognized command!");
                }
            } catch (Exception e) {
                System.out.println("##EXC###: " + e.getMessage());
            }
        
            System.out.printf("\nCommand or ? for help > ");
            args = console.nextLine().split(" ");
        }
        console.close();
        System.out.println("Goodbye!");
    }

    private static void mainHelp(String[] args) {
        System.out.println("    new           - starts a new Wordle game session.");
        System.out.println("    check {WWORD} - checks a wordle {WWORD} in the current game session." );
        System.out.println("    reveal        - reveals the secret {WWORD} in the current game session.");
        System.out.println("    stats         - gives statistics of the current game session.");
        System.out.println("    close         - closes the current Wordle game session.");
        System.out.println("    server        - gives server statistics across all sessions.");
        System.out.println("    reset {PWD}   - !resets the server state (deletes all sessions)! **requires password**");
        System.out.println("    ?             - list of available commands.");
        System.out.println("    quit          - quits the program.");
    }

    /**
     * Creates a new Wordle session.
     * @param args - args[0] is the command "NEW", args[1] (optional) is the name of the session.
     * @throws IOException 
     * @throws URISyntaxException 
     */
    private static void mainNew(String[] args) throws IOException, URISyntaxException {
        if (_sessionID != null) {
            throw new RuntimeException("Current Wordle session needs to be closed first!");
        }
        String name = (args.length < 2) ? "" : String.format("&name=%s", args[1]);
        String urlNew = String.format(_URL_NEW, name);
        AnswerSession answerSession = new AnswerSession(getAnswer(urlNew));
        System.out.println(answerSession);
        _sessionID = answerSession.getSessionId();
    }

    /**
     * Checks a Wordle word in the context of the current game session.
     * @param args - args[0] is the command "CHECK", args[1] is the word to be checked.
     * @throws IOException 
     * @throws URISyntaxException 
     */
    private static void mainCheck(String[] args) throws IOException, URISyntaxException {
        if (args.length != 2) {
            throw new RuntimeException("Missing {WWORD} argument!");
        }
        if (_sessionID == null) {
            throw new RuntimeException("New Wordle session needs to be created first!");
        }
        String word = args[1].toUpperCase();
        String urlCheck = String.format(_URL_CHECK, _sessionID, word);
        AnswerGame answerGame = new AnswerGame(getAnswer(urlCheck), false);
        System.out.println(answerGame);
    }

     /**
     * Reveals the secret Wordle word of the current game session.
     * @param args - args[0] is the command "REVEAL".
     * @throws IOException 
     * @throws URISyntaxException 
     */
    private static void mainReveal(String[] args) throws IOException, URISyntaxException {
        if (_sessionID == null) {
            throw new RuntimeException("New Wordle session needs to be created first!");
        }
        String urlReveal = String.format(_URL_REVEAL, _sessionID);
        AnswerGame answerGame = new AnswerGame(getAnswer(urlReveal), true);
        System.out.println(answerGame);
    }

     /**
     * Prints the statistics of the current game session.
     * @param args - args[0] is the command "STATS".
     * @throws IOException 
     * @throws URISyntaxException 
     */
    private static void mainStats(String[] args) throws IOException, URISyntaxException {
        if (_sessionID == null) {
            throw new RuntimeException("New Wordle session needs to be created first!");
        }
        String sid = String.format("&sid=%s",_sessionID);
        String urlStats = String.format(_URL_STATS, sid);
        AnswerStats answerStats = new AnswerStats(getAnswer(urlStats), false);
        System.out.println(answerStats);
    }

     /**
     * Prints the statistics of the current server.
     * @param args - args[0] is the command "SERVER".
     * @throws IOException 
     * @throws URISyntaxException 
     */
    private static void mainServer(String[] args) throws IOException, URISyntaxException {
        String urlStats = String.format(_URL_STATS, "");
        AnswerStats answerStats = new AnswerStats(getAnswer(urlStats), true);
        System.out.println(answerStats);
    }

    /**
     * Closes the current Wordle session by calling the Web-Wordle server with the 'close' URL.
     * @param args - args[0] is the command "CLOSE"
     * @throws IOException 
     * @throws URISyntaxException 
     */
    private static void mainClose(String[] args) throws IOException, URISyntaxException {
        if (_sessionID == null) {
            throw new RuntimeException("New Wordle session needs to be created first!");
        }
        String urlClose = String.format(_URL_CLOSE, _sessionID);
        AnswerSession answerSession = new AnswerSession(getAnswer(urlClose));
        System.out.println(answerSession);
        _sessionID = null;
    }

     /**
     * Resets the server state. !! Destructive !! Removes all session from the server.
     * Command doesn't require wordle open session but needs a valid password recognized by the server!
     * @param args - args[0] is the command "RESET", args[1] is the mandatory password.
     * @throws IOException 
     * @throws URISyntaxException 
     */
    private static void mainReset(String[] args) throws IOException, URISyntaxException {
        if (args.length != 2) {
            throw new RuntimeException("Missing {PWD} argument!");
        }
        String password = args[1];
        String urlReset = String.format(_URL_RESET, password);
        AnswerStats answerStats = new AnswerStats(getAnswer(urlReset), true);
        System.out.println(answerStats);
        _sessionID = null;
    }

    /**
     * Connects to the Web-Wordle server with the given {urlString} and returns the answer from the request.
     * @param urlString - URL to be used to reach the Web-Wordle server.
     * @return Answer object containing the HTTP status code and the content of the resposne.
     * @throws IOException
     * @throws URISyntaxException 
     */
    private static Answer getAnswer(String urlString) throws IOException, URISyntaxException {
        // establish the connection
        URI uri = new URI(urlString);
        URL url = uri.toURL();
        HttpURLConnection urlCnx = (HttpURLConnection)url.openConnection();
        // get the response status code: 200 means success, 400 means some error (ref: https://en.wikipedia.org/wiki/List_of_HTTP_status_codes)
        int httpCode = urlCnx.getResponseCode();
        // get the correct stream depending on the answer
        InputStream urlStream = httpCode >= 400 ? urlCnx.getErrorStream() : urlCnx.getInputStream();
        // get the content of the response
        Scanner urlScanner = new Scanner(urlStream);
        urlScanner.useDelimiter("\\A");
        String content = urlScanner.next();
        urlScanner.close();
        // create an Answer object and return it
        return new Answer(httpCode, content);
    }
}
 