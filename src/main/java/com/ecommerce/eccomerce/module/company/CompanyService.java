package com.ecommerce.eccomerce.module.company;

import com.ecommerce.eccomerce.module.company.dto.CreateCompanyDto;
import com.ecommerce.eccomerce.module.company.dto.LoginCompanyDto;
import com.ecommerce.eccomerce.module.company.entity.CompanyEntity;
import com.ecommerce.eccomerce.module.company.repository.CompanyRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.logging.Logger;

@Service
public class CompanyService {

    private static final Logger logger = Logger.getLogger(CompanyService.class.getName());
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public CompanyService(final CompanyRepository companyRepository, final PasswordEncoder passwordEncoder){
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(CreateCompanyDto createUserDto) throws AuthenticationException{
        logger.info("Start method save- Request - "+createUserDto);

       this.companyRepository.findByEmail(createUserDto.email()).ifPresent((user)->{
           throw new DuplicateKeyException("Company with email " + createUserDto.email() + " already exists.");
       });
        var passwordEncrypted = passwordEncoder.encode(createUserDto.password());
        var user = this.createUser(createUserDto,passwordEncrypted);
        this.companyRepository.save(user);

        logger.info("End method save");
    }

    public void login(LoginCompanyDto loginDto) throws AuthenticationException {
        logger.info("Start method login - Request - "+loginDto);

        var userEntity = this.companyRepository.findByEmail(loginDto.email())
                .orElseThrow(AuthenticationException::new);

        var passwordMatches = this.passwordEncoder.matches(loginDto.password(),userEntity.getPassword());
        if(!passwordMatches) throw new AuthenticationException("Company not found");

        logger.info("End method login");
    }


    private CompanyEntity createUser(CreateCompanyDto createUserDto, String passwordEncrypted){
        return CompanyEntity.builder()
                .email(createUserDto.email())
                .password(passwordEncrypted)
                .companyName(createUserDto.companyName())
                .build();
    }
}
