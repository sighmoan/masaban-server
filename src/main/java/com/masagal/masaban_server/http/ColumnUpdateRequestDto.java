package com.masagal.masaban_server.http;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record ColumnUpdateRequestDto (@NotNull @NotEmpty String label, @NotNull Integer index) {
}
