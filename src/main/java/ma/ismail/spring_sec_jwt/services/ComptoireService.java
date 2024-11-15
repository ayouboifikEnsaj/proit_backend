package ma.ismail.spring_sec_jwt.services;

import ma.ismail.spring_sec_jwt.dto.ComptoireDTO;
import ma.ismail.spring_sec_jwt.dto.DtoComptoire;
import ma.ismail.spring_sec_jwt.entites.Comptoire;
import ma.ismail.spring_sec_jwt.entites.Zone;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ComptoireService {
    Comptoire saveComptoire(String comptoireName, Long zoneId);
    Comptoire getCotmptoireById(Long id);
    List<ComptoireDTO> getAllComptoiresWithZoneAndAeroport();
    List<Comptoire> getAllComptoires();
    public Comptoire updateComptoire(DtoComptoire dtoComptoire);
    boolean deleteComptoire(Long id);
    public void importComptoireData(MultipartFile file) throws IOException;
}
