package com.anton.soft.desktop.application.controller.macros;

import static javafx.collections.FXCollections.observableArrayList;

import com.anton.soft.desktop.application.controller.AbstractFrameController;
import com.anton.soft.key.binding.dto.Action;
import com.anton.soft.key.binding.dto.enums.ActionType;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ActionConfigController extends AbstractFrameController {

  @FXML
  private ComboBox<ActionType> actionTypeComboBox;
  @FXML
  private TextField actionValueTextField;

  private Action action;

  @Override
  protected void initUtils(Object... params) {
    // There are not utils for use
  }

  @Override
  protected void initData(Object... params) {
    action = (Action) params[0];
  }

  @Override
  protected void initContext(Object... params) {
    actionTypeComboBox.setItems(observableArrayList(ActionType.values()));
    actionTypeComboBox.setValue(action.getActionType());
    actionValueTextField.setText(action.getValue());
  }

  @FXML
  void closeButtonOnAction(ActionEvent event) {
    windowClose();
  }

  @FXML
  void saveButtonOnAction(ActionEvent event) {
    updateData();
    setResult(action);
    windowClose();
  }

  @Override
  protected void updateData() {
    if (Objects.nonNull(action)) {
      action.setActionType(actionTypeComboBox.getValue());
      action.setValue(actionValueTextField.getText());
    }
  }

  @Override
  protected void updateUI() {
    actionTypeComboBox.setValue(action.getActionType());
    actionValueTextField.setText(action.getValue());
  }

}
