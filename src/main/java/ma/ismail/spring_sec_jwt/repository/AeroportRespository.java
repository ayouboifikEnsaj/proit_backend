package ma.ismail.spring_sec_jwt.repository;

import ma.ismail.spring_sec_jwt.entites.Aeroport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AeroportRespository extends JpaRepository<Aeroport,Long> {
    Aeroport findAeroportById(Long id);
    Aeroport findAeroportByAeroportName(String aeroportName);
    Aeroport findAeroportByUsers_Username(String username);
    Aeroport findByAeroportName(String aeroportName);

    Aeroport findByAeroportNameIgnoreCase(String tanger);
}
