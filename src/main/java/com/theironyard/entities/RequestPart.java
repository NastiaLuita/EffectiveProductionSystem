package com.theironyard.entities;

import javax.persistence.Entity;

@Entity
public class RequestPart {

    Instrument instrument;
    int time;
    int startTime = 0;

    public RequestPart() {}

    public RequestPart(Instrument instrument, int time){
        this.instrument = instrument;
        this.time = time;
    }

    public Instrument getInstrument(){
        return instrument;
    }

    public int getTime(){
        return time;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getStartTime() {
        return startTime;
    }
}
