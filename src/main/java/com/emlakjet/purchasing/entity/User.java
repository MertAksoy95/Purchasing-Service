package com.emlakjet.purchasing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@Entity
@Where(clause = "deleted = false")
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8941717238293850241L;

    @Column(unique = true, nullable = false)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String firstName;
    private String lastName;


    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

}
