package ma.ismail.spring_sec_jwt.repository;

import ma.ismail.spring_sec_jwt.entites.AppUser;
import ma.ismail.spring_sec_jwt.entites.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface InterventionRepository extends JpaRepository<Intervention,Long> {
    Intervention findInterventionById(Long id);
    List<Intervention> findInterventionsByAppUser(AppUser appUser);
    List<Intervention> findInterventionsByAeroport_Id(Long aLong);
    @Query("SELECT p.libelle AS problemType, COUNT(i.id) AS count " +
            "FROM Intervention i JOIN i.probleme p " +
            "GROUP BY p.libelle")
    List<Object[]> countInterventionsByProblemType();


    List<Intervention> findByEquipmentIdAndDateBetween(Long equipmentId, Date startDate, Date endDate);

    @Query("SELECT i FROM Intervention i WHERE i.comptoire.id = :comptoireId AND MONTH(i.date) = :month AND YEAR(i.date) = :year")
    List<Intervention> findByComptoireAndMonthAndYear(@Param("comptoireId") Long comptoireId, @Param("month") int month, @Param("year") int year);


    @Query("SELECT p.libelle, FUNCTION('MONTH', i.date) as month, COUNT(i) " +
            "FROM Intervention i " +
            "JOIN i.probleme p " +
            "GROUP BY p.libelle, FUNCTION('MONTH', i.date)")
    List<Object[]> countInterventionsByProblemAndMonth();

    List<Intervention> findInterventionsByProjet_IdAndAeroport_Id(Long projetId,Long aeroportId);

}
