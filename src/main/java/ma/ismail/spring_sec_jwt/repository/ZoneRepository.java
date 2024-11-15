package ma.ismail.spring_sec_jwt.repository;

import ma.ismail.spring_sec_jwt.entites.Aeroport;
import ma.ismail.spring_sec_jwt.entites.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ZoneRepository extends JpaRepository<Zone,Long> {
    //List<Zone> findByAeroportId(Long aeroportId);
    Zone findZoneById(Long id);

    Zone findZoneByZoneName(String stringCellValue);


//    Optional<Zone> findByZoneId(Long zoneId);

}
