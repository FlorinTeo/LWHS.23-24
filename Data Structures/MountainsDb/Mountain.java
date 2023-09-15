package MountainsDb;

import java.util.Scanner;

public class Mountain {
    private String _record;
    private double _elevationInFt;

    public Mountain(String record) {
        _record = record;

        String[] fields = _record.split("\t");
        if (fields.length != 6) {
            throw new RuntimeException("Invalid # of fields");
        }

        Double.parseDouble(fields[3]);
        Double.parseDouble(fields[4]);
        parseElevation(fields[5]);
    }

    private void parseElevation(String elevStr) {
        Scanner parser = new Scanner(elevStr);
        _elevationInFt = parser.nextDouble();
        if (!parser.hasNext() || parser.next().equalsIgnoreCase("m")) {
            parser.close();
            _elevationInFt *= 3.2808;
        } else {
            parser.close();
            throw new RuntimeException("Invalid elevation");
        }
    }

    public double getElevation() {
        return _elevationInFt;
    }
}
