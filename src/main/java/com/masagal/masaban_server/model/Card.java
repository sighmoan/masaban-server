package com.masagal.masaban_server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Card (@JsonProperty("contents") String text, String id) {
}
