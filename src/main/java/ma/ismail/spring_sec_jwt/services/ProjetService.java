package ma.ismail.spring_sec_jwt.services;

import ma.ismail.spring_sec_jwt.dto.DtoAeroport;
import ma.ismail.spring_sec_jwt.dto.EquipmentDTO;
import ma.ismail.spring_sec_jwt.entites.Aeroport;
import ma.ismail.spring_sec_jwt.entites.Projet;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProjetService {
    public Projet addOrUpdateProjet(Projet projet);

    boolean deleteProjet(Long id);
    Projet addEquipmentToProjet(String projetName, Long eqipmentId);
    Projet removeEquipmentFromProjet(String projetName, Long eqipmentId);
    List<Projet> getALl();

    List<EquipmentDTO> getAllEquipmentsByProjet(Long id);
}
