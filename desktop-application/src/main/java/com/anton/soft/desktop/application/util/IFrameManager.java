package com.anton.soft.desktop.application.util;

import java.io.File;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public interface IFrameManager {

  File chooseDirectory(Window window);

  File chooseDirectory(Window window, String defaultDir);

  File chooseFile(Window window);

  File chooseFile(Window window, String defaultDir);

  List<File> chooseFiles(Window window);

  List<File> chooseFiles(Window window, String defaultDir);

  Alert createMessageFrame(AlertType type, String title, String header, String content);

  Stage createWindow(Window owner, AnchorPane anchorPane, String title);
}
