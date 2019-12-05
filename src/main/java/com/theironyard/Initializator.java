package com.theironyard;

import com.theironyard.entities.Instrument;
import com.theironyard.repositories.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

@Controller
public class Initializator {

    @Autowired
    InstrumentRepository instrumentRepository;

    ArrayList<Instrument> instruments = new ArrayList<>();

    Initializator() {}

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public InstrumentRepository initInstruments(Model model) {
        instrumentRepository.deleteAll();

        Instrument i1 = new Instrument("Instrument 1", 3);
        instruments.add(i1);

        Instrument i2 = new Instrument("Instrument 2", 1);
        instruments.add(i2);

        Instrument i3 = new Instrument("Instrument 3", 2);
        instruments.add(i3);

        instrumentRepository.save(instruments);

        model.addAttribute("instruments", instrumentRepository.findAll());
        return instrumentRepository;
    }


}
