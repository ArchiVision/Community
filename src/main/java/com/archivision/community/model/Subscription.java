package com.archivision.community.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Subscription {
    private String name;
    private int price;
    private String description;
}
