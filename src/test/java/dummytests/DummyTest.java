//@@author A0121621Y
package dummytests;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import dummy.Dummy;

/**
 * DummyTest checks whether the test utility works
 *
 * @author Kai Yuan
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
