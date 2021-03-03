package com.rafaelwillen.controller.form.create;

import com.rafaelwillen.database.dao.finance.CostDAO;
import com.rafaelwillen.model.family.*;
import com.rafaelwillen.model.finance.GeneralCost;
import com.rafaelwillen.model.finance.IndividualCost;
import com.rafaelwillen.model.finance.MonthlyPrevision;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.CustomWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CostController extends CustomWindow implements Initializable {


    @FXML
    private AnchorPane topBar;

    @FXML
    private Label windowTitle_label;

    @FXML
    private TextField name_textField;

    @FXML
    private TextField value_textField;

    @FXML
    private DatePicker creationDate_datePicker;

    @FXML
    private TextField local_textField;

    @FXML
    private ComboBox<Object> members_comboBox;

    @FXML
    private TextArea description_textArea;

    private MonthlyPrevision prevision;
    private Family family;
    private boolean isGeneralCost;


    @Override
    protected boolean validateForm() {
        LocalDate creationDate = creationDate_datePicker.getValue();
        String local = local_textField.getText();

        if (local.isEmpty() || value_textField.getText().isEmpty()) {
            AlertManager.showWarningAlert("Campos Vazios", "Preencha todos os campos obrigatorios");
            return false;
        }

        if (!isGeneralCost && members_comboBox.getValue() == null) {
            AlertManager.showWarningAlert("Membro da Família Não Selecionado", "Selecione um membro da família");
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
        isGeneralCost = false;
    }

    public void initData(MonthlyPrevision prevision, Family family) {
        this.prevision = prevision;
        this.family = family;
        members_comboBox.setDisable(true);
        name_textField.setText(prevision.getName());
        windowTitle_label.setText("Adicionar Gasto - Família");
        isGeneralCost = true;
    }

    public void initData(MonthlyPrevision prevision, Son[] sons) {
        this.prevision = prevision;
        windowTitle_label.setText("Adicionar Gasto - Filho");
        name_textField.setText(prevision.getName());
        populatePersonComboBox(sons);
    }

    public void initData(MonthlyPrevision prevision, Parent[] parents) {
        this.prevision = prevision;
        windowTitle_label.setText("Adicionar Gasto - Parente");
        name_textField.setText(prevision.getName());
        populatePersonComboBox(parents);
    }

    public void initData(MonthlyPrevision prevision, Pet[] pets) {
        this.prevision = prevision;
        windowTitle_label.setText("Adicionar Gasto - Animal de Estimação");
        name_textField.setText(prevision.getName());
        members_comboBox.getItems().addAll(pets);
        members_comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Object o) {
                Pet pet = (Pet) o;
                return String.format("%s - %s", pet.getName(), pet.getAnimalType());
            }

            @Override
            public Object fromString(String s) {
                return null;
            }
        });
    }

    @FXML
    void closeWindow(MouseEvent event) {
        closeWindow(topBar, true);
    }

    @FXML
    void create(ActionEvent event) {
        if (!validateForm() || !addCost()) return;
        closeWindow(topBar, true);
    }

    private boolean addCost() {
        try {
            return isGeneralCost ? addGeneralCost() : addIndividualCost();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            AlertManager.showErrorAlert("Erro na inserção", "Ocorreu um erro na inserção do parente, tente novamente");
            return false;
        }
    }

    private boolean addIndividualCost() throws SQLException {
        String name = name_textField.getText();
        LocalDate creationDate = creationDate_datePicker.getValue() == null ? LocalDate.now() : creationDate_datePicker.getValue();
        String local = local_textField.getText();
        double value = Double.parseDouble(value_textField.getText());
        String description = description_textArea.getText();
        Object selectedMember = members_comboBox.getSelectionModel().getSelectedItem();

        IndividualCost cost = new IndividualCost(CostDAO.getInstance().getLastID() + 1, name, value, creationDate);
        cost.setLocal(local);
        cost.setDescription(description);

        if (selectedMember instanceof Person) {
            CostDAO.getInstance().add(cost, (Person) selectedMember, prevision.getId());
            ((Person) selectedMember).addCost(cost);
        } else {
            CostDAO.getInstance().add(cost, (Pet) selectedMember, prevision.getId());
            ((Pet) selectedMember).addCost(cost);
        }
        prevision.addCost(cost);
        return true;
    }

    private boolean addGeneralCost() throws SQLException {
        String name = name_textField.getText();
        LocalDate creationDate = creationDate_datePicker.getValue() == null ? LocalDate.now() : creationDate_datePicker.getValue();
        String local = local_textField.getText();
        double value = Double.parseDouble(value_textField.getText());
        String description = description_textArea.getText();

        GeneralCost cost = new GeneralCost(CostDAO.getInstance().getLastID() + 1, name, value, creationDate);
        cost.setLocal(local);
        cost.setDescription(description);
        CostDAO.getInstance().add(cost, prevision.getId());
        prevision.addCost(cost);
        family.addCost(cost);
        return true;
    }

    private void populatePersonComboBox(Person[] people) {
        members_comboBox.getItems().addAll(people);
        members_comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Object o) {
                Person person = (Person) o;
                return String.format("%s - %s", person.getName(), person.getUsername());
            }

            @Override
            public Object fromString(String s) {
                return null;
            }
        });
    }
}
