package com.anton.soft.desktop.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RPGType {
  TKOK("The Kingdom of Kaliron"),
  TW_EVE("Twilight's Eve");

  @Getter
  private final String name;
}
