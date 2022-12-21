module application.service {
  requires application.dto;
  requires application.repository;
  requires lombok;

  exports com.anton.soft.application.service;
  exports com.anton.soft.application.service.impl;
}