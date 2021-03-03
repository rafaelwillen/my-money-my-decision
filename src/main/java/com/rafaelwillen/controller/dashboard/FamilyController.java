package com.rafaelwillen.controller.dashboard;

import com.rafaelwillen.controller.form.create.PetController;
import com.rafaelwillen.controller.form.create.SonController;
import com.rafaelwillen.database.DatabaseManager;
import com.rafaelwillen.model.family.Family;
import com.rafaelwillen.model.family.Parent;
import com.rafaelwillen.model.family.Person;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.RoutesConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
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
    private boolean isParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initData(Family family, Person userLoggedIn) {
        this.family = family;
        this.userLoggedIn = userLoggedIn;
        isParent = userLoggedIn instanceof Parent;

        // TODO: Set parents data
        setFamilyData();
        // TODO: Set the sons data
        // TODO: Set the pets data

        if (!isParent) {
            addPet_button.setDisable(true);
            addSon_button.setDisable(true);
            editFamily_button.setDisable(true);
            editFamily_button.setVisible(false);
            deleteFamily_button.setDisable(true);
        }
    }

    @FXML
    void addPet(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.CREATE_PET_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Criar Animal Estimação");
        PetController controller;
        try {
            stage.setScene(new Scene(loader.load()));
            controller = loader.getController();
            controller.initData(family);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        stage.showAndWait();
        numberPets_label.setText(String.valueOf(family.getPets().size()));
        // TODO: Update the pets data
    }

    @FXML
    void addSon(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.CREATE_SON_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Criar Filho");
        SonController controller;
        try {
            stage.setScene(new Scene(loader.load()));
            controller = loader.getController();
            controller.initData(family);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        stage.showAndWait();
        numberMembers_label.setText(String.valueOf(family.getNumberOfMembers()));
        // TODO: Update the sons data
    }

    @FXML
    void deleteFamily(ActionEvent event) {
        if (confirmDeleteFamily()) {
            TextInputDialog passwordConfirmation = new TextInputDialog();
            passwordConfirmation.setHeaderText("Verificação de credênciais");
            passwordConfirmation.setContentText("Digite a sua password para eliminar a família");
            passwordConfirmation.initModality(Modality.APPLICATION_MODAL);
            Optional<String> textFieldResult = passwordConfirmation.showAndWait();
            if (textFieldResult.isPresent() && textFieldResult.get().equals(userLoggedIn.getPassword())) {
                DatabaseManager.deleteDatabase();
                System.exit(0);
            } else {
                AlertManager.showWarningAlert("Password Invalida", "A password digitada não está correcta");
            }
        }
    }

    @FXML
    void editFamily(MouseEvent event) {
        // TODO: Make the edit family view
        System.out.println("Edit family");
    }

    private boolean confirmDeleteFamily() {
        Alert confirmation = new Alert(Alert.AlertType.WARNING, "Eliminar Família");
        confirmation.setHeaderText("Eliminar Família");
        confirmation.setContentText("Tem a certeza que deseja eliminar esta família? Esta acção é irreversível e apagará todos os dados!");
        ButtonType sim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
        ButtonType nao = new ButtonType("Não", ButtonBar.ButtonData.NO);
        confirmation.getButtonTypes().clear();
        confirmation.getButtonTypes().addAll(sim, nao);
        confirmation.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = confirmation.showAndWait();
        return result.isPresent() && result.get().getButtonData().equals(ButtonBar.ButtonData.YES);
    }

    private void setFamilyData() {
        familyName_label.setText(family.getName());
        HashMap<String, String> address = family.getAddress();
        province_label.setText(address.get(Family.PROVINCE_KEY));
        district_label.setText(address.get(Family.DISTRICT_KEY));
        address_label.setText(address.get(Family.STREET_KEY));
        phoneNumber_label.setText(family.getHousePhoneNumber());
        numberMembers_label.setText(String.valueOf(family.getNumberOfMembers()));
        numberPets_label.setText(String.valueOf(family.getPets().size()));
    }


}
