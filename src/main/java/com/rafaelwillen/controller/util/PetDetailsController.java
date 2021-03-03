package com.rafaelwillen.controller.util;

import com.rafaelwillen.model.family.Pet;
import com.rafaelwillen.model.family.Sex;
import com.rafaelwillen.util.CustomWindow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PetDetailsController extends CustomWindow implements Initializable {

    @FXML
    private AnchorPane topBar;

    @FXML
    private Label name_label;

    @FXML
    private Label birthDate_label;

    @FXML
    private Label animalType_label;

    @FXML
    private Label sex_label;

    @FXML
    void closeWindow(MouseEvent event) {
        closeWindow(topBar, true);
    }

    @Override
    protected boolean validateForm() {
        try {
            throw new IllegalAccessException("This method must not be called");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeWindowDraggable(topBar);
    }

    public void initData(Pet pet) {
        name_label.setText(pet.getName());
        animalType_label.setText(pet.getAnimalType());
        birthDate_label.setText(pet.getBirthDate().toString());
        if (pet.getSex().equals(Sex.MASCULINO)) {
            sex_label.setText("Macho");
        } else {
            sex_label.setText("Fêmea");
        }
    }
}
