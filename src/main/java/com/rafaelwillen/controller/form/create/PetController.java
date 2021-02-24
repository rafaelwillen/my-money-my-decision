package com.rafaelwillen.controller.form.create;

import com.rafaelwillen.database.dao.family.PetDao;
import com.rafaelwillen.model.family.Family;
import com.rafaelwillen.model.family.Pet;
import com.rafaelwillen.model.family.Sex;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import com.rafaelwillen.util.CustomWindow;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PetController extends CustomWindow implements Initializable {

    @FXML
    private AnchorPane topBar;

    @FXML
    private TextField name_textField;

    @FXML
    private ComboBox<String> sex_comboBox;

    @FXML
    private TextField animalType_textField;

    @FXML
    private DatePicker birthDate_date;

    private Family family;

    @Override
    protected boolean validateForm() {
        String name = name_textField.getText();
        String animalType = animalType_textField.getText();
        LocalDate birthDate = birthDate_date.getValue();

        if (Validator.isEmpty(name) || Validator.isEmpty(animalType) || birthDate == null) {
            AlertManager.showWarningAlert("Campos Vazios", "Preencha todos os campos obrigatorios");
            return false;
        }
        if (birthDate.isAfter(LocalDate.now())) {
            AlertManager.showWarningAlert("Data Inválida", "A data inserida acontece depois da data de hoje");
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeWindowDraggable(topBar);
        sex_comboBox.getItems().addAll("Macho", "Fêmea");
        sex_comboBox.setValue("Macho");
    }

    public void initData(Family family){
        this.family = family;
    }

    @FXML
    void cancel(ActionEvent event) {
        closeWindow(topBar, true);
    }

    @FXML
    void closeWindow(MouseEvent event) {
        closeWindow(topBar, true);
    }

    @FXML
    void create(ActionEvent event) {
        if (!validateForm() || !addPet()) return;
        AlertManager.showSuccessAlert("Dados adicionados", "O animal de estimação " + name_textField.getText() + ", foi adicionado com sucesso!");
        closeWindow(topBar, true);
    }

    private boolean addPet() {
        String name = name_textField.getText();
        String animalType = animalType_textField.getText();
        LocalDate birthDate = birthDate_date.getValue();
        Sex sex;
        switch (sex_comboBox.getValue()){
            case "Macho": sex = Sex.MASCULINO; break;
            case "Fêmea": sex = Sex.FEMININO; break;
            default:
                throw new IllegalStateException("Unexpected value: " + sex_comboBox.getValue());
        }
        try {
            int newId = PetDao.getInstance().getLastID() + 1;
            Pet pet = new Pet(newId, animalType, name, birthDate, sex);
            PetDao.getInstance().add(pet);
            family.addPet(pet);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            AlertManager.showErrorAlert("Erro na inserção", "Ocorreu um erro na inserção do filho, tente novamente");
            return false;
        }
        return true;
    }
}
