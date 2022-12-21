module application.dto {
  requires lombok;
  requires org.jetbrains.annotations;

  requires key.binding.dto;
  requires key.binding.util;

  exports com.anton.soft.application.dto;
  exports com.anton.soft.application.dto.impl;
}