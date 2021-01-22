package com.thinktech.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;

public class CarbonForDate {
    private LocalDate date;
    private ArrayList<CarbonItem> carbonItems;

    public CarbonForDate(LocalDate date, ArrayList<CarbonItem> carbonItems) {
        this.date = date;
        this.carbonItems = carbonItems;
    }

    public LocalDate getDate() {
        return date;
    }

    public ArrayList<CarbonItem> getCarbonItems() {
        return carbonItems;
    }
}
