package com.rafaelwillen.model.family;

import java.time.LocalDate;

public class Parent extends Person {

    private Parent(String username, String name, LocalDate birthDate, String password, Sex sex) {
        super(username, name, birthDate, password, sex);
    }

    public static Parent buildFather(String username, String name, LocalDate birthDate, String password){
        return new Parent(username, name, birthDate, password, Sex.MASCULINO);
    }

    public static Parent buildMother(String username, String name, LocalDate birthDate, String password){
        return new Parent(username, name, birthDate, password, Sex.FEMININO);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Parent{");
        sb.append("username='").append(username).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", sex=").append(sex);
        sb.append('}');
        return sb.toString();
    }
}
