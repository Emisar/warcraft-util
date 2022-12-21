package com.anton.soft.application.service;

import com.anton.soft.application.dto.Identifiable;
import java.io.Serializable;
import java.util.List;

public interface Service<T extends Identifiable<K>, K extends Serializable> {

  T add(T object);

  T get(K id);

  List<T> getAll();

  boolean update(K id, T object);

  T delete(K id);
}
