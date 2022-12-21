module application.repository {
  requires application.dto;
  requires lombok;
  requires org.jetbrains.annotations;

  exports com.anton.soft.application.repository;
  exports com.anton.soft.application.repository.impl;
}