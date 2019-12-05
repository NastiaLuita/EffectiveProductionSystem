package com.theironyard.repositories;

import com.theironyard.entities.RequestPart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestPartRepository extends CrudRepository<RequestPart, Integer> {

}