package com.rafaelwillen.model.family;

import com.rafaelwillen.model.finance.GeneralCost;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class Family {
    public final static String PROVINCE_KEY = "PROVINCE";
    public final static String DISTRICT_KEY = "MUNICIPIO";
    public final static String STREET_KEY = "RUA";
    private final int id;
    private final LinkedList<Son> sons;
    private final LinkedList<Pet> pets;
    private final LinkedList<GeneralCost> costs;
    private String name;
    private String housePhoneNumber;
    private HashMap<String, String> address;
    private Parent mother;
    private Parent father;

    public Family(int id, String name, String housePhoneNumber, HashMap<String, String> address) {
        this.id = id;
        this.name = name;
        this.address = address == null ? new HashMap<String, String>() : address;
        this.housePhoneNumber = housePhoneNumber;
        mother = father = null;
        sons = new LinkedList<>();
        pets = new LinkedList<>();
        costs = new LinkedList<>();
    }

    public Family(int id, String name, String housePhoneNumber, String address) {
        this(id, name, housePhoneNumber, (HashMap<String, String>) null);
        setAddressString(address);
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
        if (address.containsKey(DISTRICT_KEY) && address.containsKey(STREET_KEY) && address.containsKey(PROVINCE_KEY))
            this.address = address;
        else
            throw new IllegalArgumentException("The keys in the hash map are not correct. Must be MUNICIPIO, RUA, PROVINCE");
    }

    public String getAddressString() {
        return String.join(", ", address.values());
    }

    public void setAddressString(String addressString) {

        if (addressString.split(",").length != 3)
            throw new IllegalArgumentException("The 'addressString' is not formatted correctly. Must have two ','");
        address.put(PROVINCE_KEY, addressString.split(",")[0]);
        address.put(DISTRICT_KEY, addressString.split(",")[1]);
        address.put(STREET_KEY, addressString.split(",")[2]);
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

    public LinkedList<GeneralCost> getCosts() {
        return costs;
    }

    public void addCost(GeneralCost cost) {
        costs.add(cost);
    }

    public int getNumberOfMembers(){
        int number = sons.size();
        if (mother != null) number++;
        if (father != null) number++;
        return number;
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
