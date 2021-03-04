package com.rafaelwillen.util.tableViewModel;

import com.rafaelwillen.model.finance.MonthlyPrevision;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class PrevisionModel {
    private final SimpleStringProperty previsionName;
    private final SimpleDoubleProperty previsionValue;
    private final MonthlyPrevision prevision;

    public PrevisionModel(MonthlyPrevision prevision){
        this.prevision = prevision;
        this.previsionName = new SimpleStringProperty(prevision.getName());
        this.previsionValue = new SimpleDoubleProperty(prevision.getPredictedValue());
    }

    public String getPrevisionName() {
        return previsionName.get();
    }

    public SimpleStringProperty previsionNameProperty() {
        return previsionName;
    }

    public void setPrevisionName(String previsionName) {
        this.previsionName.set(previsionName);
    }

    public double getPrevisionValue() {
        return previsionValue.get();
    }

    public SimpleDoubleProperty previsionValueProperty() {
        return previsionValue;
    }

    public void setPrevisionValue(double previsionValue) {
        this.previsionValue.set(previsionValue);
    }

    public MonthlyPrevision getPrevision() {
        return prevision;
    }
}
