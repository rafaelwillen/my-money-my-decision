package com.rafaelwillen.controller.util;

import com.rafaelwillen.model.finance.MonthlyPrevision;
import com.rafaelwillen.util.CustomWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.net.PortUnreachableException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class SelectPrevisionController extends CustomWindow implements Initializable {

    @FXML
    private AnchorPane topBar;

    @FXML
    private ComboBox<MonthlyPrevision> monthlyPrevision_comboBox;
    private LinkedList<MonthlyPrevision> previsions;
    private boolean canContinue = true;
    private MonthlyPrevision selectedPrevision;


    @Override
    protected boolean validateForm() {
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void initData(LinkedList<MonthlyPrevision> previsions){
        this.previsions = previsions;
        monthlyPrevision_comboBox.getItems().addAll(previsions);
        monthlyPrevision_comboBox.getSelectionModel().select(0);
        monthlyPrevision_comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MonthlyPrevision prevision) {
                return prevision.getName();
            }

            @Override
            public MonthlyPrevision fromString(String s) {
                return null;
            }
        });
    }

    public boolean canContinue() {
        return canContinue;
    }

    public MonthlyPrevision getSelectedPrevision() {
        return selectedPrevision;
    }

    @FXML
    void closeWindow(MouseEvent event) {
        canContinue = false;
        closeWindow(topBar, true);
    }

    @FXML
    void finish(ActionEvent event) {
        selectedPrevision = monthlyPrevision_comboBox.getValue();
        closeWindow(topBar, true);
    }



}
