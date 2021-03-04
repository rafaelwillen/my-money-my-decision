package com.rafaelwillen.controller.form.create;

import com.rafaelwillen.database.dao.finance.CreditDAO;
import com.rafaelwillen.database.dao.finance.MonthlyPrevisionDAO;
import com.rafaelwillen.model.finance.MonthlyPrevision;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.CustomWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

public class MonthlyPrevisionController extends CustomWindow implements Initializable {
    @FXML
    private AnchorPane topBar;

    @FXML
    private TextField name_textField;

    @FXML
    private ComboBox<String> month_comboBox;

    @FXML
    private Spinner<Double> value_spinner;

    @FXML
    private TextArea description_textArea;

    private double totalIncome;

    @Override
    protected boolean validateForm() {
        String name = name_textField.getText();
        if (name.isEmpty() || value_spinner.getEditor().getText().isEmpty()) {
            AlertManager.showWarningAlert("Campos Vazios", "Preencha todos os campos obrigatorios");
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeWindowDraggable(topBar);
        populateComboBox();
        month_comboBox.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
    }

    public void initData(double totalIncome) {
        this.totalIncome = totalIncome;
        value_spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, totalIncome));
    }

    @FXML
    void closeWindow(MouseEvent event) {
        closeWindow(topBar, true);
    }

    @FXML
    void create(ActionEvent event) {
        if (!validateForm() || !addMonthlyPrevision()) return;
        AlertManager.showSuccessAlert("Dados adicionados", "A previsão mensal, foi adicionada com sucesso!");
        closeWindow(topBar, true);
    }

    private boolean addMonthlyPrevision() {
        String name = name_textField.getText();
        Month month = Month.of(month_comboBox.getSelectionModel().getSelectedIndex() + 1);
        double value = value_spinner.getValue();
        String description = description_textArea.getText();

        if (value > totalIncome) {
            askCredit();
            closeWindow(topBar, true);
            return false;
        } else {
            try {
                MonthlyPrevision monthlyPrevision = new MonthlyPrevision(MonthlyPrevisionDAO.getInstance().getLastID() + 1, name, value, month, LocalDate.now().getYear());
                monthlyPrevision.setDescription(description);
                MonthlyPrevisionDAO.getInstance().add(monthlyPrevision);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                AlertManager.showErrorAlert("Erro na inserção", "Ocorreu um erro na inserção da previsão mensal, tente novamente");
                return false;
            }
        }
        return true;
    }

    private void populateComboBox() {
        String[] months = {
                "Janeiro", "Fevereiro", "Março", "Abril",
                "Maio", "Junho", "Julho", "Agosto",
                "Setembro", "Outubro", "Novembro", "Dezembro"
        };
        month_comboBox.getItems().addAll(months);
    }

    private void askCredit() {
        try {
            if (CreditDAO.getInstance().isTableEmpty()) {
                boolean askCredit = AlertManager.showConfirmationDialog("Valores Insuficientes", "Não existem valores suficiente para esta previsão mensal, deseja pedir um crédito?");
                if (askCredit) {
                    // TODO: Show the credit form here
                }
            } else {
                AlertManager.showWarningAlert("Valores Insuficientes", "Não é possível adicionar uma nova previsão mensal. Não possui valores sufucientes");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            AlertManager.showErrorAlert("Erro na busca", "Ocorreu um erro na busca dos créditos, tente novamente");
        }
    }
}
