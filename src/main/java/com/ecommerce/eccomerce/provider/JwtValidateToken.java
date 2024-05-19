package com.ecommerce.eccomerce.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecommerce.eccomerce.module.company.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtValidateToken {

    @Value("${security.token.secret}")
    private String secretKey;

    private static final Logger logger = LoggerFactory.getLogger(JwtValidateToken.class);

    public DecodedJWT validateToken(String token){
        try{
            logger.info("Start method validateToken - Request - "+token);
            token = token.replace("Bearer ","");
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            logger.info("End method validateToken - Response - "+JWT.require(algorithm).build().verify(token));
            return JWT.require(algorithm).build().verify(token);
        }catch(JWTVerificationException e){
            logger.error("Error method validateToken - Error - " +e);
            e.printStackTrace();
            return null;

        }

    };
}
