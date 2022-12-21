package com.anton.soft.key.binding.util;

import static java.lang.System.exit;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RobotManager {

  private static final Logger LOGGER = Logger.getLogger(RobotManager.class.getName());

  private static final int AUTO_DELAY = 50;
  private static final String CAN_NOT_INITIALIZE_ROBOT = "Can not initialize Robot: {0}";

  private Robot robot;

  public RobotManager() {
    try {
      this.robot = new Robot();
      robot.setAutoDelay(AUTO_DELAY);
    } catch (AWTException e) {
      LOGGER.log(Level.ALL, MessageFormat.format(CAN_NOT_INITIALIZE_ROBOT, e.getMessage()));
    }
  }

  public void keyType(Integer keyCode) {
    robot.keyPress(keyCode);
    robot.keyRelease(keyCode);
  }

  public void mouseClick(Integer keyCode) {
    robot.mousePress(keyCode);
    robot.mouseRelease(keyCode);
  }

  public void closeApp() {
    exit(0);
  }

  public void waitMs(Integer waitingTime) {
    robot.delay(waitingTime);
  }

  public void parseFromClipboard() {
    robot.keyPress(KeyEvent.VK_CONTROL);
    robot.keyPress(KeyEvent.VK_V);
    robot.keyRelease(KeyEvent.VK_V);
    robot.keyRelease(KeyEvent.VK_CONTROL);
  }

  public void copyToClipboard(String text) {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(new StringSelection(text), null);
  }
}
