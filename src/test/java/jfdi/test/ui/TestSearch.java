package jfdi.test.ui;

import static org.junit.Assert.assertEquals;

import jfdi.ui.Constants;

public class TestSearch extends UiTest {

    TestSearch(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testSingleSearchDone();
        testMultipleSearchDone();
    }

    /*
     * Test a simple "search" command with one single keyword
     */
    public void testSingleSearchDone() {

        main.addTask("test 1");
        main.addTask("testing 2");
        main.addTask("test 3");
        main.addTask("testing 4");
        main.addTask("test 2");
        main.addTask("testing 3");
        main.addTask("test 4");

        main.execute("search test");

        main.assertResponseMessage(String.format(Constants.CMD_SUCCESS_SEARCH_1, "[test]"));
        assertEquals(4, main.getImportantListSize());
    }

    /*
     * Test a simple "search" command with multiple keywords
     */
    public void testMultipleSearchDone() {

        main.execute("search testing 2");

        main.assertResponseMessage(String.format(Constants.CMD_SUCCESS_SEARCH_2, "2 and testing"));
        assertEquals(4, main.getImportantListSize());

        main.execute("search test testing 2");

        main.assertResponseMessage(String.format(Constants.CMD_SUCCESS_SEARCH_2, "2, test and testing"));
        assertEquals(7, main.getImportantListSize());
    }

}
