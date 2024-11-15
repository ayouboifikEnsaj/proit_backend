package ma.ismail.spring_sec_jwt.services;

import ma.ismail.spring_sec_jwt.dto.AeroportDTO;
import ma.ismail.spring_sec_jwt.dto.DtoAeroport;
import ma.ismail.spring_sec_jwt.entites.Aeroport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AeroportService {
    Aeroport saveAeroport(String aeroport);
    Aeroport getAeroportByName(String name);
    List<Aeroport> getAllAeroports();
    public Aeroport updateAeroport(DtoAeroport dtoAeroport);
    boolean deleteAeroport(Long id);
    List<AeroportDTO> getAllAeroportsWithCompagnies();
    Aeroport getAeroport(String username);
    public void importAeroportData(MultipartFile file) throws IOException;

}
