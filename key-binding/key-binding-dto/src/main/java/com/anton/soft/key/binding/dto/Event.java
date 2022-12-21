package com.anton.soft.key.binding.dto;

import com.anton.soft.key.binding.dto.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

  @Builder.Default
  private EventType eventType = EventType.MOUSE_CLICK;
  @Builder.Default
  private String value = "NOBUTTON";
}
