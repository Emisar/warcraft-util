package com.anton.soft.desktop.application.local;

import com.anton.soft.desktop.application.dto.RPGTableDTO;
import com.anton.soft.desktop.application.dto.RPGType;
import com.anton.soft.desktop.application.local.entity.Pane;
import com.anton.soft.desktop.application.local.entity.PaneType;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class LocalDataInitializer {

  private static final String PATH_TO_MACROS_PATTERN = "{0}\\macros";
  private static final String PATH_TO_TKOK_PATTERN = "{0}\\TKoK_Save_Files\\TKoK_3.5.15";

  private final LocalData localData;
  private final Class<?> aClass;
  private final Path location;

  public LocalDataInitializer(@NotNull Class<?> aClass) {
    localData = LocalData.getInstance();
    this.aClass = aClass;
    String path = aClass.getProtectionDomain().getCodeSource().getLocation().getPath();
    this.location = Path.of(path.substring(1)).getParent();
  }

  public void init() throws IOException {
    initMacros();
    initRPGLoader();
    initPanes();
  }

  private void initPanes() throws IOException {
    List<Pane> paneList = new ArrayList<>();
    for (PaneType type : PaneType.values()) {
      String path = type.getPath();
      if (StringUtils.isEmpty(path)) {
        continue;
      }
      FXMLLoader loader = new FXMLLoader(aClass.getResource(path));
      AnchorPane node = loader.load();
      paneList.add(new Pane(type, loader, node));
    }
    localData.setPanes(paneList);
  }

  private void initMacros() throws IOException {
    String macrosDirectoryPath = MessageFormat.format(PATH_TO_MACROS_PATTERN, location);
    String decodedPath = URLDecoder.decode(macrosDirectoryPath, StandardCharsets.UTF_8);
    Path macrosDirectory = Path.of(decodedPath);
    if (Files.notExists(macrosDirectory)) {
      Files.createDirectory(macrosDirectory);
    }

    localData.setDefaultMacrosDirectory(decodedPath);
    localData.setIsMacrosActive(Boolean.FALSE);
    localData.setMacrosListenerList(new ArrayList<>());
  }

  private void initRPGLoader() {
    RPGTableDTO tkok = new RPGTableDTO(
        "The Kingdom of Kaliron",
        "3.5.15",
        URLDecoder.decode(MessageFormat.format(PATH_TO_TKOK_PATTERN, location.getParent()), StandardCharsets.UTF_8)
    );
    List<RPGTableDTO> rpgList = List.of(tkok);
    Map<RPGType, RPGTableDTO> rpgMap = Map.of(RPGType.TKOK, tkok);

    localData.setRpgList(rpgList);
    localData.setRpgMap(rpgMap);
  }
}
