package com.theironyard.entities;

import javax.persistence.Entity;

@Entity
public class Instrument {
    String name;
    int count;

    public Instrument(String name, int count){
        this.name = name;
        this.count = count;
    }

    public String getName(){
        return name;
    }

    public int getCount(){
        return count;
    }
}
