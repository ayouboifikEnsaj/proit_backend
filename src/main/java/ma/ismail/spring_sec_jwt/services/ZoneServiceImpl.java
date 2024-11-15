package ma.ismail.spring_sec_jwt.services;

import jakarta.transaction.Transactional;
import ma.ismail.spring_sec_jwt.Exceptions.ResourceNotFoundException;
import ma.ismail.spring_sec_jwt.dto.DtoZone;
import ma.ismail.spring_sec_jwt.dto.DtoZoneResponce;
import ma.ismail.spring_sec_jwt.entites.*;

import ma.ismail.spring_sec_jwt.repository.AeroportRespository;
import ma.ismail.spring_sec_jwt.repository.ZoneRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class ZoneServiceImpl implements ZoneService {

    private ZoneRepository zoneRepository;

    private AeroportRespository aeroportRespository;

    public ZoneServiceImpl(ZoneRepository zoneRepository, AeroportRespository aeroportRespository) {
        this.zoneRepository = zoneRepository;
        this.aeroportRespository = aeroportRespository;
    }

    @Override
    public Zone addZone(String zoneName) {
        Zone zone = new Zone(null,zoneName,new ArrayList<>());
        return zoneRepository.save(zone);

    }


    @Override
    public List<Zone> getAllZones() {
        List<Zone> zones = zoneRepository.findAll(); // Fetch all zones

        // Mapping using streams (for efficiency and readability)
        return zones;
    }

    @Override
    public List<Zone> getZonesByAeroportId(Long aeroportId) {
//        return zoneRepository.findByAeroportId(aeroportId);
        return null;
    }

    @Override
    public Zone getZoneById(Long zoneId) {
//        return zoneRepository.findByZoneId(ZoneId);
        Zone zone=zoneRepository.findZoneById(zoneId);
        if(zone!=null){
            return zone;
        }
        else {
            System.out.println("Zone with id "+zoneId+"Not found");
            return null;
        }
    }

    @Override
    public Zone updateZone(DtoZone dtoZone) {
      Zone zone = zoneRepository.findZoneById(dtoZone.getId());
      zone.setZoneName(dtoZone.getZoneName());
      return zoneRepository.save(zone);
    }

    @Override
    public boolean deleteZone(Long zoneId) {
        if (zoneRepository.existsById(zoneId)) {
            zoneRepository.deleteById(zoneId);
            return true;
        } else {
            System.out.println("Comptoire with ID '" + zoneId + "' not found.");
            return false;
        }
    }

    @Override
    public void importZonesData(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(8);

        List<Aeroport> aeroports = aeroportRespository.findAll();

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
            String zoneName = row.getCell(1).getStringCellValue().trim();
            Zone zone = new Zone();
            zone.setZoneName(zoneName);
            zoneRepository.save(zone);

        }

        workbook.close();
    }

}
