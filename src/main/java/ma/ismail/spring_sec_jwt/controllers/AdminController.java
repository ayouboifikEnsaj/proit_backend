package ma.ismail.spring_sec_jwt.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import ma.ismail.spring_sec_jwt.dto.*;
import ma.ismail.spring_sec_jwt.entites.*;
import ma.ismail.spring_sec_jwt.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@RequestMapping(path = "/admin")
public class AdminController {
    private InterventionService interventionService;
    private EquipmentService equipmentService;
    private ZoneService zoneService;
    private AeroportService aeroportService;
    private AccountService accountService;
    private ProblemService problemService;
    private SolutionService solutionService;
    private ComptoireService comptoireService;
    private CompagnieService compagnieService;
    private ProjetService projetService;

    public AdminController(InterventionService interventionService, EquipmentService equipmentService,
                           ZoneService zoneService, AeroportService aeroportService, AccountService accountService,
                           ProblemService problemService, SolutionService solutionService, ComptoireService comptoireService,
                           CompagnieService compagnieService, ProjetService projetService) {
        this.interventionService = interventionService;
        this.equipmentService = equipmentService;
        this.zoneService = zoneService;
        this.aeroportService = aeroportService;
        this.accountService = accountService;
        this.problemService = problemService;
        this.solutionService = solutionService;
        this.comptoireService = comptoireService;
        this.compagnieService = compagnieService;
        this.projetService = projetService;
    }

    //<============GESTION DES INTERVENTIONS==================>
    @GetMapping(path = "/interventions")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICIEN') or hasAuthority('HELP_DESK')")
    List<Intervention> getAllInterventions() {
        return interventionService.getAllInterventions();
    }

    @PostMapping(path = "/intervention")
    @PreAuthorize("hasAuthority('ADMIN')")
    Intervention addIntervention(@RequestBody DtoIntervention intervention) {
        return interventionService.saveIntervention(intervention);
    }

    @PostMapping("/intervantion/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    Boolean deleteIntervention(@RequestParam Long id) {
        return interventionService.deleteIntervention(id);
    }

    /*@PostMapping(path = "/intervention/fin")
    @PreAuthorize("hasAuthority('ADMIN')")
    Intervention finIntervention(@RequestBody DtoIntervention intervention){
        return interventionService.finIntervention(intervention);
    }*/
    @PostMapping(path = "/intervention/fin")
    @PreAuthorize("hasAuthority('ADMIN')")
    Intervention finIntervention(@RequestBody DtoIntervention intervention) {
        return interventionService.EndOfIntervention(intervention);
    }

    @PostMapping(path = "/intervention/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    Intervention updateIntervention(@RequestBody DtoIntervention intervention) {
        return interventionService.updateIntervention(intervention);
    }

    @GetMapping(path = "/interventions/problems")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Long>> getInterventionsByProbleme() {
        Map<String, Long> interventionsByProbleme = interventionService.getInterventionsByProbleme();
        return new ResponseEntity<>(interventionsByProbleme, HttpStatus.OK);
    }

    @GetMapping("/interventions/by-problem-type")
    public ResponseEntity<Map<String, Object>> getInterventionsByProblemType() {
        Map<String, Object> response = new HashMap<>();
        List<String> problems = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        // Logique pour récupérer les données par problème et compter les interventions
        Map<String, Integer> problemCounts = interventionService.getInterventionsCountByProblemType();
        for (Map.Entry<String, Integer> entry : problemCounts.entrySet()) {
            problems.add(entry.getKey());
            counts.add(entry.getValue());
        }

        response.put("problems", problems);
        response.put("counts", counts);

        return ResponseEntity.ok(response);
    }

    //<==================Gestion des Equipements=================>
    @GetMapping(path = "/equipments")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICIEN') or hasAuthority('HELP_DESK')")
    List<EquipmentDTO> getAllEquipemnts() {
        return equipmentService.getAllEquipments();
    }

    @PostMapping(path = "/equipment")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICIEN')")
    Equipment addEquipment(@RequestBody DtoEquipment dtoEquipment) {
        return equipmentService.saveEquipment(dtoEquipment.getEquipmentName(), dtoEquipment.getIdComptoire());
    }

