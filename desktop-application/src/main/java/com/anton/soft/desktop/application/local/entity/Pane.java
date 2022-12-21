package com.anton.soft.desktop.application.local.entity;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pane {

  private PaneType paneType;
  private FXMLLoader loader;
  private AnchorPane node;

  public AnchorPane getNode() {
    return node;
  }
}
