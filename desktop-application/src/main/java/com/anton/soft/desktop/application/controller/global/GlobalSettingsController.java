package com.anton.soft.desktop.application.controller.global;

import com.anton.soft.desktop.application.controller.AbstractFrameController;
import com.anton.soft.desktop.application.controller.IFrameController;
import com.anton.soft.desktop.application.local.LocalData;
import com.anton.soft.desktop.application.local.entity.Pane;
import com.anton.soft.desktop.application.local.entity.PaneType;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

public class GlobalSettingsController extends AbstractFrameController {

  @FXML
  private VBox contentVBox;

  private LocalData localData;

  private List<Pane> paneList;

  @Override
  protected void initUtils(Object... params) {
    localData = LocalData.getInstance();
  }

  @Override
  protected void initData(Object... params) {
    paneList = List.of(
        getPane(PaneType.GLOBAL_SETTINGS_MACROS),
        getPane(PaneType.GLOBAL_SETTINGS_RPG_LOADER)
    );
  }

  @Override
  protected void initContext(Object... params) {
    paneList.stream()
        .map(Pane::getLoader)
        .filter(Objects::nonNull)
        .map(FXMLLoader::getController)
        .map(IFrameController.class::cast)
        .forEach(IFrameController::init);
    paneList.stream()
        .map(Pane::getNode)
        .filter(Objects::nonNull)
        .forEach(contentVBox.getChildren()::add);
  }

  @NotNull
  private Pane getPane(PaneType paneType) {
    return Optional.ofNullable(localData.getPaneByType(paneType)).orElse(new Pane());
  }
}
