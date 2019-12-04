package com.theironyard;

import com.theironyard.entities.Instrument;
import com.theironyard.repositories.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.ArrayList;

@Component
public class Initializator {

    @Autowired
    InstrumentRepository instrumentRepository;

    ArrayList<Instrument> instruments = new ArrayList<>();

    Initializator() {}

    public InstrumentRepository initInstruments() {
        instrumentRepository.deleteAll();

        Instrument i1 = new Instrument("Instrument 1", 3);
        instruments.add(i1);

        Instrument i2 = new Instrument("Instrument 2", 1);
        instruments.add(i2);

        Instrument i3 = new Instrument("Instrument 3", 2);
        instruments.add(i3);

        instrumentRepository.save(instruments);

        return instrumentRepository;
        //model.addAttribute("instruments", instrumentRepository.findAll());
    }


}
