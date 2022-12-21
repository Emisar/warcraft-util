package com.anton.soft.application.service;

import com.anton.soft.application.dto.Identifiable;
import com.anton.soft.application.repository.Repository;
import java.io.Serializable;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class CrudService<T extends Identifiable<K>, K extends Serializable> implements
    Service<T, K> {

  protected final Repository<T, K> repository;

  @Override
  public T add(T object) {
    return repository.create(object);
  }

  @Override
  public T get(K id) {
    return repository.read(id);
  }

  @Override
  public List<T> getAll() {
    return repository.readAll();
  }

  @Override
  public boolean update(K id, T object) {
    return repository.update(id, object);
  }

  @Override
  public T delete(K id) {
    return repository.delete(id);
  }
}
