package com.rafaelwillen.controller.dashboard;

import com.rafaelwillen.controller.form.create.IncomeController;
import com.rafaelwillen.controller.form.create.MonthlyPrevisionController;
import com.rafaelwillen.database.dao.finance.CreditDAO;
import com.rafaelwillen.database.dao.finance.IncomeDAO;
import com.rafaelwillen.database.dao.finance.MonthlyPrevisionDAO;
import com.rafaelwillen.model.family.Family;
import com.rafaelwillen.model.family.Parent;
import com.rafaelwillen.model.family.Person;
import com.rafaelwillen.model.finance.Credit;
import com.rafaelwillen.model.finance.Income;
import com.rafaelwillen.model.finance.MonthlyPrevision;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.RoutesConstants;
import com.rafaelwillen.util.tableViewModel.IncomeModel;
import com.rafaelwillen.util.tableViewModel.PrevisionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.time.Month;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class PrevisionController implements Initializable {

    private final static int DIZIMO_FEE = 10;
    private final static double MINIMUM_INCOME_DIZIMO = 100_000;
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
    @FXML
    private Tooltip dizimoValue_tootlTip;
    @FXML
    private ComboBox<String> month_comboBox;
    @FXML
    private TableView<PrevisionModel> prevision_tableView;
    @FXML
    private TableColumn<PrevisionModel, String> previsionName_column;
    @FXML
    private TableColumn<PrevisionModel, Double> previsionValue_column;

    private Family family;
    private Person userLoggedIn;
    private Person selectedPerson;
    private Credit credit;


    private boolean isParent;
    private double balance = 0;
    private double totalValuePredicted = 0;
    private int selectedMonth;
    private LinkedList<MonthlyPrevision> monthlyPrevisions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dizimoValue_tootlTip.setText("O valor do dízimo equivale a " + DIZIMO_FEE + "% de todos os rendimentos");
        getMonthlyPrevision();
    }

    public void initData(Family family, Person userLoggedIn) {
        this.family = family;
        this.userLoggedIn = userLoggedIn;
        isParent = userLoggedIn instanceof Parent;
        populateComboBox();
        updateIncomeTableViewData();
        updatePrevisionTableViewData();
        updateCredit();
    }

    @FXML
    void addCredit(ActionEvent event) {
        if (!isParent) {
            AlertManager.showWarningAlert("Operação Inválida", "Apenas um pai ou mãe pode realizar esta operação");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.CREATE_CREDIT_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Criar Crédito");
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        stage.showAndWait();
        updateCredit();
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
        updateIncomeTableViewData();
        updateLabels();
    }

    @FXML
    void payCredit(MouseEvent event) {
        if (!isParent) {
            AlertManager.showWarningAlert("Operação Inválida", "Apenas um pai ou mãe pode realizar esta operação");
            return;
        }
        if (credit.valueToPay() > balance){
            AlertManager.showWarningAlert("Valor Insuficiente", "Não existe valores suficientes para pagar o crédito");
            return;
        }
        try {
            CreditDAO.getInstance().setPaid(credit);
            balance -= credit.valueToPay();
            updateCredit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void changedMember(ActionEvent event) {
        selectedPerson = member_comboBox.getSelectionModel().getSelectedItem();
        updateIncomeTableViewData();
    }

    @FXML
    void changedMonth(ActionEvent actionEvent) {
        selectedMonth = month_comboBox.getSelectionModel().getSelectedIndex();
        updatePrevisionTableViewData();
    }

    @FXML
    void addPrevision(ActionEvent actionEvent) {
        if (!isParent) {
            AlertManager.showWarningAlert("Operação Inválida", "Apenas um pai ou mãe pode realizar esta operação");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.CREATE_MONTHLY_PREVISION_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Criar Previsão Mensal");
        MonthlyPrevisionController controller;
        try {
            stage.setScene(new Scene(loader.load()));
            controller = loader.getController();
            controller.initData(1000);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        stage.showAndWait();
        getMonthlyPrevision();
        updatePrevisionTableViewData();
        updateLabels();
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

        String[] months = {
                "Janeiro", "Fevereiro", "Março", "Abril",
                "Maio", "Junho", "Julho", "Agosto",
                "Setembro", "Outubro", "Novembro", "Dezembro"
        };
        month_comboBox.setItems(FXCollections.observableArrayList(months));
        member_comboBox.getSelectionModel().select(0);
        selectedPerson = member_comboBox.getSelectionModel().getSelectedItem();
        month_comboBox.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        selectedMonth = month_comboBox.getSelectionModel().getSelectedIndex();
    }

    private void updateIncomeTableViewData() {
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

    private void updatePrevisionTableViewData() {
        ObservableList<PrevisionModel> previsionModels = FXCollections.observableArrayList();
        for (MonthlyPrevision monthlyPrevision : monthlyPrevisions) {
            if (monthlyPrevision.getMonth().equals(Month.of(selectedMonth + 1))) {
                PrevisionModel previsionModel = new PrevisionModel(monthlyPrevision);
                previsionModels.add(previsionModel);
            }
        }
        previsionName_column.setCellValueFactory(new PropertyValueFactory<>("PrevisionName"));
        previsionValue_column.setCellValueFactory(new PropertyValueFactory<>("PrevisionValue"));
        prevision_tableView.setItems(previsionModels);
    }

    private void updateCredit() {
        try {
            if (CreditDAO.getInstance().isTableEmpty()) {
                credit = null;
            } else {
                credit = CreditDAO.getInstance().get(CreditDAO.getInstance().getLastID());
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            AlertManager.showErrorAlert("Erro na busca", "Ocorreu um erro na busca do credito, tente novamente");
            credit = null;
        }
        mainPane.getChildren().remove(addCreditPane);
        mainPane.getChildren().remove(payCreditPane);
        if (credit == null) {
            mainPane.getChildren().add(1, addCreditPane);
        } else {
            mainPane.getChildren().add(1, payCreditPane);
            creationDate_label.setText(credit.getResquestDate().toString());
            value_label.setText(String.valueOf(credit.getGrantedValue()));
            fees_label.setText(credit.getFees() + "%");
            returnValue_label.setText(String.valueOf(credit.valueToPay()));
            deadline_label.setText(credit.getDeadline().toString());
            description.setText(credit.getDescription());
        }
        updateLabels();
    }

    private void updateLabels() {
        double totalIncome;
        try {
            totalIncome = IncomeDAO.getInstance().getAll().stream().mapToDouble(Income::getValue).sum();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            AlertManager.showErrorAlert("Erro na base de dados", "Ocorreu um erro na base de dados, tente novamente");
            totalIncome = 0;
        }
        double dizimo;
        if (totalIncome < MINIMUM_INCOME_DIZIMO){
            dizimoValue_label.setText("Valor insuficiente para o dizimo");
            dizimo = 0;
        } else {
            dizimo = totalIncome * DIZIMO_FEE / 100;
            dizimoValue_label.setText(dizimo + "kzs");
        }
        balance = totalIncome - dizimo - totalValuePredicted;
        if (credit != null){
            if (credit.getDeadline().isAfter(LocalDate.now())){
                balance += credit.getGrantedValue();
            } else {
                balance -= credit.valueToPay();
            }
        }
        availableValue_label.setText(balance + "kzs");
    }

    private void getMonthlyPrevision() {
        try {
            monthlyPrevisions = MonthlyPrevisionDAO.getInstance().getAll();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            AlertManager.showErrorAlert("Erro na busca", "Ocorreu um erro na busca do credito, tente novamente");
        }
        for (MonthlyPrevision monthlyPrevision : monthlyPrevisions) {
            totalValuePredicted += monthlyPrevision.getPredictedValue();
        }
    }
}
