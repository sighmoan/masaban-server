package com.masagal.masaban_server.http;

import com.masagal.masaban_server.model.Card;

import java.util.UUID;

public record CardUpdateRequestDto (String contents){
    Card toCard() { return new Card(contents); }
}
