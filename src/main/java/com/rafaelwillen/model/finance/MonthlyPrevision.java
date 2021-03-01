package com.rafaelwillen.model.finance;

import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedList;
import java.util.Objects;

public class MonthlyPrevision {
    private final int id;
    private final LinkedList<Cost> costs;
    private final String name;
    private final double predictedValue;
    private final Month month;
    private final int year;
    private final LocalDate addedDate;
    private String description;

    public MonthlyPrevision(int id, String name, double predictedValue, Month month, int year, LocalDate addedDate) {
        this.id = id;
        this.name = name;
        this.predictedValue = predictedValue;
        this.month = month;
        this.year = year;
        this.addedDate = addedDate;
        costs = new LinkedList<>();
    }

    public MonthlyPrevision(int id, String name, double predictedValue, Month month, int year) {
        this(id, name, predictedValue, month, year, LocalDate.now());
    }

    public int getId() {
        return id;
    }

    public LinkedList<Cost> getCosts() {
        return costs;
    }

    public String getName() {
        return name;
    }

    public double getPredictedValue() {
        return predictedValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Month getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }

    public void addCost(Cost cost) {
        costs.add(cost);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MonthlyPrevision{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", predictedValue=").append(predictedValue);
        sb.append(", month=").append(month);
        sb.append(", year=").append(year);
        sb.append(", addedDate=").append(addedDate);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyPrevision that = (MonthlyPrevision) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