    @PostMapping(path = "/equipement")
    @PreAuthorize("hasAuthority('ADMIN')")
    EquipmentDTO saveEquipment(@RequestBody EquipmentDTO dtoEquipment) {
        return equipmentService.addOrUpdateEquipment(dtoEquipment);
    }

    @PostMapping(path = "/equipment/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    Boolean deleteEquipment(@RequestBody EquipmentDTO dtoEquipment) {
        if (dtoEquipment.getId() != null) {
            System.out.println("*******************IS DELETE***************");
            return equipmentService.deleteEquipment(dtoEquipment.getId());
        } else {
            System.out.println("*******************IS NOT DELETE***************");
            return false;
        }
    }

    @PostMapping(path = "/equipment/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Equipment> updateEquipment(@RequestBody DtoEquipment dtoEquipment) {
        Equipment equipment = equipmentService.updateEquipment(dtoEquipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipment);
    }

    @PostMapping(path = "/equipement/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EquipmentDTO> updateEquipement(@RequestBody EquipmentDTO dtoEquipment) {
        EquipmentDTO equipment = equipmentService.addOrUpdateEquipment(dtoEquipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipment);
    }
    @GetMapping(path = "/equipementsByprojet")
    public ResponseEntity<List<EquipmentDTO>> getAllEquipmentsByProjet(@RequestParam Long id){
        List<EquipmentDTO> equipmentDTOS=projetService.getAllEquipmentsByProjet(id);
        return ResponseEntity.ok(equipmentDTOS);
    }

    //<=================Gestion des Comptoires================>
    @GetMapping(path = "/comptoires")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICIEN') or hasAuthority('HELP_DESK')")
    public ResponseEntity<List<Comptoire>> getAllComptoire() {
        List<Comptoire> comptoires = comptoireService.getAllComptoires();
        return ResponseEntity.ok(comptoires);
    }

    @GetMapping(path = "/comptoiires")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICIEN') or hasAuthority('HELP_DESK')")
    public List<ComptoireDTO> getAllComptoires() {
        return comptoireService.getAllComptoiresWithZoneAndAeroport();
    }

    //prob
    @PostMapping(path = "/add-comptoire")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Comptoire> addComptoire(@Valid @RequestBody DtoComptoire dtoComptoire) {
        Comptoire savedComptoire = comptoireService.saveComptoire(dtoComptoire.getComptoireName(), dtoComptoire.getZoneId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComptoire);
    }

