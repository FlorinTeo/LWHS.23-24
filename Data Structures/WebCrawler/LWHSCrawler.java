package WebCrawler;
import edu.uci.ics.crawler4j.crawler.*;
import edu.uci.ics.crawler4j.url.*;
import edu.uci.ics.crawler4j.parser.*;

import java.util.Set;
import java.util.regex.Pattern;

public class LWHSCrawler extends WebCrawler {
    private final static Pattern EXCLUSIONS = Pattern.compile(".*(\\.(css|js|xml|gif|jpg|png|mp3|mp4|zip|gz|pdf))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String urlString = url.getURL().toLowerCase();
        return !EXCLUSIONS.matcher(urlString).matches() 
          && urlString.startsWith("https://lwhs.lwsd.org/");
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String title = htmlParseData.getTitle();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            // do something with the collected data
        }
    }
}
