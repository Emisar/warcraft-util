package com.anton.soft.desktop.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RPGTableDTO {

  private String title;
  private String version;
  private String path;
}
