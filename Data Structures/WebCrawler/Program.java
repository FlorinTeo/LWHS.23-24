package WebCrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import edu.uci.ics.crawler4j.crawler.*;
import edu.uci.ics.crawler4j.fetcher.*;
import edu.uci.ics.crawler4j.robotstxt.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Program {

    /**
     * trial for crawler4j
     * @see <span>A guide to Crawler4j</span>:
     *      https://baeldung-cn.com/crawler4j
     * @see <span>crawler4j jar</span>:
     *      https://repo1.maven.org/maven2/edu/uci/ics/crawler4j/4.4.0/
     */
    public static void mainCrawler4j() throws Exception {
        File crawlStorage = new File("WebCrawler/temp");
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorage.getAbsolutePath());
        
        int numCrawlers = 1;
        
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer= new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        
        controller.addSeed("https://lwhs.lwsd.org/");
        
        CrawlController.WebCrawlerFactory<LWHSCrawler> factory = LWHSCrawler::new;
        
        controller.start(factory, numCrawlers);
    }

    public static CommonCrawlIndex[] getCommonCrawlIndex() throws IOException  {
        URL url = new URL("https://index.commoncrawl.org/collinfo.json");
        URLConnection urlCnx = url.openConnection();
        InputStreamReader urlReader = new InputStreamReader(urlCnx.getInputStream(), StandardCharsets.UTF_8);
        JsonReader jsonReader = new JsonReader(urlReader);
        Gson gson = new Gson();
        CommonCrawlIndex[] ccIndex = gson.fromJson(jsonReader, CommonCrawlIndex[].class);
        return ccIndex;
    }

    public static ArrayList<CommonCrawlRecord> getCommonCrawlRecords(String ccIndex, String websiteFilter) throws IOException {
        ArrayList<CommonCrawlRecord> ccRecords = new ArrayList<CommonCrawlRecord>();
        URL url = new URL(ccIndex + "?url=" + websiteFilter + "&output=json");
        URLConnection urlCnx = url.openConnection();
        InputStreamReader urlReader = new InputStreamReader(urlCnx.getInputStream(), StandardCharsets.UTF_8);
        Scanner webInput = new Scanner(urlReader);
        Gson gson = new Gson();
        while(webInput.hasNextLine()) {
            CommonCrawlRecord ccRecord = gson.fromJson(webInput.nextLine(), CommonCrawlRecord.class);
            ccRecords.add(ccRecord);

        }
        return ccRecords;
    }

    public static Set<String> getLinks(String website) throws IOException {
        Set<String> links = new HashSet<String>();
        URL url = new URL(website);
        URLConnection urlCnx = url.openConnection();
        // Set the User-Agent header to mimic a browser request
        urlCnx.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0");
        InputStreamReader urlReader = new InputStreamReader(urlCnx.getInputStream(), StandardCharsets.UTF_8);
        Scanner webInput = new Scanner(urlReader);
        webInput.useDelimiter("\\A");
        String content = webInput.next();
        int iHref = content.indexOf(" href=");
        while(iHref != -1) {
            int iLinkStart = content.indexOf("\"", iHref);
            int iLinkEnd = content.indexOf("\"", iLinkStart+1);
            String link = content.substring(iLinkStart+1, iLinkEnd);
            links.add(link);
            iHref = content.indexOf(" href=", iLinkEnd + 1);
        }
        return links;
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println("________________________________ Scrape CommonCrawl for indexes _________________________________________________");
        CommonCrawlIndex[] ccIndex = getCommonCrawlIndex();
        for (CommonCrawlIndex ccI : ccIndex) {
            System.out.println(ccI);
        }

        System.out.println("________________________________ Scrape a CommonCrawl indexes for specific sites ________________________________");
        CommonCrawlIndex ccI = ccIndex[0];
        String websiteFilter = "*.lwhs.lwsd.org";
        System.out.println("COMMONCRAWLINDEX: " + ccI.name);
        System.out.println("WEBSITE FILTER: " + websiteFilter);
        System.out.println("_________________________________________________________________________________________________________________");
        ArrayList<CommonCrawlRecord> ccRecords = getCommonCrawlRecords(ccI.cdxapi, websiteFilter);
        for (CommonCrawlRecord ccR : ccRecords) {
            System.out.println(ccR);
        }

        System.out.println("________________________________ Get links within a specific site ________________________________ ");
        String website = "https://lwhs.lwsd.org/academics";
        System.out.println("LINKS WITHIN " + website);
        System.out.println("_________________________________________________________________________________________________________________");
        Set<String> links = getLinks(website);
        for (String link : links) {
            System.out.println("    " + link);
        }
    }
}
