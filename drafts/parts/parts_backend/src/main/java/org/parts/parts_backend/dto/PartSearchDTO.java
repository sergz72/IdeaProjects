package org.parts.parts_backend.dto;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
public class PartSearchDTO {
    public List<Integer> categoryIds;

    public List<String> sizeIds;

    public List<String> unitIds;

    public Integer precisionId;

    public String name;

    public BigDecimal value;

    public String comment;
}
