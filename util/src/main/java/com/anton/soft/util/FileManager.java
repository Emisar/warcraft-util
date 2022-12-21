package com.anton.soft.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

/**
 * Allows to perform an operation with files such as get content by the specified path.
 * <p>
 * All methods return a {@link Path path} objects.
 */
public class FileManager {

  private static final Logger LOGGER = Logger.getLogger(FileManager.class.getName());

  private static final String CANNOT_OPEN_DIRECTORY = "Cannot open directory: {0}";
  private static final String CANNOT_WRITE_TO_FILE = "Cannot write to file: {0}";
  private static final String CANNOT_DELETE_FILE = "Cannot delete file: {0}";

  /**
   * Return path to the file as a {@link Path} object.
   *
   * @param path path to the file
   * @return a new {@link Path path} object
   */
  public Path getRoot(String path) {
    return Paths.get(path);
  }

  /**
   * Return a list of file that are contained by the specified path.
   *
   * @param root path to the file
   * @return list of {@link Path path} objects
   */
  public List<Path> getContent(Path root) {
    if (Files.notExists(root)) {
      return Collections.emptyList();
    }
    try (Stream<Path> paths = Files.list(root)) {
      return paths.collect(Collectors.toList());
    } catch (IOException e) {
      LOGGER.log(Level.ALL, MessageFormat.format(CANNOT_OPEN_DIRECTORY, e.getMessage()));
    }
    return Collections.emptyList();
  }

  /**
   * Filters the list of files and leaves folders only.
   *
   * @param files list of files to filter
   * @return list of {@link Path path} objects or empty list. All objects are folders.
   */
  public List<Path> getFolders(@NotNull List<Path> files) {
    return files.stream()
        .filter(Files::exists)
        .filter(Files::isDirectory)
        .collect(Collectors.toList());
  }

  /**
   * Filters the list of files and remove all folders.
   *
   * @param files list of files to filter
   * @return list of {@link Path path} objects or empty list. All objects are not folders.
   */
  public List<Path> getFiles(@NotNull List<Path> files) {
    return files.stream()
        .filter(Files::exists)
        .filter(path -> !Files.isDirectory(path))
        .collect(Collectors.toList());
  }

  public void writeToFile(Path path, String content) {
    try {
      Files.writeString(path, content, StandardCharsets.UTF_8);
    } catch (IOException e) {
      LOGGER.log(Level.ALL, MessageFormat.format(CANNOT_WRITE_TO_FILE, e.getMessage()));
    }
  }

  public boolean deleteFile(Path path) {
    try {
      return Files.deleteIfExists(path);
    } catch (IOException e) {
      LOGGER.log(Level.ALL, MessageFormat.format(CANNOT_DELETE_FILE, e.getMessage()));
    }
    return false;
  }
}
