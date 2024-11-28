package com.rustam.unitech.model;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.rustam.unitech.exception.custom.InvalidUUIDFormatException;
import com.rustam.unitech.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    private UUID id;

    private String name;

    private String username;

    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    Set<Role> authorities;

    @PrePersist
    protected void onCreate() {
        //log.info("OnCreate - @PrePersist works in BaseUser");
        if (this.id == null) {
            try {
                NameBasedGenerator generator = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
                String uniqueName = "userID-" + UUID.randomUUID() +
                        (this.password != null ? this.password : "") +
                        (this.name != null ? this.name : "") +
                        (this.username != null ? this.username : "") +
                        System.currentTimeMillis();
                UUID uuid = generator.generate(uniqueName);
                System.out.println("Generated UUIDv5 for base user id: " + uuid.toString());
                this.id = uuid;
            } catch (Exception e) {
                System.err.println("Error generating UUID: " + e.getMessage());
            }
        }
    }


    public String getId() {
        if (id != null) {
            return id.toString();
        }
        return null;
//        else {
//            throw new RuntimeException("Base user id is null");
//        }
    }

    public void setId(String id) {
        try {
            this.id = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDFormatException("Invalid UUID format for ID: " + id, e);
        }
    }
}
