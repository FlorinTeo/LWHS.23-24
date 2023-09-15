package MountainsDb;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;

public class MountainTests {
    @Test
    public void testConstructor() {
        try {
            new Mountain("Aruba	Mountain	Jamanota	12.4899966	-69.9399918	188 m");
        } catch (Exception e) {
            // valid mountain should not fail!
            fail();
        }

        try {
            new Mountain("Italy	Mountain	Cimon della Pala    46.287224	11.820728	3185 m");
            // invalid mountain shoulod not succeed!
            fail();
        } catch (Exception e) {
            // exception is expected here, test should pass
        }
    }
}
