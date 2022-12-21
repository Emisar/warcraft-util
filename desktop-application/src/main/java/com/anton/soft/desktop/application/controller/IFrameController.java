package com.anton.soft.desktop.application.controller;

public interface IFrameController {

  void init(Object... params);

  void update();

  <T> T getResult();
}
