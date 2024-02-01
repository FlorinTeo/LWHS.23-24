package Hashes.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Program {
    private static final String _link = "https://www.gutenberg.org/cache/epub/69760/pg69760.txt";
    private static final String _specialChars = "~!@#$%^&*()_+`-={}[]|\\:\";'<>?,./“”™";

    public static String normalize(String word) {
        word = word.trim();
        int i = 0;
        while (i < word.length() && _specialChars.indexOf(""+word.charAt(i)) != -1) {
            i++;
        }
        int j = word.length()-1;
        while(j > i && _specialChars.indexOf(""+word.charAt(j)) != -1) {
            j--;
        }
        return word.substring(i, j+1).toUpperCase();
    }
    
    public static void main(String[] args) throws IOException {
        HashStringSet hss = new HashStringSet();
        
        URL url = new URL(_link);
        URLConnection urlCnx = url.openConnection();
        InputStream urlStream = urlCnx.getInputStream();
        Scanner urlScan = new Scanner(urlStream);
        while(urlScan.hasNext()) {
            hss.add(normalize(urlScan.next()));
        }
        
        System.out.println(hss.size());
        System.out.println(hss.buckets());
        System.out.println(hss.loadFactor());
        System.out.println(hss.memoryFactor());
        
        urlScan.close();
    }

}