    @PostMapping(path = "/comptoire/delete")
     @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> deleteComptoire(@Valid @RequestBody DtoComptoire dtoComptoire) {
        if (dtoComptoire.getId() != null) {
            Boolean result = comptoireService.deleteComptoire(dtoComptoire.getId());
            if (result) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
            }
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping(path = "/comptoire/update")
      @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Comptoire> updateComptoire(@Valid @RequestBody DtoComptoire dtoComptoire) {
        Comptoire comptoire = comptoireService.updateComptoire(dtoComptoire);
        return ResponseEntity.status(HttpStatus.CREATED).body(comptoire);
    }

    //<=================Gestion des compagnies================>
    @GetMapping(path = "/compagnies")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICIEN') or hasAuthority('HELP_DESK')")
    public ResponseEntity<List<Compagnie>> getAllCompagnies() {
        List<Compagnie> compagnies = compagnieService.getAllCompagnies();
        return ResponseEntity.ok(compagnies);
    }

    @PostMapping(path = "/add-compagnie")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Compagnie> addCompagnie(@Valid @RequestBody DtoCompagnie dtoCompagnie) {
        Compagnie savedConpagnie = compagnieService.saveCompagnie(dtoCompagnie.getCompagnieName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedConpagnie);
    }

    @PostMapping(path = "/add-compagnieToAeroport")
     @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Aeroport> addCompagnieToAeroport(@Valid @RequestBody DtoCompagnie dtoCompagnie) {
        Aeroport addCompagnieToAeroport = compagnieService.addCompagnieToAeroport(dtoCompagnie.getCompagnieName(), dtoCompagnie.getAeroportName());
        if (addCompagnieToAeroport != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(addCompagnieToAeroport);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(addCompagnieToAeroport);
        }
    }

    @PostMapping(path = "/remove-compagnieToAeroport")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Aeroport> removeCompagnieToAeroport(@Valid @RequestBody DtoCompagnie dtoCompagnie) {
        Aeroport addCompagnieToAeroport = compagnieService.removeCompagnieToAeroport(dtoCompagnie.getCompagnieName(), dtoCompagnie.getAeroportName());
        if (addCompagnieToAeroport != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(addCompagnieToAeroport);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(addCompagnieToAeroport);
        }
    }

    @GetMapping(path = "/aeroportCompagnie")
    public List<AeroportDTO> getAllAeroports() {
        return aeroportService.getAllAeroportsWithCompagnies();
    }

    @PostMapping(path = "/compagnie/delete")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> deleteCompagnie(@Valid @RequestBody DtoCompagnie dtoCompagnie) {
        if (dtoCompagnie.getId() != null) {
            Boolean result = compagnieService.deleteCompagnie(dtoCompagnie.getId());
            if (result) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
            }
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping(path = "/compagnie/update")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Compagnie> updateCompagnie(@Valid @RequestBody DtoCompagnie dtoCompagnie) {
        Compagnie compagnie = compagnieService.updateCompagnie(dtoCompagnie);
        return ResponseEntity.status(HttpStatus.CREATED).body(compagnie);
    }

    //<=================Gestion des Zones================>
    @GetMapping(path = "/zones")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICIEN') or hasAuthority('HELP_DESK')")
    public ResponseEntity<List<Zone>> getAllZones() {
        List<Zone> zones = zoneService.getAllZones();
        return ResponseEntity.ok(zones);
    }

    @PostMapping(path = "/add-zone")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Zone> addZone(@Valid @RequestBody DtoZone dtoZone) {
        String aerportId = dtoZone.getAeroportName();
        String zoneName = dtoZone.getZoneName();
        Zone savedzone = zoneService.addZone(zoneName);
        if (savedzone != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedzone);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }

    }

    @PostMapping(path = "/zone/delete")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> deleteZone(@RequestBody DtoZone dtoZone) {
        if (dtoZone.getId() != null) {
            Boolean result = zoneService.deleteZone(dtoZone.getId());
            if (result) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
            }
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping(path = "/zone/update")
    //  @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Zone> updateZone(@Valid @RequestBody DtoZone dtoZone) {
        Zone zone = zoneService.updateZone(dtoZone);
        return ResponseEntity.status(HttpStatus.CREATED).body(zone);
    }

    //<=================Gestion des Aeroports================>
    @GetMapping(path = "/aeroports")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICIEN') or hasAuthority('HELP_DESK')")
    public ResponseEntity<List<Aeroport>> getAllAeroport() {
        List<Aeroport> aeroports = aeroportService.getAllAeroports();
        return ResponseEntity.ok(aeroports);
    }

    @PostMapping(path = "/add-aeroport")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Aeroport> addAeroport(@Valid @RequestBody DtoAeroport dtoAeroport) {
        Aeroport savedAeroport = aeroportService.saveAeroport(dtoAeroport.getAeroportName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAeroport);
    }

    @PostMapping(path = "/aeroport/delete")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> deleteAeroport(@Valid @RequestBody DtoAeroport dtoAeroport) {
        if (dtoAeroport.getId() != null) {
            Boolean result = aeroportService.deleteAeroport(dtoAeroport.getId());
            if (result) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
            }
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping(path = "/aeroport/update")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Aeroport> updateAeroport(@Valid @RequestBody DtoAeroport dtoAeroport) {
        Aeroport aeroport = aeroportService.updateAeroport(dtoAeroport);
        return ResponseEntity.status(HttpStatus.CREATED).body(aeroport);
    }

    //<=================Gestion des Problemes================>
    @GetMapping(path = "/problemes")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICIEN') or hasAuthority('HELP_DESK')")
    public ResponseEntity<List<Probleme>> getAllProblemes() {
        List<Probleme> problemes = problemService.getAllProblemes();
        return ResponseEntity.ok(problemes);
    }

    @PostMapping(path = "/add-problem")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Probleme> addProblem(@Valid @RequestBody DtoProbleme dtoProbleme) {
        Probleme savedProblem = problemService.addNewProbleme(dtoProbleme.getLibelle());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProblem);
    }

    @PostMapping(path = "/problem/delete")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> deleteProblem(@Valid @RequestBody DtoProbleme dtoProbleme) {
        if (dtoProbleme.getId() != null) {
            Boolean result = problemService.deleteProbleme(dtoProbleme.getId());
            if (result) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
            }
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping(path = "/problem/update")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Probleme> updateProblem(@Valid @RequestBody DtoProbleme dtoProbleme) {
        Probleme probleme = problemService.updateProbleme(dtoProbleme);
        return ResponseEntity.status(HttpStatus.CREATED).body(probleme);
    }

    //<=================Gestion des Solutiones================>
    @GetMapping(path = "/solutions")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICIEN') or hasAuthority('HELP_DESK')")
    public ResponseEntity<List<Solution>> getAllSolutiones() {
        List<Solution> solutions = solutionService.getAllSolutions();
        return ResponseEntity.ok(solutions);
    }

    @PostMapping(path = "/add-solution")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Solution> addSolution(@Valid @RequestBody DtoSolution dtoSolution) {
        Long problemeId = dtoSolution.getProblemId();
        String libelle = dtoSolution.getLibelle();
        Solution savedsolution = solutionService.addSolution(libelle, problemeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedsolution);


    }

    @PostMapping(path = "/solution/delete")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> deletesolution(@Valid @RequestBody DtoSolution dtoSolution) {
        if (dtoSolution.getId() != null) {
            Boolean result = solutionService.deleteSolution(dtoSolution.getId());
            if (result) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
            }
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping(path = "/solution/update")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Solution> updateSolution(@Valid @RequestBody DtoSolution dtoSolution) {
        Solution solution = solutionService.updateSolution(dtoSolution);
        return ResponseEntity.status(HttpStatus.CREATED).body(solution);
    }
    @GetMapping(path = "/TBF")
    public Map<String, Double> getTBF(@RequestParam("equipmentId") Long equipmentId, @RequestParam("month") int month) {
        return interventionService.calculateTBF(equipmentId, month);
    }

    @GetMapping("/percentages")
    public Map<String, Double> getInterventionPercentages(
            @RequestParam Long comptoireId,
            @RequestParam int month,
            @RequestParam int year) {
        return interventionService.getInterventionPercentages(comptoireId, month, year);
    }
    @GetMapping("/interventions/by-problem-and-month")
    public Map<String, Object> getInterventionsByProblemAndMonth() {
        return interventionService.getInterventionsByProblemAndMonth();
    }
    //<==================Projet Gestion======================>
    @GetMapping(path = "/projets")
    public List<Projet> getAllProjets(){
        return projetService.getALl();
    }
    @PostMapping(path = "/projet")
    public Projet saveProjet(@RequestBody Projet projet){
        return projetService.addOrUpdateProjet(projet);
    }
    @PostMapping(path = "/projet/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    Boolean deleteProjet(@RequestBody Projet projet) {
        if (projet.getId() != null) {
            System.out.println("*******************IS DELETE***************");
            return projetService.deleteProjet(projet.getId());
        } else {
            System.out.println("*******************IS NOT DELETE***************");
            return false;
        }
    }
    @PostMapping(path = "/addEquipmentToProjet")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Projet> addEquipmentToProjet(@Valid @RequestBody ProjetDTO projetDTO) {
        Projet adddEquipmentToProjet = projetService.addEquipmentToProjet(projetDTO.getProjetName(), projetDTO.getEqipmentId());
        if (adddEquipmentToProjet != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(adddEquipmentToProjet);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(adddEquipmentToProjet);
        }
    }

    @PostMapping(path = "/removeEquipmentFromProjet")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Projet> removeEquipmentFromProjet(@Valid @RequestBody ProjetDTO projetDTO) {
        System.out.println("L'Objet DTOPROjet : "+projetDTO);
        Projet reemoveCompagnieToAeroport = projetService.removeEquipmentFromProjet(projetDTO.getProjetName(), projetDTO.getEqipmentId());
        if (reemoveCompagnieToAeroport != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(reemoveCompagnieToAeroport);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(reemoveCompagnieToAeroport);
        }
    }


    // -------------- Import data from Excel to Database ---------------------

    @PostMapping("/compagnie/upload")
    public ResponseEntity<String> uploadCompagnieFile(@RequestParam("file") MultipartFile file) {
        try {
            compagnieService.importCompagnieData(file);
            return ResponseEntity.ok("File imported successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import file: " + e.getMessage());
        }
    }
    @PostMapping("/aeroport/upload")
    public ResponseEntity<String> uploadAeroportsFile(@RequestParam("file") MultipartFile file) {
        try {
            aeroportService.importAeroportData(file);
            return ResponseEntity.ok("Aeroports file imported successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import aeroports file: " + e.getMessage());
        }
    }

    @PostMapping("/upload-users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> uploadUsersFile(@RequestParam("file") MultipartFile file) {
        System.out.println("start");
        try {
            accountService.importAppUserData(file);
            return ResponseEntity.ok("Users file imported successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import users file: " + e.getMessage());
        }
    }
    @PostMapping("/upload-problems")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> uploadProblemsFile(@RequestParam("file") MultipartFile file) {
        System.out.println("start");
        try {
            problemService.importProblemData(file);
            return ResponseEntity.ok("Users file imported successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import users file: " + e.getMessage());
        }
    }
    @PostMapping("/upload-solutions")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> uploadSolutionsFile(@RequestParam("file") MultipartFile file) {
        System.out.println("start");
        try {
            solutionService.importSolutionData(file);
            return ResponseEntity.ok("Users file imported successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import users file: " + e.getMessage());
        }
    }
    @PostMapping("/upload-zones")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> uploadZonesFile(@RequestParam("file") MultipartFile file) {
        System.out.println("start");
        try {
            zoneService.importZonesData(file);
            return ResponseEntity.ok("Users file imported successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import users file: " + e.getMessage());
        }
    }
    @PostMapping("/upload-equipment")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> uploadEquipmentsFile(@RequestParam("file") MultipartFile file) {
        System.out.println("start");
        try {
            equipmentService.importEquipmentData(file);
            return ResponseEntity.ok("Users file imported successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import users file: " + e.getMessage());
        }
    }
    @PostMapping("/upload-comptoire")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> uploadComptoiresFile(@RequestParam("file") MultipartFile file) {
        System.out.println("start");
        try {
            comptoireService.importComptoireData(file);
            return ResponseEntity.ok("Users file imported successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import users file: " + e.getMessage());
        }
    }
    @PostMapping("/upload-intervention")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> uploadInterventionsFile(@RequestParam("file") MultipartFile file) {
        System.out.println("start");
        try {
            interventionService.importInterventionData(file);
            return ResponseEntity.ok("Users file imported successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import users file: " + e.getMessage());
        }
    }
    //TBF Calculate
    @GetMapping("/interventions/tbf")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Double> getTBF(
            @RequestParam Long projetId,
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam Long aeroportId
    ) {
        double tbf = interventionService.calculateTbfByProjetAndYear(projetId, month, year,aeroportId);
        System.out.println("projetId : "+projetId);
        System.out.println("month : "+month);
        System.out.println("year : "+year);
        System.out.println("aeroportId : "+aeroportId);
        return ResponseEntity.ok(tbf);
    }
    @GetMapping("/interventions/tbf/year")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<Integer, Double>> getTBFForYear(
            @RequestParam Long projetId,
            @RequestParam int year,
            @RequestParam Long aeroportId
    ) {
        Map<Integer, Double> tbfByMonth = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            double tbf = interventionService.calculateTbfByProjetAndYear(projetId, month, year, aeroportId);
            tbfByMonth.put(month, tbf);
        }
        return ResponseEntity.ok(tbfByMonth);
    }
    @GetMapping(path = "/interventions/aeroport")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICIEN') or hasAuthority('HELP_DESK')")
    List<Intervention> getAllInterventionsByAeroport(
            @RequestParam Long aeroportId
    ) {
        return interventionService.interventionsByAirport(aeroportId);
    }
    @GetMapping(path = "/interventions/aeroport/projet")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICIEN') or hasAuthority('HELP_DESK')")
    List<Intervention> getAllInterventionsByAeroportAndProjet(
            @RequestParam Long aeroportId
    ) {
        return interventionService.interventionsByAirportAndProjet(aeroportId);
    }
    @GetMapping(path = "/interventions/aeroport/projet/egate")
    @PreAuthorize("hasAuthority('ADMIN') ")
    List<Intervention> getAllInterventionsByAeroportAndProjetEGate(
            @RequestParam Long aeroportId
    ) {
        return interventionService.interventionsByAirportAndProjetEGte(aeroportId);
    }
}






