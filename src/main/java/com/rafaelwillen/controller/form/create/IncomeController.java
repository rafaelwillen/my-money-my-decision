package com.rafaelwillen.controller.form.create;

import com.rafaelwillen.database.dao.finance.IncomeDAO;
import com.rafaelwillen.model.family.Family;
import com.rafaelwillen.model.family.Person;
import com.rafaelwillen.model.finance.Income;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.CustomWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class IncomeController extends CustomWindow implements Initializable {

    @FXML
    private AnchorPane topBar;

    @FXML
    private TextField name_textField;

    @FXML
    private DatePicker creationDate_date;

    @FXML
    private TextField local_textField;

    @FXML
    private TextField value_textField;

    @FXML
    private ComboBox<Person> familyMembers_comboBox;

    @FXML
    private TextArea description_textArea;

    private Person selectedPerson;
    private Family family;

    @Override
    protected boolean validateForm() {
        String name = name_textField.getText();
        LocalDate creationDate = creationDate_date.getValue();
        String local = local_textField.getText();
        if (name.isEmpty() || local.isEmpty() || value_textField.getText().isEmpty() || familyMembers_comboBox.getValue() == null) {
            AlertManager.showWarningAlert("Campos Vazios", "Preencha todos os campos obrigatorios");
            return false;
        }
        try {
            Double.parseDouble(value_textField.getText());
        } catch (NumberFormatException ignored) {
            AlertManager.showWarningAlert("Valor Inválido", "Preencha o campo do valor usando números");
            return false;
        }

        if (creationDate != null && creationDate.isAfter(LocalDate.now())) {
            AlertManager.showWarningAlert("Data Inválida", "A data inserida acontece depois da data de hoje");
            return false;
        }
        return true;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeWindowDraggable(topBar);
        makeNumberInput(value_textField);
    }

    public void initData(Family family) {
        this.family = family;
        populateComboBox();
    }

    @FXML
    void closeWindow(MouseEvent event) {
        closeWindow(topBar, true);
    }

    @FXML
    void create(ActionEvent event) {
        if (!validateForm() || !addIncome()) return;
        AlertManager.showSuccessAlert("Dados adicionados", "O rendimento, foi adicionado com sucesso!");
        closeWindow(topBar, true);
    }

    private boolean addIncome() {
        String name = name_textField.getText();
        LocalDate creationDate = creationDate_date.getValue() == null ? LocalDate.now() : creationDate_date.getValue();
        String local = local_textField.getText();
        double value = Double.parseDouble(value_textField.getText());
        String description = description_textArea.getText();
        selectedPerson = familyMembers_comboBox.getSelectionModel().getSelectedItem();
        try {
            Income income = new Income(IncomeDAO.getInstance().getLastID() + 1, name, value, creationDate);
            income.setLocal(local);
            income.setDescription(description);
            IncomeDAO.getInstance().add(income, selectedPerson.getUsername());
            selectedPerson.addIncome(income);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            AlertManager.showErrorAlert("Erro na inserção", "Ocorreu um erro na inserção do parente, tente novamente");
            return false;
        }
        return true;
    }

    private void populateComboBox() {
        if (family.getFather() != null) {
            familyMembers_comboBox.getItems().add(family.getFather());
        }
        if (family.getMother() != null) {
            familyMembers_comboBox.getItems().add(family.getMother());
        }
        familyMembers_comboBox.getItems().addAll(family.getSons());

        familyMembers_comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Person person) {
                return person.getName();
            }

            @Override
            public Person fromString(String s) {
                return null;
            }
        });
    }
}
