package com.ecommerce.eccomerce.module.user;

import com.ecommerce.eccomerce.module.user.dto.CreateUserDto;
import com.ecommerce.eccomerce.module.user.dto.LoginDto;
import com.ecommerce.eccomerce.module.user.entity.UsersEntity;
import com.ecommerce.eccomerce.module.user.repository.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());
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

    public void login(LoginDto loginDto) throws AuthenticationException {
        logger.info("Start method - Request - "+loginDto);

        var userEntity = this.userRepository.findByEmail(loginDto.email())
                .orElseThrow(AuthenticationException::new);

        var passwordMatches = this.passwordEncoder.matches(loginDto.password(),userEntity.getPassword());
        if(!passwordMatches) throw new AuthenticationException("User not found");

        logger.info("End method login");
    }


    private UsersEntity createUser(CreateUserDto createUserDto, String passwordEncrypted){
        return UsersEntity.builder()
                .email(createUserDto.email())
                .password(passwordEncrypted)
                .name(createUserDto.name())
                .build();
    }
}
