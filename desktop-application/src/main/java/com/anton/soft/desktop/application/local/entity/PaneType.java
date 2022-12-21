package com.anton.soft.desktop.application.local.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaneType {
  MAIN_FRAME("main-frame.fxml", "Warcraft Util"),
  GLOBAL_SETTINGS("global/settings/global-settings.fxml", "Global"),
  GLOBAL_SETTINGS_MACROS("global/settings/global-settings-macros.fxml", "Global settings - Macros"),
  GLOBAL_SETTINGS_RPG_LOADER("global/settings/global-settings-rpg-loader.fxml", "Global settings - RPG Loader"),
  TKOK_LOADER("global/settings/tkok-loader.fxml", "TKoK Loader"),
  MACROS_SETTINGS("macros/settings/macros-settings.fxml", "Macros"),
  MACROS_CONFIG("macros/settings/macros-config.fxml", "Macros Configuration"),
  MACROS_ACTION_CONFIG("macros/settings/action-config.fxml", "Action Configuration"),
  RPG_LOADER_SETTINGS("rpg/loader/settings/rpg-loader-settings.fxml", "RPG Loader");

  @Getter
  private final String path;
  @Getter
  private final String title;
}
