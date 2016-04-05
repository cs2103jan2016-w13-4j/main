package jfdi.test.ui;

public class TestMark extends UiTest {

    TestMark(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testMarkDone();
    }

    private void testMarkDone() {
        main.execute("mark 1");
    }

}
