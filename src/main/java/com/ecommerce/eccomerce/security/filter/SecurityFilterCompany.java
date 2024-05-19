package com.ecommerce.eccomerce.security.filter;

import com.ecommerce.eccomerce.module.company.CompanyService;
import com.ecommerce.eccomerce.provider.JwtValidateToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilterCompany extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilterCompany.class);
    private final JwtValidateToken jwtValidateToken;

    public SecurityFilterCompany(JwtValidateToken jwtValidateToken){this.jwtValidateToken = jwtValidateToken;}


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.info("start method doFilterInternal");
        String header = request.getHeader("Authorization");
        if(request.getRequestURI().startsWith("/company")){
            if(header !=null){
                var token = this.jwtValidateToken.validateToken(header);
                if(token == null){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                request.setAttribute("company_id",token.getSubject());
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
