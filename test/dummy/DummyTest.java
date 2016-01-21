package dummy;

import static org.junit.Assert.*;

import org.junit.Test;

import test.Dummy;

/**
 * DummyTest
 * @author Kai Yuan
 * Description: checks whether the test utility works
 */
public class DummyTest {

  /**
   * Test whether the dummy exists
   */
	@Test
	public void testDummy(){
		Dummy myDummy = new Dummy();
		assertTrue(myDummy.exists());
	}

}
