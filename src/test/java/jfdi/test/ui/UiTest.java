package jfdi.test.ui;

public abstract class UiTest {

    protected TestMain main;

    UiTest(TestMain main) {
        this.main = main;
    }

    abstract void run();

}
