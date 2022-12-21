package com.anton.soft.key.binding.util;

import com.anton.soft.key.binding.dto.Action;
import com.anton.soft.key.binding.dto.Macros;
import com.anton.soft.key.binding.dto.enums.ActionType;
import com.anton.soft.key.binding.dto.enums.EventType;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class MacrosListener implements NativeKeyListener, NativeMouseListener {

  private static final Logger LOGGER = Logger.getLogger(MacrosListener.class.getName());

  private static final String CAN_NOT_GET_ACCESS_TO_FILED = "Can not get access to filed: {0}";

  public static volatile boolean isPressed = false;
  public static volatile boolean isRunning = false;

  @Getter
  private final Macros macros;
  private final RobotManager robotManager;
  private final MacrosManager macrosManager;

  @Override
  public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
    if (checkEvent(EventType.BUTTON_PRESS) && checkEventValue(nativeEvent) && !isPressed && !isRunning) {
      setPressed(true);
      setRunning(true);
      run();
      setRunning(false);
    }
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
    if (checkEvent(EventType.BUTTON_PRESS) && checkEventValue(nativeEvent)) {
      setPressed(false);
    }
  }

  @Override
  public void nativeMousePressed(NativeMouseEvent nativeEvent) {
    if (checkEvent(EventType.MOUSE_CLICK) && checkEventValue(nativeEvent) && !isPressed && !isRunning) {
      setPressed(true);
      setRunning(true);
      run();
      setRunning(false);
    }
  }

  @Override
  public void nativeMouseReleased(NativeMouseEvent nativeEvent) {
    if (checkEvent(EventType.MOUSE_CLICK) && checkEventValue(nativeEvent)) {
      setPressed(false);
    }
  }

  private static synchronized void setPressed(boolean isPressed) {
    MacrosListener.isPressed = isPressed;
  }

  private static synchronized void setRunning(boolean isRunning) {
    MacrosListener.isRunning = isRunning;
  }

  private boolean checkEvent(EventType eventType) {
    return macros.getEvent().getEventType() == eventType;
  }

  private boolean checkEventValue(@NotNull NativeKeyEvent nativeEvent) {
    return nativeEvent.getKeyCode() == getEventKeyCodeValue();
  }

  private boolean checkEventValue(@NotNull NativeMouseEvent nativeEvent) {
    return nativeEvent.getButton() == getEventMouseCodeValue();
  }

  private int getEventKeyCodeValue() {
    String value = "VC_" + macros.getEvent().getValue().toUpperCase(Locale.ROOT);
    try {
      return NativeKeyEvent.class.getField(value).getInt(null);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      String msg = MessageFormat.format(CAN_NOT_GET_ACCESS_TO_FILED, e.getMessage());
      LOGGER.log(Level.ALL, msg);
    }
    return -1;
  }

  private int getEventMouseCodeValue() {
    String value = macros.getEvent().getValue().toUpperCase(Locale.ROOT);
    try {
      return NativeMouseEvent.class.getField(value).getInt(null);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      String msg = MessageFormat.format(CAN_NOT_GET_ACCESS_TO_FILED, e.getMessage());
      LOGGER.log(Level.ALL, msg);
    }
    return -1;
  }

  private int getActionKeyCodeValue(@NotNull Action action) {
    String value = "VK_" + action.getValue().toUpperCase(Locale.ROOT);
    try {
      return KeyEvent.class.getField(value).getInt(null);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      String msg = MessageFormat.format(CAN_NOT_GET_ACCESS_TO_FILED, e.getMessage());
      LOGGER.log(Level.ALL, msg);
    }
    return -1;
  }

  private int getActionMouseCodeValue(@NotNull Action action) {
    String value = action.getValue().toUpperCase(Locale.ROOT);
    try {
      return MouseEvent.class.getField(value).getInt(null);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      String msg = MessageFormat.format(CAN_NOT_GET_ACCESS_TO_FILED, e.getMessage());
      LOGGER.log(Level.ALL, msg);
    }
    return -1;
  }

  public void run() {
    for (Action action : macros.getActions()) {
      runAction(action);
    }
    if (macros.getOneTime().equals(Boolean.TRUE)) {
      macrosManager.removeMacrosListener(this);
    }
  }

  public void runAction(@NotNull Action action) {
    ActionType actionType = action.getActionType();
    if (actionType == ActionType.PRESS_THE_KEYBOARD_BUTTON) {
      int value = getActionKeyCodeValue(action);
      robotManager.keyType(value);
    } else if (actionType == ActionType.PRESS_THE_MOUSE_BUTTON) {
      int value = getActionMouseCodeValue(action);
      robotManager.mouseClick(value);
    } else if (actionType == ActionType.COPY_TO_CLIPBOARD) {
      String value = action.getValue();
      robotManager.copyToClipboard(value);
    } else if (actionType == ActionType.PARSE_FROM_CLIPBOARD) {
      robotManager.parseFromClipboard();
    } else if (actionType == ActionType.CLOSE) {
      robotManager.closeApp();
    } else if (actionType == ActionType.WAIT_MS) {
      Integer value = Integer.valueOf(action.getValue());
      robotManager.waitMs(value);
    }
  }
}
