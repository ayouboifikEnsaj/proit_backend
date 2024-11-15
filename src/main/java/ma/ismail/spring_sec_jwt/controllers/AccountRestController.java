package ma.ismail.spring_sec_jwt.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ma.ismail.spring_sec_jwt.dto.Dto;
import ma.ismail.spring_sec_jwt.dto.UserCredentialsDto;
import ma.ismail.spring_sec_jwt.entites.Aeroport;
import ma.ismail.spring_sec_jwt.entites.AppRole;
import ma.ismail.spring_sec_jwt.entites.AppUser;
import ma.ismail.spring_sec_jwt.repository.AeroportRespository;
import ma.ismail.spring_sec_jwt.repository.AppRoleRepository;
import ma.ismail.spring_sec_jwt.services.AccountService;
import ma.ismail.spring_sec_jwt.services.AeroportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@Slf4j
@CrossOrigin("*")
//BY Default prePostEnabled is true;
public class AccountRestController {
    AccountService accountService;
    AppRoleRepository appRoleRepository;
    AeroportService aeroportService;
    public AccountRestController(AccountService accountService,AppRoleRepository appRoleRepository, AeroportService aeroportService) {
        this.accountService = accountService;
        this.appRoleRepository=appRoleRepository;
        this.aeroportService=aeroportService;
    }

    @GetMapping(path = "/users")
    @PostAuthorize("hasAuthority('ADMIN')")
    public List<AppUser> users(){
        return accountService.listUsers();
    }
    @GetMapping(path = "/role")
    @PostAuthorize("hasAuthority('HELP_DESK') or hasAuthority('ADMIN')")
    public List<AppRole> roles(){
        return appRoleRepository.findAll();
    }
    @GetMapping(path = "/users/aeroport/{username}")
    @PostAuthorize("hasAuthority('HELP_DESK') or hasAuthority('ADMIN') or hasAuthority('TECHNICIEN')")
    public Aeroport aeroport(@PathVariable String username){

        return aeroportService.getAeroport(username);
    }

    //Help_Desk
    @GetMapping(path = "/users/role/{role}")
    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('HELP_DESK')")
    public List<AppUser> getUsersByRole(@PathVariable String role) {
        return accountService.getUsersByRole(role);
    }
    @GetMapping(path = "/users/role/aeroport")
    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('HELP_DESK') or hasAuthority('TECHNICIEN')")
    public List<AppUser> getUsersByRoleAndAeroport(@RequestParam String role,@RequestParam String aeroport) {
        return accountService.findByRoles_RoleNameAndAeroport1_AeroportName(role,aeroport);
    }
    @PostMapping(path = "/verifyCredentials")
    public ResponseEntity<String> verifyCredentials(@RequestBody UserCredentialsDto credentialsDto) {
        boolean isValid = accountService.verifyCredentials(credentialsDto.getUsername(), credentialsDto.getPassword());
        if (isValid) {
            return ResponseEntity.ok("Valid credentials");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @PostMapping(path = "/users")
    public AppUser saveUser(@RequestBody AppUser appUser){
        return accountService.addNewUser(appUser);
    }
    @PostMapping(path = "/users/update")
    public AppUser updateUser(@RequestBody AppUser appUser){
        return accountService.updateUser(appUser);
    }
    @PostMapping(path = "/role")
//    @PostAuthorize("hasAuthority('ADMIN')")
    public AppRole saveRole(@RequestBody AppRole appRole){

        return accountService.addNewRole(appRole);
    }
//    @PostMapping(path = "/addRoleToUser")
//    void addRoleToUser(@RequestBody Dto dto){
//        accountService.addRoleToUsername(dto.getUsername(),dto.getRolename());
//    }
    @GetMapping(path = "/refreshtoken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String authToken=request.getHeader("Authorization");
        if(authToken!=null && authToken.startsWith("Bearer ")){
            try {
                String jwt=authToken.substring(7);
                Algorithm algorithm=Algorithm.HMAC256("mysecret1234");
                JWTVerifier jwtVerifier= JWT.require(algorithm).build();
                DecodedJWT decodedJWT=jwtVerifier.verify(jwt);
                String username=decodedJWT.getSubject();
                AppUser appUser=accountService.loadUserByUsername(username);
                String jwtAccessToken=JWT.create()
                        .withSubject(appUser.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+1*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",appUser.getRoles().stream().map(r->
                                r.getRoleName()).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String,String> idToken=new HashMap<>();
                idToken.put("access-token",jwtAccessToken);
                idToken.put("refresh-token",jwt);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(),idToken);

            }catch (Exception e){
                throw e;
            }


        }else {
            throw new RuntimeException("Refresh Token Required!!");
        }
    }
}

