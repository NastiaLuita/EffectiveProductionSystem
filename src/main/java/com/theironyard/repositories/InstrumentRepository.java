package com.theironyard.repositories;

import com.theironyard.entities.Instrument;
import org.springframework.data.repository.CrudRepository;


public interface InstrumentRepository extends CrudRepository<Instrument, Integer> {

}