package org.parts.parts_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sizes")
@Getter
@Setter
@NoArgsConstructor
public class PartSize {
    public PartSize(String id)
    {
        this.id = id;
    }

    @Id
    @Size(min = 1, max = 4)
    private String id;
}
