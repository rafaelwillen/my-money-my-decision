package com.rafaelwillen.controller.dashboard;

import com.rafaelwillen.database.dao.finance.CostDAO;
import com.rafaelwillen.database.dao.finance.IncomeDAO;
import com.rafaelwillen.model.family.Family;
import com.rafaelwillen.model.family.Parent;
import com.rafaelwillen.model.family.Person;
import com.rafaelwillen.model.finance.Cost;
import com.rafaelwillen.model.finance.GeneralCost;
import com.rafaelwillen.model.finance.Income;
import com.rafaelwillen.model.finance.IndividualCost;
import com.rafaelwillen.util.tableViewModel.TransactionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private Family family;
    private Person userLoggedIn;

    @FXML
    private Text monthlyGain_label;

    @FXML
    private Text monthlyPrevision_label;

    @FXML
    private Text monthlyBalance_label;

    @FXML
    private ImageView parent_image;

    @FXML
    private ImageView son_image;

    @FXML
    private Label name_label;

    @FXML
    private Label userType_label;

    @FXML
    private Label age_label;

    @FXML
    private Label sex_label;

    @FXML
    private TableView<TransactionModel> transactions_table;

    @FXML
    private TableColumn<TransactionModel, String> transactionName_column;

    @FXML
    private TableColumn<TransactionModel, String> transactionType_column;

    @FXML
    private TableColumn<TransactionModel, String> value_column;

    @FXML
    private TableColumn<TransactionModel, LocalDate> date_column;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initData(Person userLoggedIn, Family family) {
        this.family = family;
        this.userLoggedIn = userLoggedIn;

        if (userLoggedIn instanceof Parent) {
            parent_image.setVisible(true);
            userType_label.setText("Parente");
        } else {
            son_image.setVisible(true);
            userType_label.setText("Filho");
        }
        name_label.setText(userLoggedIn.getName());
        age_label.setText(String.valueOf(userLoggedIn.getAge()));
        sex_label.setText(String.valueOf(userLoggedIn.getSex().name().charAt(0)));
        updateTransactionsTable();
        loadQuickFinanceCard();
    }

    private void loadQuickFinanceCard() {
        double monthlyBalance;
        double monthlyIncome = 0;
        double monthlyCost = 0;
        try{
            for (Income income : IncomeDAO.getInstance().getAll()){
                if (income.getAddedDate().getMonth().equals(LocalDate.now().getMonth())){
                    monthlyIncome += income.getValue();
                }
            }
            for (Cost cost : CostDAO.getInstance().getAll()){
                if (cost.getAddedDate().getMonth().equals(LocalDate.now().getMonth())){
                    monthlyCost += cost.getValue();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        monthlyBalance = monthlyIncome - monthlyCost;
        monthlyGain_label.setText(monthlyIncome + " kzs");
        monthlyPrevision_label.setText(monthlyCost + " kzs");
        monthlyBalance_label.setText(monthlyBalance + " kzs");
    }

    private void updateTransactionsTable(){
        ObservableList<TransactionModel> transactionModels = FXCollections.observableArrayList();
        try {
            for (Cost cost : CostDAO.getInstance().getAll()) {
                if (cost.getAddedDate().isAfter(LocalDate.now().minusDays(15))){
                    transactionModels.add(new TransactionModel(cost));
                }
            }
            for (Income income : IncomeDAO.getInstance().getAll()) {
                if (income.getAddedDate().isAfter(LocalDate.now().minusDays(15))){
                    transactionModels.add(new TransactionModel(income));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        transactionModels.sort(Comparator.comparing(TransactionModel::getDate));
        transactionName_column.setCellValueFactory(new PropertyValueFactory<>("Name"));
        transactionType_column.setCellValueFactory(new PropertyValueFactory<>("Type"));
        value_column.setCellValueFactory(new PropertyValueFactory<>("Value"));
        date_column.setCellValueFactory(new PropertyValueFactory<>("Date"));
        transactions_table.setItems(transactionModels);
    }

}
