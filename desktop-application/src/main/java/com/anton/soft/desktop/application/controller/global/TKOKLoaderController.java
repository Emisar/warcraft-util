package com.anton.soft.desktop.application.controller.global;

import com.anton.soft.desktop.application.controller.AbstractFrameController;
import com.anton.soft.desktop.application.dto.RPGTableDTO;
import com.anton.soft.desktop.application.dto.RPGType;
import com.anton.soft.desktop.application.local.LocalData;
import com.anton.soft.hero.loader.dto.Account;
import com.anton.soft.hero.loader.dto.Hero;
import com.anton.soft.hero.loader.util.IDataLoader;
import com.anton.soft.hero.loader.util.impl.TkokDataLoader;
import com.anton.soft.util.FileManager;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;

public class TKOKLoaderController extends AbstractFrameController {

  private static final String ACCOUNT_TITLE_PATTERN = "{0}_{1}AP";

  @FXML
  private ComboBox<String> heroClassComboBox;
  @FXML
  private ListView<Hero> heroListView;
  @FXML
  private TextArea heroStatisticTextArea;
  @FXML
  private ListView<String> accountListView;
  @FXML
  private TextArea accountStatisticTextArea;

  private LocalData localData;
  private FileManager fileManager;
  private IDataLoader dataLoader;

  private Map<String, Account> accountMap;
  private Map<String, List<Hero>> heroMap;

  private String loadCode1;
  private String loadCode2;
  private String accountCode;

  @Override
  protected void initUtils(Object... params) {
    localData = LocalData.getInstance();
    fileManager = new FileManager();
    dataLoader = new TkokDataLoader();
  }

  @Override
  protected void initData(Object... params) {
    Map<RPGType, RPGTableDTO> rpgMap = localData.getRpgMap();
    RPGTableDTO tkok = rpgMap.get(RPGType.TKOK);
    String pathToSave = tkok.getPath();
    Path root = fileManager.getRoot(pathToSave);
    List<Path> content = fileManager.getContent(root);
    heroMap = fileManager.getFolders(content).stream()
        .collect(Collectors.toMap(this::getFolderName, this::getHeroList));
    accountMap = fileManager.getFiles(content).stream()
        .map(dataLoader::getAccount)
        .collect(Collectors.toMap(this::getAccountTitle, Function.identity()));
  }

  private String getFolderName(@NotNull Path folder) {
    return String.valueOf(folder.getFileName());
  }

  @NotNull
  private List<Hero> getHeroList(Path folder) {
    List<Path> folderContent = fileManager.getContent(folder);
    List<Path> folderFiles = fileManager.getFiles(folderContent);
    return folderFiles.stream()
        .map(dataLoader::getHero)
        .collect(Collectors.toList());
  }

  @NotNull
  private String getAccountTitle(@NotNull Account account) {
    return MessageFormat.format(ACCOUNT_TITLE_PATTERN, account.getName(), account.getAchievementPoints());
  }

  @Override
  protected void initContext(Object... params) {
    heroClassComboBox.setItems(null);
    heroClassComboBox.setItems(FXCollections.observableArrayList(heroMap.keySet()));
    accountListView.setItems(FXCollections.observableArrayList(accountMap.keySet()));
    heroListView.setCellFactory(param -> new HeroListCell<>());
  }

  @FXML
  void heroClassComboBoxOnAction(ActionEvent event) {
    String selectedClass = heroClassComboBox.getSelectionModel().getSelectedItem();
    List<Hero> heroList = Optional.ofNullable(heroMap.get(selectedClass))
        .orElse(Collections.emptyList());
    heroListView.setItems(FXCollections.observableArrayList(heroList));
  }

  @FXML
  void heroListViewOnClicked(MouseEvent event) {
    String selectedClass = heroClassComboBox.getSelectionModel().getSelectedItem();
    Hero selectedHero = heroListView.getSelectionModel().getSelectedItem();
    List<Hero> heroList = Optional.ofNullable(heroMap.get(selectedClass))
        .orElse(Collections.emptyList());
    heroList.stream()
        .filter(selectedHero::equals)
        .findFirst()
        .map(Hero::prettyString)
        .ifPresent(heroStatisticTextArea::setText);
  }

  @FXML
  void accountListViewOnClicked(MouseEvent event) {
    String selectedItem = accountListView.getSelectionModel().getSelectedItem();
    Account selectedAccount = accountMap.get(selectedItem);
    Optional.ofNullable(selectedAccount)
        .map(Account::prettyString)
        .ifPresent(accountStatisticTextArea::setText);
  }

  @FXML
  void selectButtonOnAction(ActionEvent event) {
    updateData();
    setResult(new Object[] {loadCode1, loadCode2, accountCode});
    windowClose();
  }

  @Override
  protected void updateData() {
    Hero selectedHero = heroListView.getSelectionModel().getSelectedItem();
    String selectedItem = accountListView.getSelectionModel().getSelectedItem();
    Account selectedAccount = accountMap.get(selectedItem);
    loadCode1 = Optional.ofNullable(selectedHero).map(Hero::getLoadCode1).orElse("");
    loadCode2 = Optional.ofNullable(selectedHero).map(Hero::getLoadCode2).orElse("");
    accountCode = Optional.ofNullable(selectedAccount).map(Account::getLoadCode).orElse("");
  }

  @FXML
  void closeButtonOnAction(ActionEvent event) {
    windowClose();
  }

  private static class HeroListCell<T extends Hero> extends ListCell<T> {

    private static final String HERO_TITLE_PATTERN = "{0}({1}Lvl)[{2}]";

    @Override
    protected void updateItem(T item, boolean empty) {
      super.updateItem(item, empty);
      if (Objects.isNull(item) || empty) {
        setText(null);
      } else {
        setText(getHeroTitle(item));
      }
    }

    @NotNull
    private String getHeroTitle(@NotNull Hero hero) {
      return MessageFormat.format(HERO_TITLE_PATTERN, hero.getHeroClass(), hero.getLevel(),
          hero.getGameTime());
    }
  }
}
