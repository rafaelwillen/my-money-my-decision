package com.rafaelwillen.controller.dashboard;

import com.rafaelwillen.model.family.Family;
import com.rafaelwillen.model.family.Parent;
import com.rafaelwillen.model.family.Person;
import com.rafaelwillen.model.finance.Cost;
import com.rafaelwillen.model.finance.Income;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
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
    private TableView<String> transactions_table;

    @FXML
    private TableColumn<String, String> transactionName_column;

    @FXML
    private TableColumn<String, String> transactionType_column;

    @FXML
    private TableColumn<String, String> value_column;

    @FXML
    private TableColumn<String, String> memberName_colum;

    @FXML
    private TableColumn<String, String> date_column;

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

        loadQuickFinanceCard();
    }

    private void loadQuickFinanceCard() {
        double monthlyBalance;
        double monthlyIncome = 0;
        double monthlyCost = 0;

        // Incomes
        if (family.getFather() != null) {
            monthlyIncome += family.getFather().getIncomes().stream().filter(income ->
                    sameMonthAndYear(income.getAddedDate())).mapToDouble(Income::getValue).sum();
            monthlyCost += family.getFather().getCosts().stream().filter(cost ->
                    sameMonthAndYear(cost.getAddedDate())).mapToDouble(Cost::getValue).sum();
        }
        if (family.getMother() != null) {
            monthlyIncome += family.getMother().getIncomes().stream().filter(income ->
                    sameMonthAndYear(income.getAddedDate())).mapToDouble(Income::getValue).sum();
            monthlyCost += family.getMother().getCosts().stream().filter(cost ->
                    sameMonthAndYear(cost.getAddedDate())).mapToDouble(Cost::getValue).sum();
        }
        monthlyIncome += family.getSons().stream().flatMap(son -> son.getIncomes().stream()).filter(income ->
                sameMonthAndYear(income.getAddedDate())).mapToDouble(Income::getValue).sum();

        // Costs
        monthlyCost = family.getCosts().stream().filter(cost ->
                sameMonthAndYear(cost.getAddedDate())).mapToDouble(Cost::getValue).sum();
        monthlyCost += family.getSons().stream().flatMap(son -> son.getCosts().stream()).filter(cost ->
                sameMonthAndYear(cost.getAddedDate())).mapToDouble(Cost::getValue).sum();
        monthlyCost += family.getPets().stream().flatMap(pet -> pet.getCosts().stream()).filter(cost ->
                sameMonthAndYear(cost.getAddedDate())).mapToDouble(Cost::getValue).sum();

        monthlyBalance = monthlyIncome - monthlyCost;

        monthlyGain_label.setText(monthlyIncome + " kzs");
        monthlyPrevision_label.setText(monthlyCost + " kzs");
        monthlyBalance_label.setText(monthlyBalance + " kzs");
    }

    private boolean sameMonthAndYear(LocalDate date) {
        return date.getMonth().equals(LocalDate.now().getMonth()) && date.getYear() == LocalDate.now().getYear();
    }
}
