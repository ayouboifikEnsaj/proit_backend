package ma.ismail.spring_sec_jwt;

import ma.ismail.spring_sec_jwt.entites.*;
import ma.ismail.spring_sec_jwt.repository.*;
import ma.ismail.spring_sec_jwt.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class SpringSecJwtApplication {

    @Autowired
    AccountService accountService;
    @Autowired
    AeroportRespository aeroportRespository;
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    AppRoleRepository appRoleRepository;
    @Autowired
    CompagnieRepository compagnieRepository;
    @Autowired
    ComptoireRepository comptoireRepository;
    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    InterventionRepository interventionRepository;
    @Autowired
    ProblemeRepository problemeRepository;
    @Autowired
    SolutionRepository solutionRepository;
    @Autowired
    ZoneRepository zoneRepository;
    @Autowired
    AeroportService aeroportService;
    @Autowired
    CompagnieService compagnieService;
    @Autowired
    ProblemService problemService;
    @Autowired
    SolutionService solutionService;
    @Autowired
    ZoneService zoneService;
    @Autowired
    InterventionService interventionService;
    @Autowired
    ExcelService excelService;

    public static void main(String[] args) {
        SpringApplication.run(SpringSecJwtApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner init() {
        return args -> {
            List<String> roleNames = Arrays.asList("ADMIN", "TECHNICIEN", "HELP_DESK");
            List<AppRole> existingRoles = appRoleRepository.findAll();
            for (String roleName : roleNames) {
                boolean roleExists = existingRoles.stream()
                        .anyMatch(role -> role.getRoleName().equals(roleName));
                if (!roleExists) {
                    appRoleRepository.save(new AppRole(null, roleName));
                }
            }

            String username = "simo";
            Optional<AppUser> existingUser = Optional.ofNullable(appUserRepository.findByUsername(username));

            if (existingUser.isEmpty()) {
                accountService.addNewUser(new AppUser(null, "Mohamed", "sorhrani", username, "1234", new ArrayList<>(), new ArrayList<>(), null));
                List<AppRole> roles = new ArrayList<>();
                roles.add(appRoleRepository.findByRoleName("ADMIN"));
                accountService.addRoleToUsername("simo", roles);
            }
        };
    }
}
