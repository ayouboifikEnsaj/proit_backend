package ma.ismail.spring_sec_jwt.repository;

import ma.ismail.spring_sec_jwt.entites.Projet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetRepository extends JpaRepository<Projet,Long> {
    Projet findProjetById(Long id);
    Projet findProjetByProjetName(String projetName);

}
