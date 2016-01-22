package dummytests;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import dummy.Dummy;

/**
 * DummyTest
 * @author Kai Yuan
 * Description: Checks whether the test utility works
 */
public class DummyTest {

    /**
     * Test whether the dummy exists
     */
    @Test
    public void testDummy() {
        Dummy myDummy = new Dummy();
        assertTrue(myDummy.isPresent());
    }

}
