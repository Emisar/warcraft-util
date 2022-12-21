package com.anton.soft.key.binding.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Macros {

  @Builder.Default
  private String name = "Macros";
  @Builder.Default
  private Event event = new Event();
  @Builder.Default
  private Boolean status = false;
  @Builder.Default
  private Boolean oneTime = false;
  @Builder.Default
  private List<Action> actions = new ArrayList<>();
}
