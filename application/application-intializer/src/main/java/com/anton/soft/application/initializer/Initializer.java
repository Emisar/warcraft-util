package com.anton.soft.application.initializer;

import com.anton.soft.application.dto.Identifiable;
import java.io.Serializable;
import java.util.List;

public interface Initializer<T extends Identifiable<K>, K extends Serializable> {

  List<T> init();
}
