package jfdi.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class HelpItem extends HBox {

    @FXML
    private Label description;
    @FXML
    private Label command;

    public HelpItem(String description, String command) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    Constants.URL_HELP_PATH));
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.description.setText(description);
        this.command.setText(command);
    }
}
