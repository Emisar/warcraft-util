package com.anton.soft.application.dto.impl;

import com.anton.soft.application.dto.Identifiable;
import com.anton.soft.key.binding.dto.Macros;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IdentifiableMacros extends Macros implements Identifiable<Long> {

  private Long id;
}



