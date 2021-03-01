package com.rafaelwillen.model.finance;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Cost {
    private final int id;
    private String name;
    private double value;
    private LocalDate addedDate;
    private String local;
    private String description;

    public Cost(int id, String name, double value, LocalDate addedDate) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.addedDate = addedDate;
    }

    public Cost(int id, String name, double value) {
        this(id, name, value, LocalDate.now());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Cost{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", value=").append(value);
        sb.append(", addedDate=").append(addedDate);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cost cost = (Cost) o;
        return id == cost.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
