package ma.ismail.spring_sec_jwt.controllers;

import ma.ismail.spring_sec_jwt.dto.DTOInterventionn;
import ma.ismail.spring_sec_jwt.dto.DtoIntervention;
import ma.ismail.spring_sec_jwt.entites.Intervention;
import ma.ismail.spring_sec_jwt.services.InterventionService;
import org.apache.catalina.LifecycleState;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@RequestMapping(path = "/helpdesk")
public class HelpDeskController {
    private InterventionService interventionService;

    public HelpDeskController(InterventionService interventionService) {
        this.interventionService = interventionService;
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('HELP_DESK')")
    ResponseEntity<String> test(){

        return ResponseEntity.ok("hello ismail");
    }

    @PostMapping("/intervention")
    @PreAuthorize("hasAuthority('HELP_DESK') or hasAuthority('ADMIN')")
    Intervention addIntervention(@RequestBody DTOInterventionn dtoIntervention){
        return interventionService.saveInterventionnn(dtoIntervention);
    }
    /*@PostMapping("/intervention/fin")
    Intervention findIntervention(@RequestBody DtoIntervention dtoIntervention){
        return interventionService.finIntervention(dtoIntervention);
    }*/
    @PostMapping("/intervention/fin")
    @PreAuthorize("hasAuthority('HELP_DESK') or hasAuthority('ADMIN')")
    Intervention EndOfIntervention(@RequestBody DtoIntervention dtoIntervention){
        return interventionService.EndOfIntervention(dtoIntervention);
    }
    @PostMapping("/intervention/update")
    @PreAuthorize("hasAuthority('HELP_DESK') or hasAuthority('ADMIN')")
    Intervention updateIntervention(@RequestBody DTOInterventionn dtoIntervention){
        return interventionService.updateInterventionUserHelp(dtoIntervention);
    }
    @GetMapping(path = "/interventions")
    @PreAuthorize("hasAuthority('HELP_DESK') or hasAuthority('ADMIN')")
    List<Intervention> getAllInterventions(

    ){
        return interventionService.getAllInterventions();
    }
    @GetMapping(path = "/interventions/id/{id}")
    @PreAuthorize("hasAuthority('HELP_DESK') or hasAuthority('ADMIN')")
    List<Intervention> getAllInterventionsId(
            @PathVariable Long id
    ){
        return interventionService.getAllInterventionsHelp(id);
    }

}
