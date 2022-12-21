package com.anton.soft.desktop.application.controller.global;

import com.anton.soft.desktop.application.controller.AbstractFrameController;
import com.anton.soft.desktop.application.controller.IFrameController;
import com.anton.soft.desktop.application.local.LocalData;
import com.anton.soft.desktop.application.local.entity.Pane;
import com.anton.soft.desktop.application.local.entity.PaneType;
import com.anton.soft.desktop.application.util.FrameManager;
import com.anton.soft.desktop.application.util.IFrameManager;
import com.anton.soft.key.binding.dto.Action;
import com.anton.soft.key.binding.dto.Event;
import com.anton.soft.key.binding.dto.Macros;
import com.anton.soft.key.binding.dto.enums.ActionType;
import com.anton.soft.key.binding.dto.enums.EventType;
import com.anton.soft.key.binding.util.MacrosListener;
import com.anton.soft.key.binding.util.MacrosManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class GlobalSettingsRPGLoaderController extends AbstractFrameController {

  private static final String COPY_MESSAGE_FRAME_TITLE = "Copy done!";
  private static final String COPY_MESSAGE_FRAME_TEXT = "Text copied to clipboard.";
  private static final String ENTER = "ENTER";

  @FXML
  private TextField loadHotkeyTextField;
  @FXML
  private ComboBox<PaneType> selectRpgComboBox;
  @FXML
  private TextField loadCode1TextField;
  @FXML
  private TextField loadCode2TextField;
  @FXML
  private TextField accountCodeTextField;
  @FXML
  private Button selectRpgButton;
  @FXML
  private Button copyLoadCode1Button;
  @FXML
  private Button copyLoadCode2Button;
  @FXML
  private Button copyAccountCodeButton;
  @FXML
  private Button clearButton;
  @FXML
  private Button loadButton;

  private LocalData localData;
  private IFrameManager frameManager;
  private MacrosManager macrosManager;

  private List<PaneType> paneList;
  private Map<PaneType, Pane> paneMap;
  private Map<PaneType, Stage> stageMap;
  private String loadCode1;
  private String loadCode2;
  private String accountCode;
  private MacrosListener macrosListener;

  @Override
  protected void initUtils(Object... params) {
    localData = LocalData.getInstance();
    frameManager = new FrameManager();
    macrosManager = new MacrosManager();
  }

  @Override
  protected void initData(Object... params) {
    paneList = List.of(
        PaneType.TKOK_LOADER
    );
    paneMap = paneList.stream()
        .collect(Collectors.toMap(Function.identity(), localData::getPaneByType));
    stageMap = new EnumMap<>(PaneType.class);
  }

  @Override
  protected void initContext(Object... params) {
    selectRpgComboBox.setItems(FXCollections.observableArrayList(paneList));
    selectRpgComboBox.getSelectionModel().selectFirst();
    selectRpgComboBox.setCellFactory(c -> new ListCell<>() {
      @Override
      protected void updateItem(PaneType item, boolean empty) {
        super.updateItem(item, empty);
        if (Objects.isNull(item) || empty) {
          setText(null);
        } else {
          setText(item.getTitle());
        }
      }
    });
  }

  @FXML
  void selectRpgButtonOnAction(ActionEvent event) {
    PaneType selectedItem = selectRpgComboBox.getSelectionModel().getSelectedItem();
    Pane pane = paneMap.get(selectedItem);
    Stage stage = stageMap.get(selectedItem);
    IFrameController controller = pane.getLoader().getController();
    controller.init();
    if (Objects.isNull(stage)) {
      stage = frameManager.createWindow(
          anchorPane.getScene().getWindow(),
          paneMap.get(selectedItem).getNode(),
          selectedItem.getTitle()
      );
    }
    stage.setOnHiding(e -> onCloseAction(controller));
    stage.show();
  }

  private void onCloseAction(@NotNull IFrameController controller) {
    Object[] result = controller.getResult();
    loadCode1 = (String) result[0];
    loadCode2 = (String) result[1];
    accountCode = (String) result[2];
    updateUI();
  }

  @Override
  protected void updateUI() {
    loadCode1TextField.setText(loadCode1);
    loadCode2TextField.setText(loadCode2);
    accountCodeTextField.setText(accountCode);
  }

  @FXML
  void copyAccountCodeButtonOnAction(ActionEvent event) {
    copyToClipboard(accountCodeTextField.getText());
    Alert messageFrame = frameManager.createMessageFrame(AlertType.INFORMATION,
        COPY_MESSAGE_FRAME_TITLE,
        null,
        COPY_MESSAGE_FRAME_TEXT);
    messageFrame.initOwner(anchorPane.getScene().getWindow());
    messageFrame.showAndWait();
  }

  @FXML
  void copyLoadCode1ButtonOnAction(ActionEvent event) {
    copyToClipboard(loadCode1TextField.getText());
    Alert messageFrame = frameManager.createMessageFrame(AlertType.INFORMATION,
        COPY_MESSAGE_FRAME_TITLE,
        null,
        COPY_MESSAGE_FRAME_TEXT);
    messageFrame.initOwner(anchorPane.getScene().getWindow());
    messageFrame.showAndWait();
  }

  @FXML
  void copyLoadCode2ButtonOnAction(ActionEvent event) {
    copyToClipboard(loadCode2TextField.getText());
    Alert messageFrame = frameManager.createMessageFrame(AlertType.INFORMATION,
        COPY_MESSAGE_FRAME_TITLE,
        null,
        COPY_MESSAGE_FRAME_TEXT);
    messageFrame.initOwner(anchorPane.getScene().getWindow());
    messageFrame.showAndWait();
  }

  @FXML
  void clearButtonOnAction(ActionEvent event) {
    loadCode1TextField.clear();
    loadCode2TextField.clear();
    accountCodeTextField.clear();
  }

  @FXML
  void loadButtonOnAction(ActionEvent event) {
    if (Objects.nonNull(macrosListener)) {
      macrosManager.removeMacrosListener(macrosListener);
    }
    Macros loadMacros = createLoadMacros();
    macrosListener = macrosManager.createMacrosListener(loadMacros);
    macrosManager.addMacrosListener(macrosListener);
  }

  private void copyToClipboard(String text) {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(new StringSelection(text), null);
  }

  @NotNull
  private Macros createLoadMacros() {
    Macros macros = new Macros();
    macros.setName("Hero Loader");
    macros.setStatus(true);
    macros.setOneTime(true);
    macros.setEvent(new Event(EventType.BUTTON_PRESS, loadHotkeyTextField.getText()));
    List<Action> actionList = List.of(
        new Action(ActionType.COPY_TO_CLIPBOARD, loadCode1TextField.getText()),
        new Action(ActionType.PRESS_THE_KEYBOARD_BUTTON, ENTER),
        new Action(ActionType.PARSE_FROM_CLIPBOARD, null),
        new Action(ActionType.PRESS_THE_KEYBOARD_BUTTON, ENTER),
        new Action(ActionType.WAIT_MS, "1000"),
        new Action(ActionType.COPY_TO_CLIPBOARD, loadCode2TextField.getText()),
        new Action(ActionType.PRESS_THE_KEYBOARD_BUTTON, ENTER),
        new Action(ActionType.PARSE_FROM_CLIPBOARD, null),
        new Action(ActionType.PRESS_THE_KEYBOARD_BUTTON, ENTER),
        new Action(ActionType.WAIT_MS, "1000"),
        new Action(ActionType.COPY_TO_CLIPBOARD, accountCodeTextField.getText()),
        new Action(ActionType.PRESS_THE_KEYBOARD_BUTTON, ENTER),
        new Action(ActionType.PARSE_FROM_CLIPBOARD, null),
        new Action(ActionType.PRESS_THE_KEYBOARD_BUTTON, ENTER)
    );
    macros.setActions(actionList);
    return macros;
  }

}
