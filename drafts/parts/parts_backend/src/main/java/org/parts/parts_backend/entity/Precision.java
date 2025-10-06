package org.parts.parts_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "precisions")
@Getter
@Setter
@NoArgsConstructor
public class Precision {
    public Precision(BigDecimal value)
    {
        this.value = value;
        this.id = null;
    }

    public Precision(Integer id, BigDecimal value)
    {
        this.value = value;
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal value;
}
