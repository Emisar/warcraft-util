package com.anton.soft.desktop.application.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Setter;

public abstract class AbstractFrameController implements IFrameController {

  @FXML
  protected AnchorPane anchorPane;

  @Setter(AccessLevel.PROTECTED)
  protected Object result;

  @Override
  public void init(Object... params) {
    setResult(null);
    initUtils(params);
    initData(params);
    initContext(params);
    updateUI();
  }

  protected abstract void initUtils(Object... params);
  protected abstract void initData(Object... params);
  protected abstract void initContext(Object... params);

  @Override
  public void update() {
    updateData();
    updateUI();
  }

  protected void updateData() {}
  protected void updateUI() {}

  @Override
  public <T> T getResult() {
    return (T) result;
  }

  protected void windowClose() {
    Stage window = (Stage) anchorPane.getScene().getWindow();
    window.close();
  }
}
