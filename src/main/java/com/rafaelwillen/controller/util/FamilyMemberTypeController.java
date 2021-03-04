package com.rafaelwillen.controller.util;

import com.rafaelwillen.util.CustomWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class FamilyMemberTypeController extends CustomWindow implements Initializable {

    @FXML
    private AnchorPane topBar;

    @FXML
    private ComboBox<String> memberType_comboBox;

    private String selectedType;

    @Override
    protected boolean validateForm() {
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeWindowDraggable(topBar);
        memberType_comboBox.getItems().addAll(
                "Parente", "Filho", "Animal"
        );
        memberType_comboBox.getSelectionModel().select(0);
    }

    public String getSelectedType() {
        return selectedType;
    }

    @FXML
    void closeWindow(MouseEvent event) {
        selectedType = "";
        closeWindow(topBar, true);
    }

    @FXML
    void finish(ActionEvent event) {
        selectedType = memberType_comboBox.getSelectionModel().getSelectedItem().toUpperCase();
        closeWindow(topBar, true);
    }


}
