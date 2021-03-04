package com.rafaelwillen.controller.form.create;

import com.rafaelwillen.database.dao.finance.CreditDAO;
import com.rafaelwillen.model.finance.Credit;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.CustomWindow;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class CreditController extends CustomWindow implements Initializable {

    @FXML
    private AnchorPane topBar;

    @FXML
    private TextField value_textField;

    @FXML
    private Spinner<Integer> fee_spinner;

    @FXML
    private DatePicker creationDate_datePicker;

    @FXML
    private DatePicker deadlineDate_datePicker;

    @FXML
    private TextArea description_textArea;


    @Override
    protected boolean validateForm() {
        double value = Double.parseDouble(value_textField.getText());
        int fee = Integer.parseInt(fee_spinner.getEditor().getText());
        LocalDate creationDate = creationDate_datePicker.getValue();
        LocalDate deadline = deadlineDate_datePicker.getValue();

        if (value_textField.getText().isEmpty() || fee_spinner.getEditor().getText().isEmpty() || deadline == null){
            AlertManager.showWarningAlert("Campos Vazios", "Preencha todos os campos obrigatorios");
            return false;
        }
        if (fee < 1 || fee > 100){
            AlertManager.showWarningAlert("Juros Inválidos", "O valor dos juros deve ser entre 1 e 100");
            return false;
        }
        if (creationDate != null && creationDate.isAfter(LocalDate.now())){
            AlertManager.showWarningAlert("Data de Criação Inválida", "A data inserida acontece depois da data de hoje");
            return false;
        }
        if (creationDate != null && creationDate.isAfter(deadline)){
            AlertManager.showWarningAlert("Data de Criação Inválida", "A data inserida acontece depois da data do prazo do pagamento");
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeWindowDraggable(topBar);
        fee_spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));
        makeNumberInput(value_textField);
        makeNumberInput(fee_spinner);
    }

    @FXML
    void closeWindow(MouseEvent event) {
        closeWindow(topBar, true);
    }

    @FXML
    void create(ActionEvent event) {
        if (!validateForm() || !addCredit()) return;
        closeWindow(topBar, true);
    }

    private boolean addCredit(){
        double value = Double.parseDouble(value_textField.getText());
        int fee = Integer.parseInt(fee_spinner.getEditor().getText());
        LocalDate creationDate = creationDate_datePicker.getValue();
        LocalDate deadline = deadlineDate_datePicker.getValue();
        String description = description_textArea.getText();

        try {
            Credit credit = new Credit(CreditDAO.getInstance().getLastID() + 1, value, fee, creationDate, deadline);
            credit.setDescription(description);
            CreditDAO.getInstance().add(credit);
            AlertManager.showInformationDialog("Crédito Criado", String.format("Crédito criado com sucesso. Devem ser pagos %.2f kzs na data %s", credit.valueToPay(), credit.getDeadline()));
        } catch (SQLException e){
            System.err.println(e.getMessage());
            AlertManager.showErrorAlert("Erro na inserção", "Ocorreu um erro na inserção do credito, tente novamente");
            return false;
        }
        return true;
    }


}
