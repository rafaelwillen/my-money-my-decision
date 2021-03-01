package com.rafaelwillen.model.finance;

import java.time.LocalDate;
import java.util.Objects;

public class Credit {
    private final int id;
    private double grantedValue;
    private int fees;
    private LocalDate resquestDate;
    private LocalDate deadline;
    private String description;

    public Credit(int id, double grantedValue, int fees, LocalDate resquestDate, LocalDate deadline) {
        this.id = id;
        this.grantedValue = grantedValue;
        this.fees = fees;
        this.resquestDate = resquestDate;
        this.deadline = deadline;
    }

    public Credit(int id, double grantedValue, int fees, LocalDate deadline) {
        this(id, grantedValue, fees, LocalDate.now(), deadline);
    }

    public int getId() {
        return id;
    }

    public double getGrantedValue() {
        return grantedValue;
    }

    public void setGrantedValue(double grantedValue) {
        this.grantedValue = grantedValue;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public LocalDate getResquestDate() {
        return resquestDate;
    }

    public void setResquestDate(LocalDate resquestDate) {
        this.resquestDate = resquestDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double valueToPay() {
        double percentValue = grantedValue * ((double) fees / 100);
        return percentValue + grantedValue;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Credit{");
        sb.append("id=").append(id);
        sb.append(", grantedValue=").append(grantedValue);
        sb.append(", fees=").append(fees);
        sb.append(", resquestDate=").append(resquestDate);
        sb.append(", deadline=").append(deadline);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credit credit = (Credit) o;
        return id == credit.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
