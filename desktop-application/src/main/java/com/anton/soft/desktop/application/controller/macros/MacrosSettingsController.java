package com.anton.soft.desktop.application.controller.macros;

import static java.text.MessageFormat.format;

import com.anton.soft.desktop.application.controller.AbstractFrameController;
import com.anton.soft.desktop.application.controller.IFrameController;
import com.anton.soft.desktop.application.local.LocalData;
import com.anton.soft.desktop.application.local.entity.Pane;
import com.anton.soft.desktop.application.local.entity.PaneType;
import com.anton.soft.desktop.application.util.FrameManager;
import com.anton.soft.desktop.application.util.IFrameManager;
import com.anton.soft.key.binding.dto.Macros;
import com.anton.soft.key.binding.dto.enums.EventType;
import com.anton.soft.key.binding.util.MacrosManager;
import com.anton.soft.util.FileManager;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

public class MacrosSettingsController extends AbstractFrameController {

  private static final String NEW_MACROS_FULL_PATH_PATTERN = "{0}\\{1}.json";
  private static final String NEW_MACROS_NAME_PATTERN = "Macros{0}";

  @FXML
  private TextField macrosDirectoryPathTextField;
  @FXML
  private TableView<Macros> macrosTable;
  @FXML
  private TableColumn<Macros, EventType> macrosTableEventColumn;
  @FXML
  private TableColumn<Macros, String> macrosTableMacrosColumn;
  @FXML
  private TableColumn<Macros, Boolean> macrosTableStatusColumn;
  @FXML
  private TableColumn<Macros, String> macrosTableValueColumn;

  private LocalData localData;
  private FileManager fileManager;
  private IFrameManager frameManager;
  private MacrosManager macrosManager;

  private String macrosDirectoryPath;
  private Map<Macros, Path> macrosMap;
  private Stage macrosConfigStage;

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
    macrosMap = getMacrosMap();
  }

  @Override
  protected void initContext(Object... params) {
    macrosDirectoryPathTextField.setText(macrosDirectoryPath);
    macrosTableMacrosColumn.setCellValueFactory(macros -> getSimpleObjectProperty(macros.getValue().getName()));
    macrosTableEventColumn.setCellValueFactory(macros -> getSimpleObjectProperty(macros.getValue().getEvent().getEventType()));
    macrosTableValueColumn.setCellValueFactory(macros -> getSimpleObjectProperty(macros.getValue().getEvent().getValue()));
    macrosTableStatusColumn.setCellValueFactory(macros -> getSimpleObjectProperty(macros.getValue().getStatus()));
  }

  @NotNull
  private <T> SimpleObjectProperty<T> getSimpleObjectProperty(T value) {
    SimpleObjectProperty<T> property = new SimpleObjectProperty<>();
    property.setValue(value);
    return property;
  }

  @FXML
  void selectFolderButtonOnAction(ActionEvent event) {
    Window window = anchorPane.getScene().getWindow();
    File file = frameManager.chooseDirectory(window, macrosDirectoryPath);
    macrosDirectoryPathTextField.setText(file.getAbsolutePath());
    update();
  }

  @FXML
  void createButtonOnAction(ActionEvent event) {
    String macrosName = generateName();
    Path path = Path.of(macrosDirectoryPath, macrosName + ".json");
    Macros macros = Macros.builder().name(macrosName).build();
    String macrosJson = macrosManager.macrosToString(macros);
    fileManager.writeToFile(path, macrosJson);
    update();
  }

  @NotNull
  private String generateName() {
    for (int i = 0; true; i++) {
      String macrosName = format(NEW_MACROS_NAME_PATTERN, (i == 0) ? "" : i);
      String fullPath = format(NEW_MACROS_FULL_PATH_PATTERN, macrosDirectoryPath, macrosName);
      if (Files.notExists(Path.of(fullPath))) {
        return macrosName;
      }
    }
  }

  @FXML
  void deleteButtonOnAction(ActionEvent event) {
    Macros selectedMacros = macrosTable.getSelectionModel().getSelectedItem();
    if (Objects.nonNull(selectedMacros) && fileManager.deleteFile(macrosMap.get(selectedMacros))) {
      update();
    }
  }

  @FXML
  void editButtonOnAction(ActionEvent event) {
    Macros selectedMacros = macrosTable.getSelectionModel().getSelectedItem();
    if (Objects.nonNull(selectedMacros)) {
      Pane pane = Optional.ofNullable(localData.getPaneByType(PaneType.MACROS_CONFIG))
          .orElse(new Pane());
      IFrameController controller = pane.getLoader().getController();
      controller.init(selectedMacros);
      if (Objects.isNull(macrosConfigStage)) {
        macrosConfigStage = frameManager.createWindow(anchorPane.getScene().getWindow(),
            pane.getNode(), PaneType.MACROS_CONFIG.getTitle());
      }
      macrosConfigStage.setOnHiding(e -> onCloseAction(controller));
      macrosConfigStage.show();
    }
  }

  private void onCloseAction(@NotNull IFrameController controller) {
    Macros macros = controller.getResult();
    if (Objects.nonNull(macros)) {
      Path path = macrosMap.entrySet().stream()
          .filter(e -> e.getKey().equals(macros))
          .findFirst()
          .map(Entry::getValue)
          .orElse(null);
      String macrosJson = macrosManager.macrosToString(macros);
      fileManager.writeToFile(path, macrosJson);
    }
    update();
  }

  @FXML
  void macrosTableOnClick(@NotNull MouseEvent event) {
    if (event.getClickCount() == 2) {
      editButtonOnAction(null);
    }
  }

  @Override
  protected void updateData() {
    macrosDirectoryPath = macrosDirectoryPathTextField.getText();
    macrosMap = getMacrosMap();
  }

  private Map<Macros, Path> getMacrosMap() {
    Path root = fileManager.getRoot(macrosDirectoryPath);
    List<Path> content = fileManager.getContent(root);
    return fileManager.getFiles(content).stream()
        .collect(Collectors.toMap(macrosManager::getMacros, Function.identity()));
  }

  @Override
  protected void updateUI() {
    List<Macros> macrosList = macrosMap.keySet().stream()
        .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
        .collect(Collectors.toList());
    macrosTable.setItems(FXCollections.observableArrayList(macrosList));
    macrosTable.refresh();
  }
}
