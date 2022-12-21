package com.anton.soft.hero.loader.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO-class that contains a data about player account.
 *
 * @see java.lang.Character
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

  /**
   * Player account name.
   */
  private String name;
  /**
   * Points that player gains for complete achievements.
   */
  private Integer achievementPoints;
  /**
   * Points that player gains for... I don't know...
   */
  private Integer dedicationPoints;
  /**
   * Code to load player account data in game.
   */
  private String loadCode;

  public String prettyString() {
    return "Account: " + name + '\n' +
        "Achievement Points: " + achievementPoints + '\n' +
        "Dedication Points: " + dedicationPoints + '\n' +
        "Load Code: " + loadCode;
  }

}
