package com.ecommerce.eccomerce.module.user;

import com.ecommerce.eccomerce.module.user.dto.CreateUserDto;
import com.ecommerce.eccomerce.module.user.dto.LoginDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());
    private final UserService userService;
    public UserController(UserService userService){this.userService = userService;}
    
    @PostMapping("/register")
    public void register(@RequestBody CreateUserDto createUserDto) throws Exception {
        try{
            logger.warning("Start method register - Request - "+createUserDto);
            this.userService.save(createUserDto);
        }catch (Exception e){
            logger.severe("Error method register - Response - "+e);
            throw new Exception();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) throws Exception {
        try{
            logger.warning("Start method login - Request - "+loginDto);
            return ResponseEntity.ok().body(this.userService.login(loginDto));
        }catch (Exception e){
            logger.warning("Error method login - Response - "+e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
