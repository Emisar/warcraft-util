module com.anton.soft.desktop.application {
  requires javafx.controls;
  requires javafx.fxml;
  requires lombok;
  requires org.jetbrains.annotations;
  requires org.apache.commons.lang3;

  requires util;
  requires hero.loader.dto;
  requires hero.loader.util;
  requires key.binding.dto;
  requires key.binding.util;

  opens com.anton.soft.desktop.application to javafx.fxml;
  opens com.anton.soft.desktop.application.controller to javafx.fxml;
  opens com.anton.soft.desktop.application.dto to javafx.base;

  exports com.anton.soft.desktop.application;
  exports com.anton.soft.desktop.application.controller;
  exports com.anton.soft.desktop.application.controller.global;
  exports com.anton.soft.desktop.application.controller.macros;
  exports com.anton.soft.desktop.application.controller.loader;
  opens com.anton.soft.desktop.application.controller.global to javafx.fxml;
  opens com.anton.soft.desktop.application.controller.macros to javafx.fxml;
  opens com.anton.soft.desktop.application.controller.loader to javafx.fxml;
}