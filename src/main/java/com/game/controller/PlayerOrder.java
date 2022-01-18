package com.game.controller;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PlayerOrder {
    ID("id"), // default
    NAME("name"),
    EXPERIENCE("experience"),
    BIRTHDAY("birthday"),
    LEVEL("level");

    private final String fieldName;

    PlayerOrder(String fieldName) {
        this.fieldName = fieldName;
    }

    //@JsonValue
    public String getFieldName() {
        return fieldName;
    }
}