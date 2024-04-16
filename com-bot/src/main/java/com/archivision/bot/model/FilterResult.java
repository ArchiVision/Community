package com.archivision.bot.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilterResult {
    private String message;
    private boolean processNext;
}
