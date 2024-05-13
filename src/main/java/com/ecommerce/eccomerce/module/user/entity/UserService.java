package com.ecommerce.eccomerce.module.user.entity;

import com.ecommerce.eccomerce.module.user.dto.CreateUserDto;
import com.ecommerce.eccomerce.module.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository){this.userRepository = userRepository;}

    public void saveUser(CreateUserDto createUserDto){
        logger.info("Start method - Request - "+createUserDto);
        var user = this.createUser(createUserDto);
        logger.warning("esse e o user: "+user);
        this.userRepository.save(user);
        logger.info("End method ");
    }


    private UsersEntity createUser(CreateUserDto createUserDto){

        return UsersEntity.builder()
                .email(createUserDto.email())
                .password(createUserDto.password())
                .name(createUserDto.name())
                .build();
    }
}
