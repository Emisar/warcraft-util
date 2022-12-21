package com.anton.soft.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;

/**
 * Allows loading properties from property-files.
 */

public class PropertiesLoader {

  /**
   * Read property list from file at the specified path and return {@link Properties} object.
   * This method used an {@link InputStream} for reading.
   *
   * @param path path to the property file
   * @return the {@link Properties property} object
   */
  public Properties loadProperties(String path) {
    Properties properties = new Properties();
    ClassLoader classLoader = PropertiesLoader.class.getClassLoader();
    try (InputStream stream = classLoader.getResourceAsStream(path)) {
      properties.load(stream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return properties;
  }

  /**
   * Read property list from file at the specified path and return {@link Properties} object.
   * This method used an {@link InputStream} for reading.
   *
   * @param path path to the property file
   * @return the {@link Properties property} object
   */
  public Properties loadProperties(@NotNull Path path) {
    return loadProperties(path.toString());
  }

  public void saveProperties(@NotNull Properties properties, String path) {
    try (OutputStream stream = new FileOutputStream(path)) {
      properties.store(stream, null);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveProperties(Properties properties, @NotNull Path path) {
    saveProperties(properties, path.toString());
  }
}
