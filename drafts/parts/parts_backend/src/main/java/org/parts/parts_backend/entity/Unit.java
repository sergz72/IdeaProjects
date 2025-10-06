package org.parts.parts_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "units")
@Getter
@Setter
@NoArgsConstructor
public class Unit {
    public Unit(String id, BigDecimal multiplier)
    {
        this.multiplier = multiplier;
        this.id = id;
    }

    @Id
    @Size(min = 1, max = 4)
    private String id;

    private BigDecimal multiplier;
}
