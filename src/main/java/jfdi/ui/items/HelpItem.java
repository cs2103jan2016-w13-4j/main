package jfdi.ui.items;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import jfdi.ui.Constants;

public class HelpItem extends HBox {

    @FXML
    private Label description;
    @FXML
    private Label command;

    public HelpItem(String des, String cmd) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    Constants.URL_HELP_PATH));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        description.setWrapText(true);
        description.setText(des);
        command.setWrapText(true);
        command.setText(cmd);
    }
}
