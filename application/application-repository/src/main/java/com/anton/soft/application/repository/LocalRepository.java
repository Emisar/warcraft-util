package com.anton.soft.application.repository;

import com.anton.soft.application.dto.Identifiable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public abstract class LocalRepository<T extends Identifiable<K>, K extends Serializable> implements
    Repository<T, K> {

  protected K idCounter;
  protected Map<K, T> storage;

  protected abstract K getIdCounter();

  @Override
  public T create(@NotNull T object) {
    K id = Optional.ofNullable(object.getId()).orElse(getIdCounter());
    object.setId(id);
    return storage.put(id, object);
  }

  @Override
  public T read(K id) {
    return storage.get(id);
  }

  @Override
  public List<T> readAll() {
    return new ArrayList<>(storage.values());
  }

  @Override
  public boolean update(K id, T object) {
    T updatedValue = storage.computeIfPresent(id, (k, v) -> object);
    return Objects.nonNull(updatedValue);
  }

  @Override
  public T delete(K id) {
    return storage.remove(id);
  }

  @Override
  public boolean deleteAll() {
    storage.clear();
    return true;
  }

  @Override
  public Long count() {
    return (long) storage.values().size();
  }

  @Override
  public boolean exists(K id) {
    return storage.containsKey(id);
  }
}
