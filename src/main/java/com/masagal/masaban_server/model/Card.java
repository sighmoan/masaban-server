package com.masagal.masaban_server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record Card (@JsonProperty("contents") String text, UUID id) {
}
