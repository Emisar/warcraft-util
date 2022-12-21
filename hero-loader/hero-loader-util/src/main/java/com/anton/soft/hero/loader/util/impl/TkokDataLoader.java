package com.anton.soft.hero.loader.util.impl;

import static java.lang.Integer.valueOf;

import com.anton.soft.hero.loader.dto.Account;
import com.anton.soft.hero.loader.dto.Hero;
import com.anton.soft.hero.loader.util.IDataLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public class TkokDataLoader implements IDataLoader {

  private static final Logger LOGGER = Logger.getLogger(TkokDataLoader.class.getName());
  private static final String CANNOT_READ_FROM_FILE = "Cannot read from file: {0}";

  @Override
  public Account getAccount(Path path) {
    try {
      List<String> lines = Files.readAllLines(path);
      return accountMapper(lines);
    } catch (IOException e) {
      LOGGER.log(Level.ALL, MessageFormat.format(CANNOT_READ_FROM_FILE, e.getMessage()));
    }
    return null;
  }

  @Override
  public Hero getHero(Path path) {
    try {
      List<String> lines = Files.readAllLines(path);
      lines.add(path.getFileName().toString());
      return characterMapper(lines);
    } catch (IOException e) {
      LOGGER.log(Level.ALL, MessageFormat.format(CANNOT_READ_FROM_FILE, e.getMessage()));
    }
    return null;
  }

  @NotNull
  private Account accountMapper(@NotNull List<String> list) {
    String name = list.get(4).substring(6);
    Integer achievementPoints = valueOf(list.get(6).substring(5));
    Integer dedicationPoints = valueOf(list.get(8).substring(10));
    String loadCode = list.get(11);
    return new Account(name, achievementPoints, dedicationPoints, loadCode);
  }

  @NotNull
  private Hero characterMapper(@NotNull List<String> list) {
    Pattern gameTimePattern = Pattern.compile("\\d+hrs\\d+mins");
    String gameTime = Stream.of(list.get(list.size() - 1))
        .map(gameTimePattern::matcher)
        .filter(Matcher::find).map(Matcher::group).findFirst().orElse("");
    Pattern pattern = Pattern.compile("(:\\s.+\")|(\\s{2}-l2\\s.+\")");
    List<String> formattedList = list.stream().map(pattern::matcher).filter(Matcher::find)
        .map(Matcher::group).map(s -> s.substring(2, s.length() - 1)).collect(Collectors.toList());
    String name = formattedList.get(0);
    String hero = formattedList.get(1);
    Integer level = valueOf(formattedList.get(2));
    Integer experience = valueOf(formattedList.get(3));
    Integer gold = valueOf(formattedList.get(4));
    Integer starGlass = valueOf(formattedList.get(5));
    String loadCode1 = formattedList.get(6);
    String loadCode2 = formattedList.get(7);
    return new Hero(name, gameTime, hero, level, experience, gold, starGlass, loadCode1, loadCode2);
  }

}
