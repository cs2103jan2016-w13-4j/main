package jfdi.test.storage;

import static org.junit.Assert.assertEquals;

import jfdi.storage.Constants;
import jfdi.storage.data.Alias;

import org.junit.Test;

public class AliasTest {

    @Test
    public void testAliasConstructorAndGetters() {
        Alias alias = new Alias(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        assertEquals(alias.getAlias(), Constants.TEST_ALIAS);
        assertEquals(alias.getCommand(), Constants.TEST_COMMAND);
    }

}
