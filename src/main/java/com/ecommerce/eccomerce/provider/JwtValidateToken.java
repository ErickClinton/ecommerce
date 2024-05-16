package com.ecommerce.eccomerce.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class JwtValidateToken {

    @Value("${security.token.secret}")
    private String secretKey;

    private final Logger logger = Logger.getLogger(JwtValidateToken.class.getName());

    public DecodedJWT validateToken(String token){
        try{
            logger.info("Start method validateToken - Request - "+token);
            token = token.replace("Bearer ","");
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            logger.info("End method validateToken - Response - "+JWT.require(algorithm).build().verify(token));
            return JWT.require(algorithm).build().verify(token);
        }catch(JWTVerificationException e){
            logger.severe("Error method validateToken - Error - " +e);
            e.printStackTrace();
            return null;

        }

    };
}
