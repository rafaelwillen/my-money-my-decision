package com.rafaelwillen.model.family;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

public abstract class Person {
    protected final String username;
    protected final LinkedList<String> emails;
    protected final LinkedList<String> phoneNumbers;
    protected String name;
    protected LocalDate birthDate;
    protected String password;
    protected Sex sex;

    public Person(String username, String name, LocalDate birthDate, String password, Sex sex) {
        this.username = username;
        this.name = name;
        this.birthDate = birthDate;
        this.password = password;
        this.sex = sex;
        emails = new LinkedList<>();
        phoneNumbers = new LinkedList<>();
    }

    public String getUsername() {
        return username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public int getAge() {
        return LocalDate.now().getYear() - birthDate.getYear();
    }

    public void addEmail(String email) {
        emails.add(email);
    }

    public void addPhoneNumbers(String phoneNumber) {
        phoneNumbers.add(phoneNumber);
    }

    public void addAllEmail(String... email) {
        emails.addAll(Arrays.asList(email));
    }

    public void addAllPhoneNumber(String... phoneNumber) {
        phoneNumbers.addAll(Arrays.asList(phoneNumber));
    }

    public LinkedList<String> getEmails() {
        return emails;
    }

    public LinkedList<String> getPhoneNumbers(){
        return phoneNumbers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return username.equals(person.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }


}
