package com.anton.soft.desktop.application.util;

import java.io.File;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class FrameManager implements IFrameManager {

  private static final int NEW_WINDOW_OFFSET_X = 50;
  private static final int NEW_WINDOW_OFFSET_Y = 50;

  @Override
  public File chooseDirectory(Window window) {
    DirectoryChooser chooser = new DirectoryChooser();
    return chooser.showDialog(window);
  }

  @Override
  public File chooseFile(Window window) {
    FileChooser chooser = new FileChooser();
    return chooser.showOpenDialog(window);
  }

  @Override
  public List<File> chooseFiles(Window window) {
    FileChooser chooser = new FileChooser();
    return chooser.showOpenMultipleDialog(window);
  }

  @Override
  public File chooseDirectory(Window window, String defaultDir) {
    DirectoryChooser chooser = new DirectoryChooser();
    File defaultFile = new File(defaultDir);
    if (defaultFile.exists()) {
      chooser.setInitialDirectory(defaultFile);
    }
    return chooser.showDialog(window);
  }

  @Override
  public File chooseFile(Window window, String defaultDir) {
    FileChooser chooser = new FileChooser();
    File defaultFile = new File(defaultDir);
    if (defaultFile.exists()) {
      chooser.setInitialDirectory(defaultFile);
    }
    return chooser.showOpenDialog(window);
  }

  @Override
  public List<File> chooseFiles(Window window, String defaultDir) {
    FileChooser chooser = new FileChooser();
    File defaultFile = new File(defaultDir);
    if (defaultFile.exists()) {
      chooser.setInitialDirectory(defaultFile);
    }
    return chooser.showOpenMultipleDialog(window);
  }

  @Override
  public Alert createMessageFrame(AlertType type, String title, String header, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);
    return alert;
  }

  @Override
  public Stage createWindow(Window owner, AnchorPane anchorPane, String title) {
    Stage stage = new Stage();
    stage.setScene(new Scene(anchorPane));
    stage.setTitle(title);
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(owner);
    stage.setX(owner.getX() + NEW_WINDOW_OFFSET_X);
    stage.setY(owner.getY() + NEW_WINDOW_OFFSET_Y);
    stage.setResizable(false);
    return stage;
  }
}
