package com.rafaelwillen.util.tableViewModel;

import com.rafaelwillen.model.finance.Cost;
import com.rafaelwillen.model.finance.Income;
import com.rafaelwillen.model.finance.IndividualCost;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class TransactionModel {
    private  SimpleStringProperty name;
    private  SimpleStringProperty type;
    private  SimpleDoubleProperty value;
    private  SimpleObjectProperty<LocalDate> date;
    private final boolean isCost;
    private final boolean isIncome;
    private final Cost cost;
    private final Income income;

    public TransactionModel(Cost cost){
        this.cost = cost;
        income = null;
        this.name = new SimpleStringProperty(cost.getName());
        this.type = new SimpleStringProperty(cost instanceof IndividualCost ? "Gasto Geral" : "Gasto Individual");
        this.value = new SimpleDoubleProperty(cost.getValue());
        this.date = new SimpleObjectProperty<>(cost.getAddedDate());
        isCost = true;
        isIncome = false;
    }

    public TransactionModel(Income income){
        this.income = income;
        cost = null;
        this.name = new SimpleStringProperty(income.getName());
        this.type = new SimpleStringProperty("Rendimento");
        this.value = new SimpleDoubleProperty(income.getValue());
        this.date = new SimpleObjectProperty<>(income.getAddedDate());
        isIncome = true;
        isCost = false;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public double getValue() {
        return value.get();
    }

    public SimpleDoubleProperty valueProperty() {
        return value;
    }

    public void setValue(double value) {
        this.value.set(value);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public boolean isCost() {
        return isCost;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public Cost getCost() {
        return cost;
    }

    public Income getIncome() {
        return income;
    }
}
