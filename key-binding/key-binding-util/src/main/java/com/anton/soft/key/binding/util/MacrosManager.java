package com.anton.soft.key.binding.util;

import com.anton.soft.key.binding.dto.Macros;
import com.anton.soft.key.binding.dto.enums.EventType;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MacrosManager {

  private static final Logger LOGGER = Logger.getLogger(MacrosManager.class.getName());

  private static final String ERROR_WHILE_READ_FILE = "Error while read file: {0}";
  private static final String JSON_EXTENSION = "json";

  public String macrosToString(Macros macros) {
    return new Gson().toJson(macros);
  }

  @Nullable
  public Macros getMacros(@NotNull Path path) {
    if (Files.exists(path) && FilenameUtils.isExtension(path.toString(), JSON_EXTENSION)) {
      try {
        String json = Files.readString(path);
        return new Gson().fromJson(json, Macros.class);
      } catch (IOException e) {
        LOGGER.log(Level.ALL, MessageFormat.format(ERROR_WHILE_READ_FILE, e.getMessage()));
      }
    }
    return null;
  }

  public MacrosListener createMacrosListener(Macros macros) {
    return new MacrosListener(macros, new RobotManager(), this);
  }

  public void addMacrosListener(@NotNull MacrosListener macrosListener) {
    EventType eventType = macrosListener.getMacros().getEvent().getEventType();
    Map<EventType, Consumer<MacrosListener>> options = Map.of(
        EventType.BUTTON_PRESS, GlobalScreen::addNativeKeyListener,
        EventType.MOUSE_CLICK, GlobalScreen::addNativeMouseListener);
    Optional.ofNullable(options.get(eventType)).ifPresent(c -> c.accept(macrosListener));
  }

  public void removeMacrosListener(@NotNull MacrosListener macrosListener) {
    EventType eventType = macrosListener.getMacros().getEvent().getEventType();
    Map<EventType, Consumer<MacrosListener>> options = Map.of(
        EventType.BUTTON_PRESS, GlobalScreen::removeNativeKeyListener,
        EventType.MOUSE_CLICK, GlobalScreen::removeNativeMouseListener);
    Optional.ofNullable(options.get(eventType)).ifPresent(c -> c.accept(macrosListener));
  }
}
