package com.anton.soft.desktop.application.controller.macros;

import static javafx.collections.FXCollections.observableArrayList;

import com.anton.soft.desktop.application.controller.AbstractFrameController;
import com.anton.soft.desktop.application.controller.IFrameController;
import com.anton.soft.desktop.application.local.LocalData;
import com.anton.soft.desktop.application.local.entity.Pane;
import com.anton.soft.desktop.application.local.entity.PaneType;
import com.anton.soft.desktop.application.util.FrameManager;
import com.anton.soft.key.binding.dto.Action;
import com.anton.soft.key.binding.dto.Macros;
import com.anton.soft.key.binding.dto.enums.ActionType;
import com.anton.soft.key.binding.dto.enums.EventType;
import java.util.Objects;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class MacrosConfigurationController extends AbstractFrameController {

  @FXML
  private TableView<Action> actionTable;
  @FXML
  private TableColumn<Action, ActionType> actionTableActionTypeColumn;
  @FXML
  private TableColumn<Action, String> actionTableActionValueColumn;
  @FXML
  private ComboBox<EventType> eventTypeComboBox;
  @FXML
  private TextField eventValueTextField;
  @FXML
  private TextField macrosNameTextField;
  @FXML
  private CheckBox statusCheckbox;
  @FXML
  private CheckBox oneTimeCheckbox;

  private Macros macros;
  private Stage actionConfigStage;
  private LocalData localData;
  private FrameManager frameManager;

  @Override
  protected void initUtils(Object... params) {
    localData = LocalData.getInstance();
    frameManager = new FrameManager();
  }

  @Override
  protected void initData(Object... params) {
    macros = (Macros) params[0];
  }

  @Override
  protected void initContext(Object... params) {
    eventTypeComboBox.setItems(observableArrayList(EventType.values()));
    eventTypeComboBox.setValue(macros.getEvent().getEventType());
    actionTableActionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("actionType"));
    actionTableActionValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
  }

  @FXML
  void createActionButtonOnAction(ActionEvent event) {
    macros.getActions().add(new Action());
    actionTable.setItems(observableArrayList(macros.getActions()));
    actionTable.refresh();
  }

  @FXML
  void deleteActionOnAction(ActionEvent event) {
    int index = actionTable.getSelectionModel().getSelectedIndex();
    if (index != -1) {
      macros.getActions().remove(index);
      actionTable.setItems(observableArrayList(macros.getActions()));
      actionTable.refresh();
    }
  }

  @FXML
  void editActionButtonOnAction(ActionEvent event) {
    int index = actionTable.getSelectionModel().getSelectedIndex();
    if (index != -1) {
      Pane pane = Optional.ofNullable(localData.getPaneByType(PaneType.MACROS_ACTION_CONFIG))
          .orElse(new Pane());
      IFrameController controller = pane.getLoader().getController();
      Action action = macros.getActions().get(index);
      controller.init(action);
      if (Objects.isNull(actionConfigStage)) {
        actionConfigStage = frameManager.createWindow(anchorPane.getScene().getWindow(),
            pane.getNode(), PaneType.MACROS_ACTION_CONFIG.getTitle());
      }
      actionConfigStage.setOnHiding(e -> actionTable.refresh());
      actionConfigStage.show();
    }
  }

  @FXML
  void actionTableOnClicked(@NotNull MouseEvent event) {
    if (event.getClickCount() == 2) {
      editActionButtonOnAction(null);
    }
  }

  @FXML
  void closeButtonOnAction(ActionEvent event) {
    windowClose();
  }

  @FXML
  void saveButtonOnAction(ActionEvent event) {
    updateData();
    setResult(macros);
    windowClose();
  }

  @Override
  protected void updateData() {
    if (Objects.nonNull(macros)) {
      macros.setName(macrosNameTextField.getText());
      if (Objects.nonNull(macros.getEvent())) {
        macros.getEvent().setEventType(eventTypeComboBox.getValue());
        macros.getEvent().setValue(eventValueTextField.getText());
      }
      macros.setStatus(statusCheckbox.isSelected());
      macros.setOneTime(oneTimeCheckbox.isSelected());
      macros.setActions(actionTable.getItems());
    }
  }

  @Override
  protected void updateUI() {
    if (Objects.nonNull(macros)) {
      macrosNameTextField.setText(macros.getName());
      if (Objects.nonNull(macros.getEvent())) {
        eventTypeComboBox.setValue(macros.getEvent().getEventType());
        eventValueTextField.setText(macros.getEvent().getValue());
      }
      statusCheckbox.setSelected(macros.getStatus());
      oneTimeCheckbox.setSelected(macros.getOneTime());
      actionTable.setItems(observableArrayList(macros.getActions()));
      actionTable.refresh();
    }
  }

}
