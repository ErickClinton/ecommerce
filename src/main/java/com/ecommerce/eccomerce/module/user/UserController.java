package com.ecommerce.eccomerce.module.user;

import com.ecommerce.eccomerce.module.user.dto.CreateUserDto;
import com.ecommerce.eccomerce.module.user.dto.LoginDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    public UserController(UserService userService){this.userService = userService;}
    
    @PostMapping("/register")
    public void register(@RequestBody CreateUserDto createUserDto) {
            logger.info("Start method register - Request - "+createUserDto);
            this.userService.save(createUserDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) throws Exception {
        try{
            logger.info("Start method login - Request - "+ loginDto);
            return ResponseEntity.ok().body(this.userService.login(loginDto));
        }catch (Exception e){
            logger.error("Error method login - Response - "+e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


}
