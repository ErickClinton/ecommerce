package com.ecommerce.eccomerce.module.company;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ecommerce.eccomerce.module.company.dto.AuthResponseDto;
import com.ecommerce.eccomerce.module.company.dto.CreateDto;
import com.ecommerce.eccomerce.module.company.dto.LoginDto;
import com.ecommerce.eccomerce.module.company.entity.CompanyEntity;
import com.ecommerce.eccomerce.module.company.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class CompanyService {

    @Value("${security.token.secret}")
    private String secretKey;

    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;


    public CompanyService(final CompanyRepository companyRepository, final PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(CreateDto createUserDto) {
        logger.info("Start method save- Request - "+createUserDto);

        this.companyRepository.findByEmail(createUserDto.email()).ifPresent((user)-> {
            throw new DuplicateKeyException("Company with email " + createUserDto.email() + " already exists.");
        });
        var passwordEncrypted = passwordEncoder.encode(createUserDto.password());
        var user = this.createUser(createUserDto,passwordEncrypted);
        this.companyRepository.save(user);

        logger.info("End method save");
    }

    public AuthResponseDto login(LoginDto loginDto) throws AuthenticationException {
        logger.info("Start method login - Request - "+loginDto);

        var companyEntity = this.companyRepository.findByEmail(loginDto.email())
                .orElseThrow(AuthenticationException::new);

        var passwordMatches = this.passwordEncoder.matches(loginDto.password(),companyEntity.getPassword());
        if(!passwordMatches) throw new AuthenticationException("Company not found");


        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiresIn = Instant.now().plus(Duration.ofHours(2));

        var token = JWT.create().withIssuer("companyToken")
                .withExpiresAt(expiresIn)
                .withSubject(companyEntity.getId().toString())
                .withClaim("roles", List.of("COMPANY"))
                .sign(algorithm);
        var response = AuthResponseDto.builder().acessToken(token).expiresIn(expiresIn.toEpochMilli()).build();

        logger.info("End method login - Response - "+response);

        return response;

    }


    private CompanyEntity createUser(CreateDto createUserDto, String passwordEncrypted){
        return CompanyEntity.builder()
                .email(createUserDto.email())
                .password(passwordEncrypted)
                .companyName(createUserDto.companyName())
                .build();
    }
}