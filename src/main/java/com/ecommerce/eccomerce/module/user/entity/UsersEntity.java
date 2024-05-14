package com.ecommerce.eccomerce.module.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Entity(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "email",unique = true)
    @Email(message = "please, put an email exist")
    private String email;

    @Column(name = "password")
    @Length(min = 8,max = 100)
    private String password;

    @Column(name = "name")
    private String name;
}
