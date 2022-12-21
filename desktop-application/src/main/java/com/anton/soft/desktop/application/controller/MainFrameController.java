package com.anton.soft.desktop.application.controller;

import static javafx.collections.FXCollections.observableArrayList;

import com.anton.soft.desktop.application.local.LocalData;
import com.anton.soft.desktop.application.local.entity.Pane;
import com.anton.soft.desktop.application.local.entity.PaneType;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;

public class MainFrameController extends AbstractFrameController {

  @FXML
  private ListView<String> listView;
  @FXML
  private SplitPane splitPane;

  private List<PaneType> paneList;
  private Map<String, Pane> paneMap;
  private Pane selectedPane;

  private LocalData localData;

  @Override
  protected void initUtils(Object... params) {
    localData = LocalData.getInstance();
  }

  @Override
  protected void initData(Object... params) {
    paneList = List.of(
        PaneType.GLOBAL_SETTINGS,
        PaneType.MACROS_SETTINGS,
        PaneType.RPG_LOADER_SETTINGS
    );
    paneMap = paneList.stream()
        .collect(Collectors.toMap(PaneType::getTitle, this::getPane));
    paneMap.values().stream()
        .map(Pane::getLoader)
        .filter(Objects::nonNull)
        .map(FXMLLoader::getController)
        .map(IFrameController.class::cast)
        .forEach(IFrameController::init);
  }

  @NotNull
  private Pane getPane(PaneType paneType) {
    return Optional.ofNullable(localData.getPaneByType(paneType)).orElse(new Pane());
  }

  @Override
  protected void initContext(Object... params) {
    List<String> titleList = paneList.stream()
        .map(PaneType::getTitle)
        .collect(Collectors.toList());
    listView.setItems(observableArrayList(titleList));
    listView.getSelectionModel().selectFirst();
    selectedPane = selectPane(listView.getSelectionModel().getSelectedItem());
  }

  @FXML
  void listViewOnClick(MouseEvent event) {
    update();
  }

  @Override
  protected void updateData() {
    String item = listView.getSelectionModel().getSelectedItem();
    selectedPane = selectPane(item);
  }

  @Override
  protected void updateUI() {
    splitPane.getItems().set(1, selectedPane.getNode());
  }

  @NotNull
  private Pane selectPane(String item) {
    return Optional.ofNullable(paneMap.get(item)).orElse(new Pane());
  }

}