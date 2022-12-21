package com.anton.soft.desktop.application.local;

import com.anton.soft.desktop.application.dto.RPGTableDTO;
import com.anton.soft.desktop.application.dto.RPGType;
import com.anton.soft.desktop.application.local.entity.Pane;
import com.anton.soft.desktop.application.local.entity.PaneType;
import com.anton.soft.key.binding.util.MacrosListener;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import lombok.Data;

@Data
public class LocalData {

  private static LocalData instance;

  private String defaultMacrosDirectory;
  private Boolean isMacrosActive;
  private List<MacrosListener> macrosListenerList;
  private Map<RPGType, RPGTableDTO> rpgMap;
  private List<RPGTableDTO> rpgList;
  private List<Pane> panes;
  private Map<PaneType, AnchorPane> paneMap;
  private Map<PaneType, FXMLLoader> loaderMap;

  private LocalData() {
    // Singleton
  }

  public static LocalData getInstance() {
    if (instance == null) {
      instance = new LocalData();
    }
    return instance;
  }

  public Pane getPaneByType(PaneType type) {
    return panes.stream()
        .filter(pane -> pane.getPaneType().equals(type))
        .findFirst()
        .orElse(null);
  }

}
