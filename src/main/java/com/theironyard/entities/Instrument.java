package com.theironyard.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Instrument {
    /*
    Каждый Entity должен иметь поле id
     */
    @Id
    @GeneratedValue
    int id;

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
