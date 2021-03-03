package com.rafaelwillen.controller.dashboard;

import com.rafaelwillen.model.family.Family;
import com.rafaelwillen.model.family.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class FamilyController implements Initializable {
    @FXML
    private VBox parentsCardContainer;

    @FXML
    private Button addPet_button;

    @FXML
    private Button addSon_button;

    @FXML
    private Label editFamily_button;

    @FXML
    private Label familyName_label;

    @FXML
    private Label province_label;

    @FXML
    private Label district_label;

    @FXML
    private Label address_label;

    @FXML
    private Label phoneNumber_label;

    @FXML
    private Label numberMembers_label;

    @FXML
    private Label numberPets_label;

    @FXML
    private Button deleteFamily_button;

    @FXML
    private VBox sonsContainer;

    @FXML
    private VBox petsContainer;

    private Person userLoggedIn;
    private Family family;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initData(Family family, Person userLoggedIn) {
        this.family = family;
        this.userLoggedIn = userLoggedIn;
    }

    @FXML
    void addPet(ActionEvent event) {

    }

    @FXML
    void addSon(ActionEvent event) {

    }

    @FXML
    void deleteFamily(ActionEvent event) {

    }

    @FXML
    void editFamily(MouseEvent event) {

    }
}
