package org.parts.parts_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "parts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "size_id")
    @Size(min = 1, max = 4)
    private String sizeId;

    @Column(name = "unit_id")
    @Size(min = 1, max = 4)
    private String unitId;

    @Column(name = "precision_id")
    private Integer precisionId;

    @Column(name = "name", nullable = false)
    @Size(min = 1, max = 100)
    private String name;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "quantity", nullable = false)
    private Short quantity;

    @Column(name = "quantity_in_use", nullable = false)
    private Short quantityInUse = 0;

    @Column(name = "quantity_not_in_use", nullable = false, insertable=false, updatable=false)
    private Short quantityNotInUse;

    @Column(name = "comment", length = 1000)
    private String comment;
}