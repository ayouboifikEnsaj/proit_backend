package ma.ismail.spring_sec_jwt.services;

import jakarta.transaction.Transactional;
import ma.ismail.spring_sec_jwt.dto.ComptoireDTO;
import ma.ismail.spring_sec_jwt.dto.DtoComptoire;
import ma.ismail.spring_sec_jwt.entites.Compagnie;
import ma.ismail.spring_sec_jwt.entites.Comptoire;
import ma.ismail.spring_sec_jwt.entites.Zone;
import ma.ismail.spring_sec_jwt.repository.ComptoireRepository;
import ma.ismail.spring_sec_jwt.repository.ZoneRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class ComptoireServiceImpl implements ComptoireService {
    @Autowired
    ComptoireRepository comptoireRepository;
    @Autowired
    ZoneRepository zoneRepository;
    @Override
    public Comptoire saveComptoire(String comptoireName, Long zoneId) {

        if (comptoireRepository.findComptoireByComptoireName(comptoireName) != null) {
            return null;
        }

        Zone zone = zoneRepository.findZoneById(zoneId);
        if (zone == null) {
            System.out.println("Not Found");
            return null;

        }

        Comptoire comptoire = new Comptoire(null, comptoireName, zone, new ArrayList<>());
        return comptoireRepository.save(comptoire);
    }

    @Override
    public Comptoire getCotmptoireById(Long id) {
        if(comptoireRepository.existsById(id)){
            return comptoireRepository.findComptoireById(id);
        }
        else {
            System.out.println("Comptoire with ID '" + id + "' not found.");
            return null;
        }
    }

    @Override
    public List<Comptoire> getAllComptoires() {
        return comptoireRepository.findAll();
    }
    //Should be changed

    @Override
    public Comptoire updateComptoire(DtoComptoire dtoComptoire){
        Comptoire comptoire = comptoireRepository.findComptoireById(dtoComptoire.getId());
        comptoire.setComptoireName(dtoComptoire.getComptoireName());
        Zone zone = zoneRepository.findZoneById(dtoComptoire.getZoneId());
        comptoire.setZone(zone);
        return comptoireRepository.save(comptoire);
    }

    @Override
    public boolean deleteComptoire(Long id) {
        if(comptoireRepository.existsById(id)){
            comptoireRepository.deleteById(id);
            return true;
        }
        else {
            System.out.println("Comptoire with ID '" + id + "' not found.");
            return false;
        }

    }

    @Override
    public void importComptoireData(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(3);

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
            Comptoire comptoire=new Comptoire();
            // Ignoring ID column, assuming it is auto-generated
            comptoire.setComptoireName(row.getCell(1).getStringCellValue().trim());
            Zone zone=zoneRepository.findZoneByZoneName(row.getCell(2).getStringCellValue().trim());
            comptoire.setZone(zone);
            comptoireRepository.save(comptoire);
        }
        workbook.close();
    }

    @Override
    public List<ComptoireDTO> getAllComptoiresWithZoneAndAeroport() {
        List<Comptoire> comptoires = comptoireRepository.findAll();
        return comptoires.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ComptoireDTO convertToDTO(Comptoire comptoire) {
        ComptoireDTO dto = new ComptoireDTO();
        dto.setId(comptoire.getId());
        dto.setComptoireName(comptoire.getComptoireName());
        dto.setZoneName(comptoire.getZone().getZoneName());
        dto.setZoneId(comptoire.getZone().getId());
        return dto;
    }
}
