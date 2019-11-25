package com.theironyard.repositories;

import com.theironyard.entities.User;
import com.theironyard.entities.Widget;
import org.springframework.data.repository.CrudRepository;


public interface WidgetRepository extends CrudRepository<Widget, Integer> {

}