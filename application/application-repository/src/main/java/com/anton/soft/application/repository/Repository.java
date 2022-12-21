package com.anton.soft.application.repository;

import java.io.Serializable;
import java.util.List;

public interface Repository<T, K extends Serializable> {

  T create(T object);

  T read(K id);

  List<T> readAll();

  boolean update(K id, T object);

  T delete(K id);

  boolean deleteAll();

  Long count();

  boolean exists(K id);
}
