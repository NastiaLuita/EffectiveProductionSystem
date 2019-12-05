package com.theironyard.entities;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;


@Entity
public class Request {

    @Id
    @GeneratedValue
    int id;

    @Embedded
    private ArrayList<RequestPart> parts;
    //User user;

    public Request(){
        parts = new ArrayList<>();
    }

    public void addPart(Instrument instrument, int time){
        parts.add(new RequestPart(this, instrument, time));
    }

    public ArrayList<RequestPart> getParts(){
        return parts;
    }

    public String[] resultParts(){
        String[] res = new String[parts.size()];
        int i = 0;
        for(RequestPart p: parts){
            res[i] = "Instrument: "+p.getInstrument().getName()+", requested time: "+p.getTime()+", start time: "+p.getStartTime();
            i+=1;
        }
        return res;
    }
}
