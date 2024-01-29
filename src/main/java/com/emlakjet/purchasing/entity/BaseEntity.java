package com.emlakjet.purchasing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@MappedSuperclass
@Data
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(length = 100)
    private UUID id;

    @JsonIgnore
    private boolean deleted;

    @Schema(readOnly = true)
    @CreationTimestamp
    private Timestamp created;

    @Schema(readOnly = true)
    @UpdateTimestamp
    private Timestamp updated;

}
