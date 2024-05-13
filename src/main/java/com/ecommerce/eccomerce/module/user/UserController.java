package com.ecommerce.eccomerce.module.user;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @PostMapping("/register")
    public static void register(){
        logger.warning("Entrei no registe");
    }
}
