package ma.ismail.spring_sec_jwt.repository;

import ma.ismail.spring_sec_jwt.entites.Equipment;
import ma.ismail.spring_sec_jwt.entites.Probleme;
import ma.ismail.spring_sec_jwt.entites.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProblemeRepository extends JpaRepository<Probleme,Long> {

    //List<Equipment> findByEquipementId(Long equipementId);

//    Optional<Probleme> findBySolutionId(Long solutionId);
    Probleme findProblemeById(Long id);
    Probleme findProblemeByLibelle(String libelle);

}
