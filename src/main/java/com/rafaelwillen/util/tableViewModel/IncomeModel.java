package com.rafaelwillen.util.tableViewModel;

import com.rafaelwillen.model.finance.Income;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class IncomeModel {
    private final SimpleStringProperty incomeName;
    private final SimpleStringProperty incomeLocal;
    private final SimpleObjectProperty<LocalDate> creationDate;
    private final SimpleDoubleProperty value;
    private final Income income;

    public IncomeModel(Income income){
        this.income = income;
        this.incomeName = new SimpleStringProperty(income.getName());
        this.value = new SimpleDoubleProperty(income.getValue());
        this.incomeLocal = new SimpleStringProperty(income.getLocal());
        this.creationDate = new SimpleObjectProperty<>(income.getAddedDate());
    }


    public String getIncomeName() {
        return incomeName.get();
    }

    public SimpleStringProperty incomeNameProperty() {
        return incomeName;
    }

    public void setIncomeName(String incomeName) {
        this.incomeName.set(incomeName);
    }

    public String getIncomeLocal() {
        return incomeLocal.get();
    }

    public SimpleStringProperty incomeLocalProperty() {
        return incomeLocal;
    }

    public void setIncomeLocal(String incomeLocal) {
        this.incomeLocal.set(incomeLocal);
    }

    public LocalDate getCreationDate() {
        return creationDate.get();
    }

    public SimpleObjectProperty<LocalDate> creationDateProperty() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate.set(creationDate);
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

    public Income getIncome() {
        return income;
    }
}
