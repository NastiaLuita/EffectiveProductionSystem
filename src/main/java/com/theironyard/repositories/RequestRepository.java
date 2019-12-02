package com.theironyard.repositories;

import com.theironyard.entities.Request;
import org.springframework.data.repository.CrudRepository;


public interface RequestRepository extends CrudRepository<Request, Integer> {

}