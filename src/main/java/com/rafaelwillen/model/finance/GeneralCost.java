package com.rafaelwillen.model.finance;

import java.time.LocalDate;

public class GeneralCost extends Cost {
    public GeneralCost(int id, String name, double value, LocalDate addedDate) {
        super(id, name, value, addedDate);
    }

    public GeneralCost(int id, String name, double value) {
        super(id, name, value);
    }
}
