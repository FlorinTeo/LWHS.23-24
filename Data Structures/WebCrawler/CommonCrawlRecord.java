package WebCrawler;

import com.google.gson.annotations.SerializedName;

/**
 * Class modeling a website record in CommonCrawl, which can be retrieved from a URL like below:
 * https://index.commoncrawl.org/CC-MAIN-2024-18-index?url=*.lwhs.org&output=json
 * The structure of the record is as follows:
 * {
 *    "urlkey": "org,lwsd,lwhs)/",
 *    "timestamp": "20240416131624",
 *    "url": "https://lwhs.lwsd.org/",
 *    "mime": "text/html",
 *    "mime-detected": "text/html",
 *    "status": "200",
 *    "digest": "JKSPMFLW7ZCZGOUAJYRSNNM4TC34XMPS",
 *    "length": "19585",
 *    "offset": "350044700",
 *    "filename": "crawl-data/CC-MAIN-2024-18/segments/1712296817095.3/warc/CC-MAIN-20240416124708-20240416154708-00521.warc.gz",
 *    "languages": "eng",
 *    "encoding": "UTF-8"
 * }
 * 
 * Prompt for CoPilot/GPT for more information:
 * "I'm scraping CommonCrawl with a URL like this: https://index.commoncrawl.org/CC-MAIN-2024-18-index?url=*.lwhs.org&output=json.
 * I'm getting JSON objects with fields such as urlkey, timestamp, url, mime, mime-detected, status, digest, length, offset,
 * filename, languages and encoding. What is their meaning?"
 */
public class CommonCrawlRecord {
    public String urlKey;
    public String timestamp;
    public String url;
    public String mime;
    @SerializedName("mime-detected")
    public String mimeDetected;
    public String status;
    public String digest;
    public String length;
    public String offset;
    public String filename;
    public String languages;
    public String encoding;

    @Override
    public String toString() {
      String output = "";
      output += "Website URL: " + url + "\n";
      output += "  indexed at (YYYMMDDHHMMSS): " + timestamp + "\n";
      output += "  is " + (status.equals("200") ? "active" : "broken (" + status + ")") + "\n";
      output += "  has " + length + " bytes\n";
      return output;
    }
}
