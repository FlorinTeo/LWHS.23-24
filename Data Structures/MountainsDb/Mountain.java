package MountainsDb;

import java.util.Scanner;

public class Mountain {
    // Country, Type, Mountain, Latitude, Longitude, Elevation
    private String _record;
    private String _country;
    private String _type;
    private String _name;
    private double _latitude;
    private double _longitude;
    private double _elevationInFt;

    public Mountain(String record) {
        _record = record;

        String[] fields = _record.split("\t");
        if (fields.length != 6) {
            throw new RuntimeException("Invalid # of fields");
        }

        // Load class fields and throw exceptions if they are invalid
        _country = fields[0];
        _type = fields[1];
        _name = fields[2];
        _latitude = parseLatitude(fields[3]);
        _longitude = parseLongitude(fields[4]);
        _elevationInFt = parseElevation(fields[5]);
    }

    //#region private utility methods
    private static double parseLatitude(String latStr) {
        double latValue = Double.parseDouble(latStr);
        if (latValue < -90 || latValue > 90) {
            throw new RuntimeException("Invalid latitude value!");
        }
        return latValue;
    }

    private static double parseLongitude(String lonStr) {
        double lonValue = Double.parseDouble(lonStr);
        if (lonValue < -180 || lonValue > 180) {
            throw new RuntimeException("Invalid longitude value!");
        }
        return lonValue;
    }

    private static double parseElevation(String elevStr) {
        Scanner parser = new Scanner(elevStr);
        double elevationValue = parser.nextDouble();
        String elevationUnit = parser.hasNext() ? parser.next().toLowerCase() : "m";
        parser.close();
        if (elevationValue == 0 || !elevationUnit.equals("m") && !elevationUnit.equals("ft")) {
            throw new RuntimeException("Invalid elevation unit!");
        }
        if (elevationUnit.equals("m")) {
            elevationValue *= 3.2808399;
        }
        return elevationValue;
    }
    //#endregion private utility methods

    //#region accessors
    public String getCountry() {
        return _country;
    }

    public String getType() {
        return _type;
    }

    public String getName() {
        return _name;
    }
    
    public double getLongitude() {
        return _longitude;
    }

    public double getLatitude() {
        return _latitude;
    }

    public double getElevationInFt() {
        return _elevationInFt;
    }
    //#endregion accessors

    @Override
    public String toString() {
        return _record;
    }
}
