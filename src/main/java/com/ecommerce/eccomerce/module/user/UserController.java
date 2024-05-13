package com.ecommerce.eccomerce.module.user;

import com.ecommerce.eccomerce.module.user.dto.CreateUserDto;
import com.ecommerce.eccomerce.module.user.entity.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());
    private final UserService userService;
    public UserController(UserService userService){this.userService = userService;}
    
    @PostMapping("/register")
    public  void register(@RequestBody CreateUserDto createUserDto){
        logger.warning("Entrei no register");
        this.userService.saveUser(createUserDto);

    }
}
