module key.binding.util {
  requires lombok;
  requires com.google.gson;
  requires org.jetbrains.annotations;
  requires org.apache.commons.lang3;
  requires org.apache.commons.io;
  requires key.binding.dto;
  requires java.logging;
  requires transitive java.desktop;
  requires transitive com.github.kwhat.jnativehook;

  exports com.anton.soft.key.binding.util;
}