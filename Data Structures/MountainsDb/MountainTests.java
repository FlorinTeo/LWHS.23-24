package MountainsDb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import org.junit.Test;

public class MountainTests {

    @Test
    public void testConstructor() {
        new Mountain("Aruba	Mountain	Jamanota	12.4899966	-69.9399918	188 m");
        try {
            new Mountain("Italy	Mountain	Cimon della Pala    46.287224	11.820728	3185 m");
            fail();
        } catch (RuntimeException e) {
            assertEquals("Invalid # of fields", e.getMessage());
        }
    }

    @Test
    public void testConstructor_fancy() {
        new Mountain("Aruba	Mountain	Jamanota	12.4899966	-69.9399918	188 m");
        Exception e = assertThrows(RuntimeException.class, () -> {
            new Mountain("Italy	Mountain	Cimon della Pala    46.287224	11.820728	3185 m");
        });
        assertEquals("Invalid # of fields", e.getMessage());
    }

    @Test
    public void testElevation() {
        Mountain mtRainier = new Mountain("United States	Volcano	Mount Rainier	46.8528543410397	-121.760530471802	4392 m");
        assertEquals(14409.4488408, mtRainier.getElevationInFt(), 0);
    }
}
