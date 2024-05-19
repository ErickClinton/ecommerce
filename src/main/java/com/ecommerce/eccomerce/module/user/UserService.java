package com.ecommerce.eccomerce.module.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ecommerce.eccomerce.module.company.CompanyService;
import com.ecommerce.eccomerce.module.company.dto.AuthResponseDto;
import com.ecommerce.eccomerce.module.user.dto.CreateUserDto;
import com.ecommerce.eccomerce.module.user.dto.LoginDto;
import com.ecommerce.eccomerce.module.user.entity.UsersEntity;
import com.ecommerce.eccomerce.module.user.repository.UserRepository;
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
public class UserService {

    @Value("${security.token.secret}")
    private String secretKey;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(CreateUserDto createUserDto) throws AuthenticationException{
        logger.info("Start method save- Request - "+createUserDto);

       this.userRepository.findByEmail(createUserDto.email()).ifPresent((user)->{
           throw new DuplicateKeyException("User with email " + createUserDto.email() + " already exists.");
       });
        var passwordEncrypted = passwordEncoder.encode(createUserDto.password());
        var user = this.createUser(createUserDto,passwordEncrypted);
        this.userRepository.save(user);

        logger.info("End method save");
    }

    public AuthResponseDto login(LoginDto loginDto) throws AuthenticationException {
        logger.info("Start method - Request - "+loginDto);

        var userEntity = this.userRepository.findByEmail(loginDto.email())
                .orElseThrow(AuthenticationException::new);

        var passwordMatches = this.passwordEncoder.matches(loginDto.password(),userEntity.getPassword());
        if(!passwordMatches) throw new AuthenticationException("User not found");

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiresIn = Instant.now().plus(Duration.ofHours(2));

        var token = JWT.create().withIssuer("companyToken")
                .withExpiresAt(expiresIn)
                .withSubject(userEntity.getId().toString())
                .withClaim("roles", List.of("USER"))
                .sign(algorithm);
        var response = AuthResponseDto.builder().acessToken(token).expiresIn(expiresIn.toEpochMilli()).build();

        logger.info("End method login - Response - "+response);

        return response;
    }


    private UsersEntity createUser(CreateUserDto createUserDto, String passwordEncrypted){
        return UsersEntity.builder()
                .email(createUserDto.email())
                .password(passwordEncrypted)
                .name(createUserDto.name())
                .build();
    }
}
