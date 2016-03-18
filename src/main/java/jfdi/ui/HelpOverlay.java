package jfdi.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

public class HelpOverlay {

    @FXML
    private Label description;
    @FXML
    private Label command;

    public HelpOverlay(String description, String command) {

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
