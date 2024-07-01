package com.masagal.masaban_server.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ColumnRepository extends CrudRepository<Column, UUID> {
    Column findByCardsContaining(List<Card> cards);
}
