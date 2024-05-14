package com.ecommerce.eccomerce.module.company;

import com.ecommerce.eccomerce.module.company.dto.CreateCompanyDto;
import com.ecommerce.eccomerce.module.company.dto.LoginCompanyDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private static final Logger logger = Logger.getLogger(CompanyController.class.getName());
    private final CompanyService companyService;
    public CompanyController(CompanyService companyService){this.companyService = companyService;}
    
    @PostMapping("/register")
    public void register(@RequestBody CreateCompanyDto createCompanyDto) throws Exception {
        try{
            logger.warning("Start method register - Request - "+createCompanyDto);
            this.companyService.save(createCompanyDto);
        }catch (Exception e){
            logger.warning("Error method register - Response - "+e);
            throw new Exception();
        }
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginCompanyDto loginCompanyDto) throws Exception {
        try{
            logger.warning("Start method login - Request - "+loginCompanyDto);
            this.companyService.login(loginCompanyDto);
        }catch (Exception e){
            logger.warning("Error method login - Response - "+e);
            throw new Exception(e);
        }
    }
}
