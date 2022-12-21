package com.anton.soft.desktop.application;

import com.anton.soft.desktop.application.controller.IFrameController;
import com.anton.soft.desktop.application.local.LocalData;
import com.anton.soft.desktop.application.local.LocalDataInitializer;
import com.anton.soft.desktop.application.local.entity.Pane;
import com.anton.soft.desktop.application.local.entity.PaneType;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import java.io.IOException;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

public class MainApplication extends Application {

  private static final String TITLE = "Warcraft Util";

  private static LocalDataInitializer dataInitializer;

  @Override
  public void start(Stage stage) throws IOException, NativeHookException {
    dataInitializer.init();
    GlobalScreen.registerNativeHook();
    GlobalScreen.setEventDispatcher(Executors.newCachedThreadPool());
    loadWindow(stage);
  }

  private void loadWindow(@NotNull Stage stage) {
    LocalData localData = LocalData.getInstance();
    Pane pane = localData.getPaneByType(PaneType.MAIN_FRAME);
    pane.getLoader().<IFrameController>getController().init();
    Scene scene = new Scene(pane.getNode());
    stage.setTitle(TITLE);
    stage.setScene(scene);
    stage.setOnCloseRequest(this::unregisterNativeHook);
    stage.setResizable(false);
    stage.show();
  }

  private void unregisterNativeHook(WindowEvent event) {
    try {
      GlobalScreen.unregisterNativeHook();
    } catch (NativeHookException ex) {
      ex.printStackTrace();
    }
  }

  public static void main(String[] args) {
    dataInitializer = new LocalDataInitializer(MainApplication.class);
    launch();
  }
}