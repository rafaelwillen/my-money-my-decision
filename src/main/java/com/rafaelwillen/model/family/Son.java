package com.rafaelwillen.model.family;

import java.time.LocalDate;

public class Son extends Person{

    public Son(String username, String name, LocalDate birthDate, String password, Sex sex) {
        super(username, name, birthDate, password, sex);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Son{");
        sb.append("username='").append(username).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", sex=").append(sex);
        sb.append('}');
        return sb.toString();
    }
}
