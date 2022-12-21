package com.anton.soft.hero.loader.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO-class that contains a data about player character.
 *
 * @see Account
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Hero {

  /**
   * Player account name.
   */
  private String name;
  /**
   * Total game time
   */
  private String gameTime;
  /**
   * Class of the hero.
   */
  private String heroClass;
  /**
   * Level of the hero.
   */
  private Integer level;
  /**
   * Total amount of experience.
   */
  private Integer experience;
  /**
   * Total amount of gold.
   */
  private Integer gold;
  /**
   * I don't know.
   */
  private Integer starGlass;
  /**
   * First part of load code. <br> This part contain information about hero himself.
   */
  private String loadCode1;
  /**
   * Second part of load code. <br> This part contain information about hero inventory.
   */
  private String loadCode2;

  public String prettyString() {
    return "Name: " + name + '\n' +
        "Game Time: " + gameTime + '\n' +
        "Hero Class: " + heroClass + '\n' +
        "Level: " + level + '\n' +
        "Experience: " + experience + '\n' +
        "Gold: " + gold + '\n' +
        "Star Glass: " + starGlass + '\n' +
        "Load Code 1: " + loadCode1 + '\n' +
        "Load Code 2: " + loadCode2;
  }
}
