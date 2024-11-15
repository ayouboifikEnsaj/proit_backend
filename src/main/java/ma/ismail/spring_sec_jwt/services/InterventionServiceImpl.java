package ma.ismail.spring_sec_jwt.services;

import jakarta.transaction.Transactional;
import ma.ismail.spring_sec_jwt.dto.DTOInterventionn;
import ma.ismail.spring_sec_jwt.dto.DtoIntervention;
import ma.ismail.spring_sec_jwt.entites.*;
import ma.ismail.spring_sec_jwt.repository.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class InterventionServiceImpl implements InterventionService {

    private InterventionRepository interventionRepository;
    private AppUserRepository appUserRepository;
    private ProblemeRepository problemeRepository;
    private SolutionRepository solutionRepository;
    private CompagnieRepository compagnieRepository;
    private ComptoireRepository comptoireRepository;
    private EquipmentRepository equipmentRepository;
    private AeroportRespository aeroportRespository;
    private CompagnieService compagnieService;
    private ProjetRepository projetRepository;

    public InterventionServiceImpl(InterventionRepository interventionRepository, AppUserRepository appUserRepository, ProblemeRepository problemeRepository, SolutionRepository solutionRepository, CompagnieRepository compagnieRepository, ComptoireRepository comptoireRepository, EquipmentRepository equipmentRepository, AeroportRespository aeroportRespository, ProjetRepository projetRepository) {
        this.interventionRepository = interventionRepository;
        this.appUserRepository = appUserRepository;
        this.problemeRepository = problemeRepository;
        this.solutionRepository = solutionRepository;
        this.compagnieRepository = compagnieRepository;
        this.comptoireRepository = comptoireRepository;
        this.equipmentRepository = equipmentRepository;
        this.aeroportRespository = aeroportRespository;
        this.projetRepository = projetRepository;
    }

    @Override
    /*public Intervention saveIntervention(DtoIntervention dtoIntervention) {

        Compagnie compagnie=compagnieRepository.findCompagnieById(dtoIntervention.getCompagnie());
        Comptoire comptoire=comptoireRepository.findComptoireById(dtoIntervention.getComptoire());
        Equipment equipment=equipmentRepository.findEquipmentById(dtoIntervention.getEquipment());
        AppUser appUser=appUserRepository.findByUsername(dtoIntervention.getAppUser());
        Aeroport aeroport=aeroportRespository.findAeroportById(dtoIntervention.getAeroport());
        Intervention intervention=new Intervention(null,Status.ENCOURS,new Date(),new Date(),null,compagnie,appUser,comptoire,equipment,null,null,aeroport);
        return interventionRepository.save(intervention);
    }*/
    public Intervention saveIntervention(DtoIntervention dtoIntervention) {
        Compagnie compagnie = compagnieRepository.findCompagnieById(dtoIntervention.getCompagnie());
        Comptoire comptoire = comptoireRepository.findComptoireById(dtoIntervention.getComptoire());
        Equipment equipment = equipmentRepository.findEquipmentById(dtoIntervention.getEquipment());
        AppUser appUser = appUserRepository.findByUsername(dtoIntervention.getAppUser());
        Aeroport aeroport = aeroportRespository.findAeroportById(dtoIntervention.getAeroport());
        Probleme probleme = problemeRepository.findProblemeById(dtoIntervention.getProbleme());
        Solution solution = solutionRepository.findSolutionById(dtoIntervention.getSolution());
        Projet projet=projetRepository.findProjetById(dtoIntervention.getProjet());
        Intervention intervention = new Intervention(
                null,
                Status.ENCOURS,
                new Date(),
                new Date(),
                null,
                compagnie,
                appUser,
                comptoire,
                equipment,
                solution,
                probleme,
                aeroport,
                projet
        );
        return interventionRepository.save(intervention);
    }

    @Override
    public Intervention saveInterventionnn(DTOInterventionn dtoIntervention) {
        Compagnie compagnie = compagnieRepository.findCompagnieById(dtoIntervention.getCompagnie());
        Comptoire comptoire = comptoireRepository.findComptoireById(dtoIntervention.getComptoire());
        Equipment equipment = equipmentRepository.findEquipmentById(dtoIntervention.getEquipment());
        AppUser appUser = appUserRepository.findAppUserById(dtoIntervention.getAppUser());
        Aeroport aeroport = aeroportRespository.findAeroportById(dtoIntervention.getAeroport());
        Probleme probleme = problemeRepository.findProblemeById(dtoIntervention.getProbleme());
        Solution solution = solutionRepository.findSolutionById(dtoIntervention.getSolution());
        Projet projet=projetRepository.findProjetById(dtoIntervention.getProjet());
        Intervention intervention = new Intervention(
                null,
                Status.ENCOURS,
                new Date(),
                new Date(),
                null,
                compagnie,
                appUser,
                comptoire,
                equipment,
                solution,
                probleme,
                aeroport,
                projet
        );
        return interventionRepository.save(intervention);
    }

    @Override
    public Map<String, Long> getInterventionsByProbleme() {
        List<Intervention> interventions = interventionRepository.findAll();
        Map<String, Long> problemeCountMap = new HashMap<>();

        for (Intervention intervention : interventions) {
            if (intervention.getProbleme() != null) {
                String problemeLibelle = intervention.getProbleme().getLibelle();
                problemeCountMap.put(problemeLibelle, problemeCountMap.getOrDefault(problemeLibelle, 0L) + 1);


            }
        }

        return problemeCountMap;
    }



    @Override
    public Map<String, Integer> getInterventionsCountByProblemType() {
        List<Object[]> results = interventionRepository.countInterventionsByProblemType();
        Map<String, Integer> problemCounts = new HashMap<>();

        for (Object[] result : results) {
            String problemType = (String) result[0];
            Long count = (Long) result[1];
            problemCounts.put(problemType, count.intValue());
        }

        return problemCounts;
    }


    @Override
    public Intervention getInterventionById(Long id) {
        if (interventionRepository.existsById(id)) {
            return interventionRepository.findInterventionById(id);
        } else {
            return null;
        }
    }

    @Override
    public List<Intervention> getAllInterventions() {
        return interventionRepository.findAll();
    }

    @Override
    public List<Intervention> getAllInterventionsHelp(Long id) {
        return interventionRepository.findInterventionsByAeroport_Id(id);
    }

    //Should Be changed
    @Override
    public Intervention updateIntervention(DtoIntervention dtoIntervention) {

        if (interventionRepository.existsById(dtoIntervention.getId())) {
            Intervention intervention = interventionRepository.findInterventionById(dtoIntervention.getId());
            Probleme probleme = problemeRepository.findProblemeById(dtoIntervention.getProbleme());
            Solution solution = solutionRepository.findSolutionById(dtoIntervention.getSolution());
            Compagnie compagnie = compagnieRepository.findCompagnieById(dtoIntervention.getCompagnie());
            Comptoire comptoire = comptoireRepository.findComptoireById(dtoIntervention.getComptoire());
            Equipment equipment = equipmentRepository.findEquipmentById(dtoIntervention.getEquipment());
            AppUser appUser = appUserRepository.findByUsername(dtoIntervention.getAppUser());
            intervention.setDate(dtoIntervention.getDate());
            intervention.setHeureDebut(dtoIntervention.getHeureDebut());
            intervention.setHeureFin(dtoIntervention.getHeureFin());
            intervention.setCompagnie(compagnie);
            intervention.setComptoire(comptoire);
            intervention.setSolution(solution);
            intervention.setProbleme(probleme);
            intervention.setEquipment(equipment);
            intervention.setAppUser(appUser);
            return interventionRepository.save(intervention);
        } else {
            System.out.println("Intervention not found");
            return null;
        }
    }


    @Override
    public boolean deleteIntervention(Long id) {
        if (interventionRepository.existsById(id)) {
            interventionRepository.deleteById(id);
            return true;
        } else {
            System.out.println("Intervention Not Found");
            return false;
        }
    }

    @Override
    public Intervention finIntervention(DtoIntervention dtoIntervention) {
        Intervention intervention = interventionRepository.findInterventionById(dtoIntervention.getId());
        Probleme probleme = problemeRepository.findProblemeById(dtoIntervention.getProbleme());
        Solution solution = solutionRepository.findSolutionById(dtoIntervention.getSolution());
        intervention.setHeureFin(new Date());
        intervention.setSolution(solution);
        intervention.setProbleme(probleme);
        intervention.setStatus(Status.FERMER);
        return interventionRepository.save(intervention);
    }

    @Override
    public List<Intervention> interventionByUser(String username) {
        AppUser user = appUserRepository.findByUsername(username);
        return interventionRepository.findInterventionsByAppUser(user);
    }

    @Override
    public List<Intervention> interventionsByAeroport(String aeroportName) {
        AppUser user = appUserRepository.findByUsername(aeroportName);
        Aeroport aeroport = aeroportRespository.findAeroportByAeroportName(user.getAeroport().getAeroportName());
        if (aeroport != null) {
            return interventionRepository.findInterventionsByAeroport_Id(aeroport.getId());
        }
        return null;
    }

    @Override
    public Intervention updateInterventionUser(DtoIntervention dtoIntervention) {
        if (interventionRepository.existsById(dtoIntervention.getId())) {
            Intervention intervention = interventionRepository.findInterventionById(dtoIntervention.getId());
            Probleme probleme = problemeRepository.findProblemeById(dtoIntervention.getProbleme());
            Solution solution = solutionRepository.findSolutionById(dtoIntervention.getSolution());
            Compagnie compagnie = compagnieRepository.findCompagnieById(dtoIntervention.getCompagnie());
            Comptoire comptoire = comptoireRepository.findComptoireById(dtoIntervention.getComptoire());
            Equipment equipment = equipmentRepository.findEquipmentById(dtoIntervention.getEquipment());
            AppUser appUser = appUserRepository.findByUsername(dtoIntervention.getAppUser());
            Aeroport aeroport = aeroportRespository.findAeroportById(dtoIntervention.getAeroport());
            Projet projet=projetRepository.findProjetById(dtoIntervention.getProjet());
            intervention.setCompagnie(compagnie);
            intervention.setComptoire(comptoire);
            intervention.setSolution(solution);
            intervention.setProbleme(probleme);
            intervention.setEquipment(equipment);
            intervention.setAppUser(appUser);
            intervention.setAeroport(aeroport);
            intervention.setProjet(projet);
            return interventionRepository.save(intervention);
        } else {
            System.out.println("Intervention not found");
            return null;
        }
    }

    @Override
    public Intervention updateInterventionUserHelp(DTOInterventionn dtoIntervention) {
        if (interventionRepository.existsById(dtoIntervention.getId())) {
            Intervention intervention = interventionRepository.findInterventionById(dtoIntervention.getId());
            Probleme probleme = problemeRepository.findProblemeById(dtoIntervention.getProbleme());
            Solution solution = solutionRepository.findSolutionById(dtoIntervention.getSolution());
            Compagnie compagnie = compagnieRepository.findCompagnieById(dtoIntervention.getCompagnie());
            Comptoire comptoire = comptoireRepository.findComptoireById(dtoIntervention.getComptoire());
            Equipment equipment = equipmentRepository.findEquipmentById(dtoIntervention.getEquipment());
            AppUser appUser = appUserRepository.findAppUserById(dtoIntervention.getAppUser());
            Aeroport aeroport = aeroportRespository.findAeroportById(dtoIntervention.getAeroport());
            Projet projet=projetRepository.findProjetById(dtoIntervention.getProjet());
            intervention.setCompagnie(compagnie);
            intervention.setComptoire(comptoire);
            intervention.setSolution(solution);
            intervention.setProbleme(probleme);
            intervention.setEquipment(equipment);
            intervention.setAppUser(appUser);
            intervention.setAeroport(aeroport);
            intervention.setProjet(projet);
            return interventionRepository.save(intervention);
        } else {
            System.out.println("Intervention not found");
            return null;
        }
    }

    @Override
    public Intervention EndOfIntervention(DtoIntervention dtoIntervention) {
        Intervention intervention = interventionRepository.findInterventionById(dtoIntervention.getId());
        if (intervention == null) {
            System.out.println("error");
        }

        intervention.setHeureFin(new Date()); // Set heureFin to current time
        intervention.setStatus(Status.FERMER);
        Equipment equipment = equipmentRepository.findEquipmentById(dtoIntervention.getEquipment());
        Probleme probleme = problemeRepository.findProblemeById(dtoIntervention.getProbleme());
        Solution solution = solutionRepository.findSolutionById(dtoIntervention.getSolution());
        intervention.setEquipment(equipment);
        intervention.setSolution(solution);
        intervention.setProbleme(probleme);

        return interventionRepository.save(intervention);
    }

    @Override

    public Map<String, Double> calculateTBF(Long equipmentId, int month) {
        int year = Calendar.getInstance().get(Calendar.YEAR);

        // Calculate the start and end dates of the selected month
        Calendar startCalendar = new GregorianCalendar(year, month - 1, 1, 0, 0, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        Date startDate = startCalendar.getTime();

        Calendar endCalendar = new GregorianCalendar(year, month - 1, 1, 23, 59, 59);
        endCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endCalendar.set(Calendar.MILLISECOND, 999);
        Date endDate = endCalendar.getTime();

        // Fetch interventions for the selected equipment within the selected month
        List<Intervention> interventions = interventionRepository.findByEquipmentIdAndDateBetween(equipmentId, startDate, endDate);

        // Calculate total minutes in the month
        int daysInMonth = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        long totalMinutesInMonth = (long) daysInMonth * 24 * 60; // Total minutes in the selected month

        // Calculate total intervention duration for selected equipment
        double totalInterventionDuration = interventions.stream()
                .mapToDouble(intervention -> {
                    long duration = intervention.getHeureFin().getTime() - intervention.getHeureDebut().getTime();
                    return duration / (1000 * 60); // Convert milliseconds to minutes
                })
                .sum();

        // Calculate TBF for each company
        Map<String, Double> tbfMap = interventions.stream()
                .collect(Collectors.groupingBy(
                        intervention -> intervention.getCompagnie().getCompagnieName(),
                        Collectors.summingDouble(intervention -> {
                            long duration = intervention.getHeureFin().getTime() - intervention.getHeureDebut().getTime();
                            return duration / (1000 * 60);
                        })
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            double totalCompanyInterventions = entry.getValue();
                            return ((totalMinutesInMonth - totalCompanyInterventions) / totalMinutesInMonth) * 100;
                        }
                ));

        return tbfMap;
    }

    @Override
    public Map<String, Double> getInterventionPercentages(Long comptoireId, int month, int year) {
        List<Intervention> interventions = interventionRepository.findByComptoireAndMonthAndYear(comptoireId, month, year);
        Map<Equipment, Long> equipmentCount = new HashMap<>();
        long totalInterventions = interventions.size();

        for (Intervention intervention : interventions) {
            Equipment equipment = intervention.getEquipment();
            equipmentCount.put(equipment, equipmentCount.getOrDefault(equipment, 0L) + 1);
        }

        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<Equipment, Long> entry : equipmentCount.entrySet()) {
            double percentage = (entry.getValue() / (double) totalInterventions) * 100;
            result.put(entry.getKey().getEquipementName(), percentage);
        }

        return result;
    }
    // les interventions par probleme pour chaque mois
    public Map<String, Object> getInterventionsByProblemAndMonth() {
        System.out.println("Avant Mappage Bien");
        List<Object[]> results = interventionRepository.countInterventionsByProblemAndMonth();

        Map<String, Integer> problemIndexMap = new HashMap<>();
        List<String> problems = new ArrayList<>();
        Map<String, List<Integer>> monthData = new LinkedHashMap<>();

        for (Object[] row : results) {
            String problem = (String) row[0];
            int month = (int) row[1];
            Long count = (Long) row[2];

            // Populate problems list and map
            if (!problemIndexMap.containsKey(problem)) {
                problemIndexMap.put(problem, problems.size());
                problems.add(problem);
            }

            String monthName = new DateFormatSymbols().getMonths()[month - 1];

            monthData.putIfAbsent(monthName, new ArrayList<>(Collections.nCopies(problems.size(), 0)));
            monthData.get(monthName).set(problemIndexMap.get(problem), count.intValue());
        }
        System.out.println("Avant Mappage");

        // Format JSON response
        Map<String, Object> response = new HashMap<>();
        response.put("problems", problems);

        List<Map<String, Object>> monthsList = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : monthData.entrySet()) {
            Map<String, Object> monthEntry = new HashMap<>();
            monthEntry.put("month", entry.getKey());
            monthEntry.put("counts", entry.getValue());
            monthsList.add(monthEntry);
        }
        response.put("months", monthsList);
        System.out.println("Responce :"+response);
        return response;
    }

    @Override
    public void importInterventionData(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(5);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header row
            }
            try {
                row.getCell(1).getStringCellValue().trim();
            }catch (Exception e){
                workbook.close();
                return;
            }
            Intervention intervention=new Intervention();
            if(row.getCell(1).getStringCellValue().trim().equals("FERMER")){
                intervention.setStatus(Status.FERMER);
            }else{
                intervention.setStatus(Status.ENCOURS);
            }
            Compagnie compagnie=compagnieRepository.findCompagnieByCompagnieName(row.getCell(5).getStringCellValue().trim());
            System.out.println("Apres compagnie");
            AppUser user=appUserRepository.findByUsername(row.getCell(6).getStringCellValue().trim());
            System.out.println("Apres user");
            Comptoire comptoire=comptoireRepository.findComptoireByComptoireName(row.getCell(7).getStringCellValue().trim());
            System.out.println("Apres comptoire");

            Solution solution=solutionRepository.findSolutionByLibelle(row.getCell(9).getStringCellValue().trim());
            System.out.println("Apres solution");
            Probleme probleme=problemeRepository.findProblemeByLibelle(row.getCell(10).getStringCellValue().trim());
            Projet projet=projetRepository.findProjetByProjetName(row.getCell(11).getStringCellValue().trim());
            System.out.println(projet.getProjetName());
            for (Equipment e:projet.getEquipments()
                 ) {
                if(row.getCell(8).getStringCellValue().trim().equals(e.getEquipementName())){
                    intervention.setEquipment(e);
                    System.out.println("Apres equipment");
                }
            }
            Date date=row.getCell(4).getDateCellValue();
            System.out.println("Apres prb");
            Aeroport aeroport=user.getAeroport();
            System.out.println("Apres aeroport");
            Date heureDebut=row.getCell(2).getDateCellValue();
            Date heureFin=row.getCell(3).getDateCellValue();
            intervention.setCompagnie(compagnie);
            intervention.setComptoire(comptoire);
            intervention.setAppUser(user);
            intervention.setSolution(solution);
            intervention.setProbleme(probleme);
            intervention.setAeroport(aeroport);
            intervention.setDate(date);
            intervention.setHeureDebut(heureDebut);
            intervention.setHeureFin(heureFin);
            intervention.setProjet(projet);
            interventionRepository.save(intervention);
        }

        workbook.close();
    }

    @Override
    public double calculateTbfByProjetAndYear(Long projetId, int month, int year, Long aeroportId) {
        List<Intervention> interventions=interventionRepository.
                findInterventionsByProjet_IdAndAeroport_Id(projetId,aeroportId);
        double tta=0;
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        int minutInMonth=daysInMonth*24*60;
        for (Intervention in:interventions
             ) {
            if((month-1)==in.getDate().getMonth() && year==(in.getDate().getYear()+1900)){
                int i=1;
                System.out.println("in : "+in.getProjet().getProjetName()+
                        " : "+
                        in.getAeroport().getAeroportName()+
                        "YEAR : "+
                        in.getDate().getYear()+
                        "MONTH : "+
                        in.getDate().getMonth()
                );
                tta+=in.getHeureFin().getTime()-in.getHeureDebut().getTime();
                System.out.println("TTA : " +i +" est : "+tta/60000);
                i=+1;
            }

        }

        double tbf= ((minutInMonth - tta/60000) * 100)/minutInMonth;
        tbf = Math.round(tbf * 1000.0) / 1000.0;
        return tbf;
    }

    @Override
    public List<Intervention> interventionsByAirport(Long aeroportId) {
        return interventionRepository.findInterventionsByAeroport_Id(aeroportId);
    }

    @Override
    public List<Intervention> interventionsByAirportAndProjet(Long aeroportId) {
        List<Intervention> interventions=interventionsByAirport(aeroportId);
        List<Intervention> interventionsList=new ArrayList<>();
        for (Intervention in:interventions){
            if(in.getProjet().getProjetName().equals("BRS") || in.getProjet().getProjetName().equals("CUTE") ){
                interventionsList.add(in);
            }
        }
        return interventionsList;
    }

    @Override
    public List<Intervention> interventionsByAirportAndProjetEGte(Long aeroportId) {
        List<Intervention> interventions=interventionsByAirport(aeroportId);
        List<Intervention> interventionsList=new ArrayList<>();
        for (Intervention in:interventions){
            if(in.getProjet().getProjetName().equals("E-GATE")){
                interventionsList.add(in);
            }
        }
        return interventionsList;
    }


}



