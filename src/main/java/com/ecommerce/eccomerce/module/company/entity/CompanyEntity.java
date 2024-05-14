package com.ecommerce.eccomerce.module.company.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Entity(name = "company")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "email",unique = true)
    @Email(message = "please, put an email exist")
    private String email;

    @Column(name = "password")
    @Length(min = 8,max = 100)
    private String password;

    @Column(name = "company_name")
    private String companyName;
}
