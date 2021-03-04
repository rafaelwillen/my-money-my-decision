package com.rafaelwillen.controller.dashboard;

import com.rafaelwillen.controller.form.create.CreditController;
import com.rafaelwillen.controller.form.create.IncomeController;
import com.rafaelwillen.database.dao.finance.CreditDAO;
import com.rafaelwillen.model.family.Family;
import com.rafaelwillen.model.family.Person;
import com.rafaelwillen.model.finance.Credit;
import com.rafaelwillen.model.finance.Income;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.RoutesConstants;
import com.rafaelwillen.util.tableViewModel.IncomeModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;

import java.util.ResourceBundle;
import java.util.Stack;

public class PrevisionController implements Initializable {

    @FXML
    private FlowPane mainPane;

    @FXML
    private TableView<IncomeModel> income_tableView;

    @FXML
    private TableColumn<IncomeModel, String> incomeName_column;

    @FXML
    private TableColumn<IncomeModel, Double> incomeValue_column;

    @FXML
    private TableColumn<IncomeModel, String> incomeLocal_column;

    @FXML
    private TableColumn<IncomeModel, LocalDate> incomeDate_column;

    @FXML
    private ComboBox<Person> member_comboBox;

    @FXML
    private AnchorPane addCreditPane;

    @FXML
    private AnchorPane payCreditPane;

    @FXML
    private Label creationDate_label;

    @FXML
    private Label value_label;

    @FXML
    private Label fees_label;

    @FXML
    private Label returnValue_label;

    @FXML
    private Label deadline_label;

    @FXML
    private Label description;

    @FXML
    private Label availableValue_label;

    @FXML
    private Label dizimoValue_label;

    private Family family;
    private Person userLoggedIn;
    private Person selectedPerson;
    private Credit credit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initData(Family family, Person userLoggedIn){
        this.family = family;
        this.userLoggedIn = userLoggedIn;
        populateComboBox();
        member_comboBox.getSelectionModel().select(0);
        selectedPerson = member_comboBox.getSelectionModel().getSelectedItem();
        setIncomeTableViewData();
        setCredit();
    }

    @FXML
    void addCredit(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.CREATE_CREDIT_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Criar Crédito");
        CreditController controller;
        try {
            stage.setScene(new Scene(loader.load()));
            controller = loader.getController();
            controller.initData(1000);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        stage.showAndWait();
        setCredit();
        // TODO: Update labels
    }

    @FXML
    void addIncome(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.CREATE_INCOME_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Criar Rendimento");
        IncomeController controller;
        try {
            stage.setScene(new Scene(loader.load()));
            controller = loader.getController();
            controller.initData(family);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        stage.showAndWait();
        setIncomeTableViewData();
        // TODO: Update labels
    }

    @FXML
    void pagarCredito(MouseEvent event) {

    }

    @FXML
    void changedMember(ActionEvent event) {
        selectedPerson = member_comboBox.getSelectionModel().getSelectedItem();
        setIncomeTableViewData();
    }

    private void populateComboBox() {
        if (family.getFather() != null) {
            member_comboBox.getItems().add(family.getFather());
        }
        if (family.getMother() != null) {
            member_comboBox.getItems().add(family.getMother());
        }
        member_comboBox.getItems().addAll(family.getSons());

        member_comboBox.setConverter(new StringConverter<>() {
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

    private void setIncomeTableViewData(){
        ObservableList<IncomeModel> incomeModels = FXCollections.observableArrayList();
        for (Income income : selectedPerson.getIncomes()) {
            incomeModels.add(new IncomeModel(income));
        }
        incomeName_column.setCellValueFactory(new PropertyValueFactory<>("IncomeName"));
        incomeLocal_column.setCellValueFactory(new PropertyValueFactory<>("IncomeLocal"));
        incomeDate_column.setCellValueFactory(new PropertyValueFactory<>("CreationDate"));
        incomeValue_column.setCellValueFactory(new PropertyValueFactory<>("Value"));
        income_tableView.setItems(incomeModels);
    }

    private void setCredit(){
        try {
            if (CreditDAO.getInstance().isTableEmpty()){
                credit = null;
            } else {
                credit = CreditDAO.getInstance().get(CreditDAO.getInstance().getLastID());
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            AlertManager.showErrorAlert("Erro na busca", "Ocorreu um erro na busca do credito, tente novamente");
            credit = null;
        };
        mainPane.getChildren().remove(addCreditPane);
        mainPane.getChildren().remove(payCreditPane);
        if (credit == null){
            mainPane.getChildren().add(1, addCreditPane);
        } else {
            mainPane.getChildren().add(1, payCreditPane);
            creationDate_label.setText(credit.getResquestDate().toString());
            value_label.setText(String.valueOf(credit.getGrantedValue()));
            fees_label.setText(credit.getFees()  + "%");
            returnValue_label.setText(String.valueOf(credit.valueToPay()));
            deadline_label.setText(credit.getDeadline().toString());
            description.setText(credit.getDescription());
        }
    }
}
