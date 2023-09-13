package Demo;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyClassTests {

    private MyClass myObj;

    @Before
    public void setupTests() {
        System.out.println("setupTests");
        myObj = new MyClass(13);
    }

     @Test
    public void testConstructor() {
        System.out.println("testConstructor");
        System.out.println(myObj.getValue());
        // verify that what I get out of getValue() call is the value 13
        assertEquals(13, myObj.getValue());
    }

    @Test
    public void testSetter() {
        System.out.println("testSetter");
        myObj.setValue(33);
        assertEquals(33, myObj.getValue());
    }

    @After
    public void cleanupTests() {
        System.out.println("cleanupTests");
    }
}
