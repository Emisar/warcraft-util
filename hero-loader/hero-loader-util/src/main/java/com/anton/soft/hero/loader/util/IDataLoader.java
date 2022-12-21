package com.anton.soft.hero.loader.util;

import com.anton.soft.hero.loader.dto.Account;
import com.anton.soft.hero.loader.dto.Hero;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Allow gain information about player {@link Account account} and {@link Hero character}.
 * <p>
 * All data is taken from text-files by {@link Path path} object.
 *
 * @see Account
 * @see Hero
 */

public interface IDataLoader {

  /**
   * Takes data from a file at the specified path and converts it to new {@link Account account}
   * object.
   *
   * @param path path to the data-file
   * @return a new {@link Account account} object
   * @throws IOException if an I/O error occurs
   */
  Account getAccount(Path path);

  /**
   * Takes data from a file at the specified path and converts it to new {@link Hero character}
   * object.
   *
   * @param path path to the data-file
   * @return a new {@link Hero character} object
   * @throws IOException if an I/O error occurs
   */
  Hero getHero(Path path);
}
