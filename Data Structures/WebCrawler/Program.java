package WebCrawler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Program {
    /**
     * Get all the CommonCrawl indexes by connecting to the index server, fetch all the JSON content
     * and deserialize it in an array of CommonCrawlIndex objects.
     * @return array of CommonCrawlIndex objects, each representing an index on commoncrawl website.
     * @throws IOException
     * @see CommonCrawlIndex
     */
    public static CommonCrawlIndex[] getCommonCrawlIndex() throws IOException  {
        URL url = new URL("https://index.commoncrawl.org/collinfo.json");
        URLConnection urlCnx = url.openConnection();
        InputStreamReader urlReader = new InputStreamReader(urlCnx.getInputStream(), StandardCharsets.UTF_8);
        JsonReader jsonReader = new JsonReader(urlReader);
        Gson gson = new Gson();
        CommonCrawlIndex[] ccIndex = gson.fromJson(jsonReader, CommonCrawlIndex[].class);
        return ccIndex;
    }

    /**
     * Get all the websites matching a given pattern in their URLs, from the given CommonCrawl index.
     * @param ccIndexId - CommonCrawl index id to be used (i.e "CC-MAIN-2024-18-index")
     * @param websiteFilter - website URL filter (i.e. "*.lwhs.lwsd.org")
     * @return list of website records matching the filter, each an instance of CommonCrawlRecord class.
     * @throws IOException
     * @see CommonCrawlRecord
     */
    public static ArrayList<CommonCrawlRecord> getCommonCrawlRecords(String ccIndexId, String websiteFilter) throws IOException {
        ArrayList<CommonCrawlRecord> ccRecords = new ArrayList<CommonCrawlRecord>();
        URL url = new URL("https://index.commoncrawl.org/" + ccIndexId + "-index?url=" + websiteFilter + "&output=json");
        URLConnection urlCnx = url.openConnection();
        InputStreamReader urlReader = new InputStreamReader(urlCnx.getInputStream(), StandardCharsets.UTF_8);
        Scanner webInput = new Scanner(urlReader);
        Gson gson = new Gson();
        while(webInput.hasNextLine()) {
            CommonCrawlRecord ccRecord = gson.fromJson(webInput.nextLine(), CommonCrawlRecord.class);
            ccRecords.add(ccRecord);

        }
        webInput.close();
        return ccRecords;
    }

    /**
     * Gets all the URLs embedded in the content of the given website. The webiste page content is an HTML file, which can be read as any regular string of characters.
     * In it, URLs can be identified by the " href=" HTML tag prefixing the link (which is surrounded by double quotes).
     * and are surrounded by double quotes.
     * I.e:  href="/uploaded/favicon-Lake_Washington_HS.ico"
     * @param website - website content to be downloaded and parsed
     * @return Set of unique links within the page
     * @throws IOException
     */
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
        webInput.close();
        return links;
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println("________________________________ Scrape CommonCrawl for indexes _________________________________________________");
        // get all the indexes from CommonCrawl and print them out
        CommonCrawlIndex[] ccIndex = getCommonCrawlIndex();
        for (CommonCrawlIndex ccI : ccIndex) {
            System.out.println(ccI);
        }

        System.out.println("________________________________ Scrape a CommonCrawl indexes for specific sites ________________________________");
        // use the first index (most recent) to look for all the websites with URL matching the pattern *.lwhs.lwsd.org
        CommonCrawlIndex ccI = ccIndex[0];
        String websiteFilter = "*.lwhs.lwsd.org";
        System.out.println("COMMONCRAWLINDEX: " + ccI.name);
        System.out.println("WEBSITE FILTER: " + websiteFilter);
        System.out.println("_________________________________________________________________________________________________________________");
        ArrayList<CommonCrawlRecord> ccRecords = getCommonCrawlRecords(ccI.id, websiteFilter);
        for (CommonCrawlRecord ccR : ccRecords) {
            System.out.println(ccR);
        }

        System.out.println("________________________________ Get links within a specific site ________________________________ ");
        // parse the webpage content of one of those websites to retrieve all the links embedded into the page.
        String website = "https://lwhs.lwsd.org/academics";
        System.out.println("LINKS WITHIN " + website);
        System.out.println("_________________________________________________________________________________________________________________");
        Set<String> links = getLinks(website);
        for (String link : links) {
            System.out.println("    " + link);
        }
    }
}
