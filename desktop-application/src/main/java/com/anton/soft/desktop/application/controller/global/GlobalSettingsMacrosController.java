package com.anton.soft.desktop.application.controller.global;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static javafx.scene.paint.Color.color;

import com.anton.soft.desktop.application.controller.AbstractFrameController;
import com.anton.soft.desktop.application.local.LocalData;
import com.anton.soft.desktop.application.util.FrameManager;
import com.anton.soft.desktop.application.util.IFrameManager;
import com.anton.soft.key.binding.dto.Macros;
import com.anton.soft.key.binding.util.MacrosListener;
import com.anton.soft.key.binding.util.MacrosManager;
import com.anton.soft.util.FileManager;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Window;

public class GlobalSettingsMacrosController extends AbstractFrameController {

  private static final String STOPPED = "Stopped";
  private static final String RUNNING = "Running";

  @FXML
  private TextField macrosDirectoryPathTextField;
  @FXML
  private Label macrosStatusLabel;
  @FXML
  private ToggleButton offToggleButton;
  @FXML
  private ToggleButton onToggleButton;
  @FXML
  private Button selectDirectoryButton;

  private String macrosDirectoryPath;
  private List<Path> macrosFiles;
  private List<Macros> macrosList;

  private LocalData localData;
  private FileManager fileManager;
  private IFrameManager frameManager;
  private MacrosManager macrosManager;

  @Override
  protected void initUtils(Object... params) {
    localData = LocalData.getInstance();
    fileManager = new FileManager();
    frameManager = new FrameManager();
    macrosManager = new MacrosManager();
  }

  @Override
  protected void initData(Object... params) {
    macrosDirectoryPath = localData.getDefaultMacrosDirectory();
  }

  @Override
  protected void initContext(Object... params) {
    ToggleGroup toggleGroup = new ToggleGroup();
    onToggleButton.setToggleGroup(toggleGroup);
    offToggleButton.setToggleGroup(toggleGroup);
    offToggleButton.setSelected(true);
    macrosDirectoryPathTextField.setText(macrosDirectoryPath);
  }

  @FXML
  void selectDirectoryButtonOnAction(ActionEvent event) {
    Window window = anchorPane.getScene().getWindow();
    File file = frameManager.chooseDirectory(window, localData.getDefaultMacrosDirectory());
    macrosDirectoryPathTextField.setText(file.getAbsolutePath());
    updateData();
  }

  @FXML
  void macrosDirectoryPathTextFieldOnAction(ActionEvent event) {
    updateData();
  }

  @FXML
  void offToggleButtonOnAction(ActionEvent event) {
    if (offToggleButton.isSelected()) {
      turnMacrosOff();
      macrosStatusLabel.setText(STOPPED);
      macrosStatusLabel.setTextFill(color(1, 0, 0));
      localData.setIsMacrosActive(FALSE);
    } else {
      onToggleButton.setSelected(true);
      onToggleButtonOnAction(event);
    }
  }

  @FXML
  void onToggleButtonOnAction(ActionEvent event) {
    if (onToggleButton.isSelected()) {
      updateData();
      turnMacrosOn();
      macrosStatusLabel.setText(RUNNING);
      macrosStatusLabel.setTextFill(color(0, 1, 0));
      localData.setIsMacrosActive(TRUE);
    } else {
      offToggleButton.setSelected(true);
      offToggleButtonOnAction(event);
    }
  }

  private void turnMacrosOn() {
    List<MacrosListener> macrosListeners = new ArrayList<>();
    for (Macros macros : macrosList) {
      if (Objects.nonNull(macros) && macros.getStatus().equals(TRUE)) {
        MacrosListener macrosListener = macrosManager.createMacrosListener(macros);
        macrosManager.addMacrosListener(macrosListener);
        macrosListeners.add(macrosListener);
      }
    }
    localData.setMacrosListenerList(macrosListeners);
  }

  private void turnMacrosOff() {
    List<MacrosListener> macrosListeners = localData.getMacrosListenerList();
    for (MacrosListener macrosListener : macrosListeners) {
      macrosManager.removeMacrosListener(macrosListener);
    }
    localData.setMacrosListenerList(new ArrayList<>());
  }

  @Override
  protected void updateData() {
    macrosDirectoryPath = macrosDirectoryPathTextField.getText();
    macrosFiles = updateMacrosFiles();
    macrosList = updateMacrosList();
  }

  private List<Path> updateMacrosFiles() {
    Path root = fileManager.getRoot(macrosDirectoryPath);
    List<Path> content = fileManager.getContent(root);
    return fileManager.getFiles(content);
  }

  private List<Macros> updateMacrosList() {
    return macrosFiles.stream()
        .map(macrosManager::getMacros)
        .collect(Collectors.toList());
  }
}
