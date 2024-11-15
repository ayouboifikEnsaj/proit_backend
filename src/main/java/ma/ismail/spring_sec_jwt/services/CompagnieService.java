package ma.ismail.spring_sec_jwt.services;

import jakarta.transaction.Transactional;
import ma.ismail.spring_sec_jwt.dto.DtoCompagnie;
import ma.ismail.spring_sec_jwt.entites.Aeroport;
import ma.ismail.spring_sec_jwt.entites.Compagnie;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface CompagnieService {
    Compagnie saveCompagnie(String companieName);
    Aeroport addCompagnieToAeroport(String companieName, String AeroportName);
    Aeroport removeCompagnieToAeroport(String companieName, String AeroportName);
    Compagnie getCompagnieById(Long id);
    List<Compagnie> getAllCompagnies();
    public Compagnie updateCompagnie(DtoCompagnie dtoCompagnie);
    boolean deleteCompagnie(Long id);

    String getCompagnieNameById(Long key);
    public void importCompagnieData(MultipartFile file) throws IOException;
}
