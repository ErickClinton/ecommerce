package com.ecommerce.eccomerce.module.company.dto;

public record CreateCompanyDto(
        String email,String password, String companyName
) {
}
