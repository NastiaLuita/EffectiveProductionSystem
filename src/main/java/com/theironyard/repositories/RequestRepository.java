package com.theironyard.repositories;

import com.theironyard.entities.Request;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends CrudRepository<Request, Integer> {

}