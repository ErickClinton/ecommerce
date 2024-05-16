package com.ecommerce.eccomerce.module.company;

import com.ecommerce.eccomerce.module.company.dto.CreateDto;
import com.ecommerce.eccomerce.module.company.dto.LoginDto;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private static final Logger logger = Logger.getLogger(CompanyController.class.getName());
    private final CompanyService companyService;
    public CompanyController(CompanyService companyService){this.companyService = companyService;}
    
    @PostMapping("/register")
    public void register(@RequestBody CreateDto createDto) throws Exception {
        try{
            logger.info("Start method register - Request - "+ createDto);
            this.companyService.save(createDto);
        }catch (Exception e){
            logger.warning("Error method register - Response - "+e);
            throw new Exception();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) throws Exception {
        try{
            logger.info("Start method login - Request - "+ loginDto);
            return ResponseEntity.ok().body(this.companyService.login(loginDto));
        }catch (Exception e){
            logger.severe("Error method login - Response - "+e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/teste")
    @PreAuthorize("hasRole('COMPANY')")
    public String teste(){
        return "funcionou";
    }
}
