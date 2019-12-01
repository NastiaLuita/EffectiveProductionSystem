package com.theironyard.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Widget {

    @Id
    @GeneratedValue
    int id;

    String name;

    String description;

    double time;

    public Widget(){}

    public Widget(String name, String description, double time) {
        this.name = name;
        this.description = description;
        this.time = time;
    }
}
