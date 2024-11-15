package ma.ismail.spring_sec_jwt.services;

import ma.ismail.spring_sec_jwt.entites.*;
import ma.ismail.spring_sec_jwt.repository.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private AeroportRespository aeroportRespository;
    @Autowired
    private AppRoleRepository appRoleRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private CompagnieRepository compagnieRepository;
    @Autowired
    private ComptoireRepository comptoireRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private InterventionRepository interventionRepository;
    @Autowired
    private ProblemeRepository problemeRepository;
    @Autowired
    private SolutionRepository solutionRepository;
    @Autowired
    private ZoneRepository zoneRepository;
    private PasswordEncoder passwordEncoder;

    public void SaveDataFromExcel() throws IOException {
        try(InputStream inputStream = new ClassPathResource("sitaData.xls").getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream)){

            Sheet aeroportSheet = workbook.getSheet("Aeroport");
            Sheet appUserSheet = workbook.getSheet("AppUser");
            Sheet compagnieSheet = workbook.getSheet("Compagnie");
            Sheet comptoireSheet = workbook.getSheet("Comptoire");
            Sheet equipementSheet = workbook.getSheet("Equipement");
            Sheet interventionSheet = workbook.getSheet("Intervention");
            Sheet problemeSheet = workbook.getSheet("Probleme");
            Sheet solutionSheet = workbook.getSheet("Solution");
            Sheet zoneSheet = workbook.getSheet("Zone");

            saveAeropports(aeroportSheet);
            saveAppUsers(appUserSheet);
            saveCompagnies(compagnieSheet);
            saveComptoire(comptoireSheet);
            List<Equipment> equipementList = saveEquipement(equipementSheet);
            saveIntervention(interventionSheet);
            saveProbleme(problemeSheet);
            saveSolution(solutionSheet);
            List<Zone> zoneList= saveZone(zoneSheet);

            List<Comptoire> comptoires = comptoireRepository.findAll();
            for(Comptoire comptoire : comptoires){
                comptoireRepository.save(comptoire);
            }
            List<Aeroport> aeroports = aeroportRespository.findAll();
            for(Aeroport aeroport : aeroports){
                aeroportRespository.save(aeroport);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void saveComptoire(Sheet comptoireSheet) {
        List<Comptoire> comptoires = new ArrayList<>();
        for(int i=1 ; i <= comptoireSheet.getLastRowNum(); i++){
            Row row =comptoireSheet.getRow(i);
            Comptoire comptoire = new Comptoire();
            comptoire.setComptoireName(row.getCell(1).getStringCellValue());
            Zone zone = zoneRepository.findZoneByZoneName(row.getCell(2).getStringCellValue());
            if(zone!= null)
                comptoire.setZone(zone);
            else
                System.out.println("zone not found");
            comptoires.add(comptoire);
        }
        comptoireRepository.saveAll(comptoires);
    }

    private List<Zone> saveZone(Sheet zoneSheet){
        List<Zone> zones = new ArrayList<>();
        for(int i=1 ; i  <= zoneSheet.getLastRowNum(); i++){
            Row row=zoneSheet.getRow(i);
            Zone zone = new Zone();
            zone.setZoneName(row.getCell(1).getStringCellValue());
            zones.add(zone);
        }
        zoneRepository.saveAll(zones);
        return zones;
    }

    private void saveSolution(Sheet solutionSheet) {
        List<Solution> solutions = new ArrayList<>();
        for(int i=1 ; i  <= solutionSheet.getLastRowNum(); i++){
            Row row=solutionSheet.getRow(i);
            Solution solution = new Solution();
            solution.setLibelle(row.getCell(1).getStringCellValue());
            solutions.add(solution);
        }
        solutionRepository.saveAll(solutions);
    }

    private void saveProbleme(Sheet problemeSheet) {
        List<Probleme> problemes = new ArrayList<>();
        for(int i=1 ; i  <= problemeSheet.getLastRowNum(); i++){
            Row row=problemeSheet.getRow(i);
            Probleme probleme = new Probleme();
            probleme.setLibelle(row.getCell(1).getStringCellValue());
            problemes.add(probleme);
        }
        problemeRepository.saveAll(problemes);
    }

    private void saveIntervention(Sheet interventionSheet) {

    }

    private List<Equipment> saveEquipement(Sheet equipementSheet) {
        List<Equipment> equipments = new ArrayList<>();
        for(int i=1 ; i  <= equipementSheet.getLastRowNum(); i++){
            Row row=equipementSheet.getRow(i);
            Equipment equipment = new Equipment();
            equipment.setEquipementName(row.getCell(1).getStringCellValue());
            equipments.add(equipment);
        }
        equipmentRepository.saveAll(equipments);
        return equipments;
    }

    private void saveCompagnies(Sheet compagnieSheet) {
        List<Compagnie> compagnies = new ArrayList<>();
        for(int i=1 ; i  <= compagnieSheet.getLastRowNum(); i++){
            Row row=compagnieSheet.getRow(i);
            Compagnie compagnie = new Compagnie();
            compagnie.setCompagnieName(row.getCell(1).getStringCellValue());
            compagnies.add(compagnie);
        }
        compagnieRepository.saveAll(compagnies);
    }

    private void saveAppUsers(Sheet appUserSheet) {
        List<AppUser> appUsers = new ArrayList<>();
        for(int i=1 ; i <= appUserSheet.getLastRowNum() ; i++){
            Row row = appUserSheet.getRow(i);
            AppUser appUser = new AppUser();
            appUser.setFirstname(row.getCell(1).getStringCellValue());
            appUser.setLastname(row.getCell(2).getStringCellValue());
            appUser.setUsername(row.getCell(3).getStringCellValue());
            String password = row.getCell(4).getStringCellValue();
            appUser.setPassword(passwordEncoder.encode(password));
            Aeroport aeroport = aeroportRespository.findAeroportByAeroportName(row.getCell(5).getStringCellValue());
            if(aeroport != null)
                appUser.setAeroport(aeroport);
            else
                System.out.println("aeroport not found");

            String roleNames =row.getCell(6).getStringCellValue();
            List <AppRole> roles =new ArrayList<>();
            for(String roleName : roleNames.split(",")){
                AppRole appRole = appRoleRepository.findByRoleName(roleName);
                if(appRole!= null)
                    roles.add(appRole);
                else
                    System.out.println("approle not found");

            }

            appUser.setRoles(roles);
            appUsers.add(appUser);

        }
        appUserRepository.saveAll(appUsers);
    }

    private void saveAeropports(Sheet aeroportSheet) {
        List<Aeroport> aeroports = new ArrayList<>();
        for(int i=1; i <= aeroportSheet.getLastRowNum();i++){
            Row row = aeroportSheet.getRow(i);
            Aeroport aeroport = new Aeroport();
            aeroport.setAeroportName(row.getCell(1).getStringCellValue());
            aeroports.add(aeroport);
        }
        aeroportRespository.saveAll(aeroports);
    }

}
