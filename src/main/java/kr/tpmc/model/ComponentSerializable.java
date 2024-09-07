package kr.tpmc.model;

import net.kyori.adventure.text.Component;

public interface ComponentSerializable {
    String toJsonString();
    String toString();
    String toLagacyString();
    String toMiniMessageString();
    Component toMiniMessage();
}
