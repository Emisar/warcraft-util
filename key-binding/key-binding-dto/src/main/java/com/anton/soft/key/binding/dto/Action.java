package com.anton.soft.key.binding.dto;

import com.anton.soft.key.binding.dto.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Action {

  @Builder.Default
  private ActionType actionType = ActionType.PRESS_THE_MOUSE_BUTTON;
  @Builder.Default
  private String value = "NOBUTTON";
}
