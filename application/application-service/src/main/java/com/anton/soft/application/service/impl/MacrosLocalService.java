package com.anton.soft.application.service.impl;

import com.anton.soft.application.dto.impl.IdentifiableMacros;
import com.anton.soft.application.repository.impl.MacrosLocalRepository;
import com.anton.soft.application.service.CrudService;

public class MacrosLocalService extends CrudService<IdentifiableMacros, Long> {

  public MacrosLocalService() {
    super(new MacrosLocalRepository());
  }
}
