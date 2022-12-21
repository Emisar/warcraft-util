package com.anton.soft.application.dto;

import java.io.Serializable;

public interface Identifiable<K extends Serializable> {

  K getId();

  void setId(K id);
}
