package MountainsDb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MountainsDb {

    public static void main(String[] args) throws IOException
    {
        System.out.println("Hello to MountainsDb program!");
        File crtDir = new File(".");
        System.out.printf("Current folder: %s\n", crtDir.getAbsolutePath());

        // open the database file and print its size
        File dbFile = new File("./MountainsDb/mountains_db.tsv");
        System.out.printf("MountainsDb statistics:\n");
        System.out.printf("    Original file size: %,d bytes\n", dbFile.length());

        // create database reader
        FileInputStream fiStream = new FileInputStream(dbFile);
        InputStreamReader isReader = new InputStreamReader(fiStream, StandardCharsets.UTF_8);
        BufferedReader bReader = new BufferedReader(isReader);

        // create clean and err database
        Path cleanPath = Paths.get("./MountainsDb/mountains_clean.tsv");
        BufferedWriter bCleanWriter = Files.newBufferedWriter(cleanPath, StandardCharsets.UTF_8);
        Path errPath = Paths.get("./MountainsDb/mountains_err.tsv");
        BufferedWriter bErrWriter = Files.newBufferedWriter(errPath, StandardCharsets.UTF_8);

        // iterate through each record in the database (lines in the file)
        long nValidLines = 0;
        long nInvalidLines = 0;
        String line = bReader.readLine();
        Mountain tallestMtn = null;
        Mountain smallestMtn = null;
        while(line != null) {
            try {
                Mountain mtn = new Mountain(line);
                nValidLines++;
                bCleanWriter.write(line + "\n");
                if (smallestMtn == null || smallestMtn.getElevationInFt() > mtn.getElevationInFt()) {
                    smallestMtn = mtn;
                }
                if (tallestMtn == null || tallestMtn.getElevationInFt() < mtn.getElevationInFt()) {
                    tallestMtn = mtn;
                }
            } catch (Exception e) {
                nInvalidLines++;
                bErrWriter.write(line + "\n");
            }
            line = bReader.readLine();
        }
        bReader.close();
        bCleanWriter.close();
        bErrWriter.close();

        // print the number of lines in the database
        System.out.printf("    Valid lines: %,d\n", nValidLines);
        System.out.printf("    Invalid lines: %,d\n", nInvalidLines);
        System.out.printf("    Smallest mountain: %s @ %f ft\n", smallestMtn.getName(), smallestMtn.getElevationInFt());
        System.out.printf("    Tallest mtn: %s @ %f ft\n", tallestMtn.getName(), tallestMtn.getElevationInFt());
        System.out.printf("    Clean file size: %,d bytes\n", Files.size(cleanPath));
        System.out.printf("    Err file size: %,d bytes\n", Files.size(errPath));

        System.out.println("Goodbye!");
    }
}
