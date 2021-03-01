package com.rafaelwillen.model.family;

import com.rafaelwillen.model.finance.IndividualCost;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Objects;

public class Pet {
    private final int id;
    private String animalType;
    private String name;
    private LocalDate birthDate;
    private Sex sex;
    private final LinkedList<IndividualCost> costs;

    public Pet(int id, String animalType, String name, LocalDate birthDate, Sex sex) {
        this.id = id;
        this.animalType = animalType;
        this.name = name;
        this.birthDate = birthDate;
        this.sex = sex;
        costs = new LinkedList<>();
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getAge(){
        return LocalDate.now().getYear() - birthDate.getYear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return id == pet.id;
    }

    public LinkedList<IndividualCost> getCosts() {
        return costs;
    }

    public void addCost(IndividualCost cost){
        costs.add(cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", animalType='" + animalType + '\'' +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
