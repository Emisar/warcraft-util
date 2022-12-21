package com.anton.soft.desktop.application.controller.loader;

import static javafx.collections.FXCollections.observableArrayList;

import com.anton.soft.desktop.application.controller.AbstractFrameController;
import com.anton.soft.desktop.application.dto.RPGTableDTO;
import com.anton.soft.desktop.application.local.LocalData;
import com.anton.soft.desktop.application.util.FrameManager;
import com.anton.soft.desktop.application.util.IFrameManager;
import java.io.File;
import java.util.List;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

public class RPGLoaderSettingsController extends AbstractFrameController {

  @FXML
  private Button editButton;
  @FXML
  private TableView<RPGTableDTO> rpgTable;
  @FXML
  private TableColumn<RPGTableDTO, String> rpgTableTitleColumn;
  @FXML
  private TableColumn<RPGTableDTO, String> rpgTableVersionColumn;
  @FXML
  private TableColumn<RPGTableDTO, String> rpgTablePathColumn;

  private LocalData localData;
  private IFrameManager frameManager;
  private List<RPGTableDTO> rpgList;

  @Override
  protected void initUtils(Object... params) {
    localData = LocalData.getInstance();
    frameManager = new FrameManager();
  }

  @Override
  protected void initData(Object... params) {
    rpgList = localData.getRpgList();
  }

  @Override
  protected void initContext(Object... params) {
    rpgTableTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    rpgTableVersionColumn.setCellValueFactory(new PropertyValueFactory<>("version"));
    rpgTablePathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));
    rpgTable.setItems(observableArrayList(rpgList));
  }

  @FXML
  void editButtonOnAction(ActionEvent event) {
    Window window = anchorPane.getScene().getWindow();
    RPGTableDTO rpg = rpgTable.getSelectionModel().getSelectedItem();
    File file = frameManager.chooseDirectory(window, rpg.getPath());
    if (Objects.nonNull(file)) {
      rpg.setPath(file.getAbsolutePath());
      update();
    }
  }

  @FXML
  void rpgTableOnClick(@NotNull MouseEvent event) {
    if (event.getClickCount() == 2) {
      editButtonOnAction(null);
    }
  }

  @Override
  protected void updateUI() {
    rpgTable.refresh();
  }

}
