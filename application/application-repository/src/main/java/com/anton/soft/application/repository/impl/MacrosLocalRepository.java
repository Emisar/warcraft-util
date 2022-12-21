package com.anton.soft.application.repository.impl;

import com.anton.soft.application.dto.impl.IdentifiableMacros;
import com.anton.soft.application.repository.LocalRepository;
import java.util.HashMap;

public class MacrosLocalRepository extends LocalRepository<IdentifiableMacros, Long> {

  public MacrosLocalRepository() {
    super(0L, new HashMap<>());
  }

  @Override
  protected Long getIdCounter() {
    return idCounter++;
  }
}
