package com.rafaelwillen.model.family;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class Family {
    private final int id;
    private final LinkedList<Son> sons;
    private final LinkedList<Pet> pets;
    private String name;
    private String housePhoneNumber;
    private HashMap<String, String> address;
    private Parent mother;
    private Parent father;

    public Family(int id, String name, String housePhoneNumber, HashMap<String, String> address) {
        this.id = id;
        this.name = name;
        this.housePhoneNumber = housePhoneNumber;
        this.address = address;
        mother = father = null;
        sons = new LinkedList<>();
        pets = new LinkedList<>();
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

    public String getHousePhoneNumber() {
        return housePhoneNumber;
    }

    public void setHousePhoneNumber(String housePhoneNumber) {
        this.housePhoneNumber = housePhoneNumber;
    }

    public HashMap<String, String> getAddress() {
        return address;
    }

    public void setAddress(HashMap<String, String> address) {
        this.address = address;
    }

    public Parent getMother() {
        return mother;
    }

    public void setMother(Parent mother) {
        this.mother = mother;
    }

    public Parent getFather() {
        return father;
    }

    public void setFather(Parent father) {
        this.father = father;
    }

    public LinkedList<Son> getSons() {
        return sons;
    }

    public LinkedList<Pet> getPets() {
        return pets;
    }

    public void addSon(Son newSon) {
        sons.add(newSon);
    }

    public void addPet(Pet newPet) {
        pets.add(newPet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Family family = (Family) o;
        return id == family.id && name.equals(family.name);
    }

    @Override
    public String toString() {
        return "Family{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
