package jfdi.ui;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author Liu Xinan
 */
public class AutoCompleteTextField extends TextField {

    private SortedSet<String> keywords;
    private LinkedList<String> results;

    private ContextMenu popup;

    public AutoCompleteTextField() {
        super();
        keywords = new TreeSet<>(String::compareToIgnoreCase);
        results = new LinkedList<>();
        popup = new ContextMenu();

        textProperty().addListener((observable, oldValue, newValue) -> {
            if (getText().isEmpty()) {
                popup.hide();
                return;
            }
            results.clear();
            results.addAll(keywords.subSet(getText(), getText() + Character.MAX_VALUE));
            if (results.isEmpty()) {
                popup.hide();
            } else {
                populatePopup(results);
            }
        });

        focusedProperty().addListener((observable, oldValue, newValue) -> popup.hide());

        popup.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            @SuppressWarnings("deprecation")
            boolean isAnyItemSelected = popup.getItems().stream().map(item -> item.impl_styleableGetNode())
                    .anyMatch(node -> node.isFocused());
            if (event.getCode() == KeyCode.ENTER && !isAnyItemSelected) {
                UI.getInstance().triggerEnter();
                results.clear();
            }
        });
    }

    public void setKeywords(SortedSet<String> keywords) {
        this.keywords.clear();
        this.keywords.addAll(keywords);
    }

    public void hidePopup() {
        popup.hide();
    }

    public void selectFirst() {
        if (!results.isEmpty()) {
            select(results.get(0));
            popup.hide();
        }
    }

    private void select(String suggestion) {
        setText(suggestion + " ");
        this.positionCaret(this.getText().length());
    }

    private void populatePopup(LinkedList<String> results) {
        List<CustomMenuItem> menuItems = results.stream()
            .map(Label::new)
            .map(label -> {
                CustomMenuItem menuItem = new CustomMenuItem(label, true);
                menuItem.setOnAction(action -> {
                    select(label.getText());
                    popup.hide();
                });
                return menuItem;
            })
            .collect(Collectors.toCollection(LinkedList::new));

        popup.getItems().setAll(menuItems);

        if (!popup.isShowing()) {
            popup.show(this, Side.BOTTOM, 0, 0);
        }
    }

}
