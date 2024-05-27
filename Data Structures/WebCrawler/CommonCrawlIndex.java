package WebCrawler;

import com.google.gson.annotations.SerializedName;
/**
 * Class modeling an index record in CommonCrawl, which can be retrieved this URL:
 * https://index.commoncrawl.org/collinfo.json
 * The structure of the record is as follows:
 * <pre>
 * {
 *      "id": "CC-MAIN-2024-18",
 *      "name": "April 2024 Index",
 *      "timegate": "https://index.commoncrawl.org/CC-MAIN-2024-18/",
 *      "cdx-api": "https://index.commoncrawl.org/CC-MAIN-2024-18-index"
 * }
 * </pre>
 * Prompt for Copilot/GPT for more information:<p><i>
 * "I'm scraping CommonCrawl with a URL like this: https://index.commoncrawl.org/collinfo.json.  I'm getting JSON objects with fields
 * such as id, name, timegate, cdx-api. What does each of the mean?"</i></p>
 */
public class CommonCrawlIndex {
    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("timegate")
    public String timegate;

    @SerializedName("cdx-api")
    public String cdxapi;

    @Override
    public String toString() {
        String output = "";
        output += "Index entry: " + id + "\n";
        output += "  CommonCrawl name: " + name + "\n";
        output += "  with interactive webpage: " + timegate + "\n";
        output += "  and API-level access: " + cdxapi + "\n";
        return output;
    }
}
