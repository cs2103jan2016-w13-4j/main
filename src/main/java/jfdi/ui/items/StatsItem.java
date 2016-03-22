package jfdi.ui.items;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import jfdi.ui.Constants;

public class StatsItem extends HBox {

    public String name;

    @FXML
    private Label statsName;

    @FXML
    private Label statsNum;

    public StatsItem(String name) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    Constants.URL_STATS_PATH));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.name = name;
        statsName.setText(this.name);
        statsNum.setText(String.valueOf(0));
    }

    public void setNum(int num) {
        statsNum.setText(String.valueOf(num));
    }
}
