package com.laliga.laliga_crud_app.common;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorEntry> fieldErrors
) {
    public static record FieldErrorEntry(String field, String message){}
}
