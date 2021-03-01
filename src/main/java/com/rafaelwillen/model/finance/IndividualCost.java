package com.rafaelwillen.model.finance;

import java.time.LocalDate;

public class IndividualCost extends Cost {
    public IndividualCost(int id, String name, double value, LocalDate addedDate) {
        super(id, name, value, addedDate);
    }

    public IndividualCost(int id, String name, double value) {
        super(id, name, value);
    }
}
