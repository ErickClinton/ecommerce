package com.ecommerce.eccomerce.security.filter;

import com.ecommerce.eccomerce.provider.JwtValidateToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class SecurityFilterUser extends OncePerRequestFilter {

    private final Logger logger = Logger.getLogger(SecurityFilterUser.class.getName());
    private JwtValidateToken jwtValidateToken;

    public SecurityFilterUser(JwtValidateToken jwtValidateToken){this.jwtValidateToken = jwtValidateToken;}


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.info("Start method doFilterInternal");

        String header = request.getHeader("Authorization");

        if(request.getRequestURI().startsWith("/user")){
            if(header !=null){
                var token = this.jwtValidateToken.validateToken(header);
                if(token == null){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                request.setAttribute("user_id",token.getSubject());
                var roles = token.getClaim("roles").asList(Object.class);
                var grants = roles.stream().map(role->new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase())).toList();

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                        (token.getSubject(),null,grants);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        logger.info("End method doFilterInternal");
        filterChain.doFilter(request,response);
    }
}
