package ma.ismail.spring_sec_jwt.controllers;

import ma.ismail.spring_sec_jwt.dto.DtoIntervention;
import ma.ismail.spring_sec_jwt.entites.Intervention;
import ma.ismail.spring_sec_jwt.services.InterventionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@RequestMapping(path = "/technicien")
public class TechnicienController {
    private InterventionService interventionService;

    public TechnicienController(InterventionService interventionService) {
        this.interventionService = interventionService;
    }


    @GetMapping("/test")
    @PreAuthorize("hasAuthority('TECHNICIEN')")
    ResponseEntity<String> test(){
        return ResponseEntity.ok("hello ismail");
    }
    /*@GetMapping("/interventions")
    @PreAuthorize("hasAuthority('TECHNICIEN')")
    List<Intervention> getAllInterventions(@RequestParam String username){
        return interventionService.interventionByUser(username);
    }*/
    @GetMapping("/interventionns")
    @PreAuthorize("hasAuthority('TECHNICIEN')")
    List<Intervention> getAllInterventions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return interventionService.interventionByUser(username);
    }

    @GetMapping("/interventions/username")
    List<Intervention> getAllInterventioons(@RequestParam String username) {
        return interventionService.interventionByUser(username);
    }
    @GetMapping("/interventions/aeroport")
    List<Intervention> getAllInterventioonsByAeroport(@RequestParam String aeroport) {
        return interventionService.interventionsByAeroport(aeroport);
    }
    @GetMapping("/interventions")
    List<Intervention> getAllInterventiioons() {
        return interventionService.getAllInterventions();
    }

    @PostMapping("/intervention")
    @PreAuthorize("hasAuthority('TECHNICIEN')")
    Intervention addIntervention(@RequestBody DtoIntervention dtoIntervention){
        return interventionService.saveIntervention(dtoIntervention);
    }
//    @PostMapping("/intervention/fin")
//    @PreAuthorize("hasAuthority('TECHNICIEN')")
//    Intervention findIntervention(@RequestBody DtoIntervention dtoIntervention){
//        return interventionService.finIntervention(dtoIntervention);
//    }
    @PostMapping("/intervention/fin")
    @PreAuthorize("hasAuthority('TECHNICIEN') ")
    Intervention EndOfIntervention(@RequestBody DtoIntervention dtoIntervention){
        return interventionService.EndOfIntervention(dtoIntervention);
    }
    @PostMapping("/intervention/update")
    @PreAuthorize("hasAuthority('TECHNICIEN')")
    Intervention updateIntervention(@RequestBody DtoIntervention dtoIntervention){
        return interventionService.updateInterventionUser(dtoIntervention);
    }
}
