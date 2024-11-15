package ma.ismail.spring_sec_jwt.services;

import ma.ismail.spring_sec_jwt.dto.DtoZone;
import ma.ismail.spring_sec_jwt.dto.DtoZoneResponce;
import ma.ismail.spring_sec_jwt.entites.Solution;
import ma.ismail.spring_sec_jwt.entites.Zone;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ZoneService {
    Zone addZone(String zoneName);
    List<Zone> getAllZones();
    List<Zone> getZonesByAeroportId(Long aeroportId);
    Zone getZoneById(Long ZoneId);
    public Zone updateZone(DtoZone dtoZone);
    boolean deleteZone(Long zoneId);
    public void importZonesData(MultipartFile file) throws IOException;
}
