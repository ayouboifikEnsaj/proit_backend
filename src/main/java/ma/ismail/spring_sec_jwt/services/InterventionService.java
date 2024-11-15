package ma.ismail.spring_sec_jwt.services;

import ma.ismail.spring_sec_jwt.dto.DTOInterventionn;
import ma.ismail.spring_sec_jwt.dto.DtoIntervention;
import ma.ismail.spring_sec_jwt.entites.Intervention;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface InterventionService {
    Intervention saveIntervention(DtoIntervention intervention);
    Intervention getInterventionById(Long id);
    List<Intervention> getAllInterventions();
    List<Intervention> getAllInterventionsHelp(Long aLong);
    Intervention updateIntervention(DtoIntervention intervention);
    boolean deleteIntervention(Long id);
    Intervention finIntervention(DtoIntervention dtoIntervention);
    List<Intervention> interventionByUser(String username);
    List<Intervention> interventionsByAeroport(String aeroport);
    Intervention updateInterventionUser(DtoIntervention dtoIntervention);
    Intervention updateInterventionUserHelp(DTOInterventionn dtoIntervention);
    Intervention EndOfIntervention(DtoIntervention dtoIntervention);
    public Intervention saveInterventionnn(DTOInterventionn dtoIntervention);
    Map<String, Long> getInterventionsByProbleme();
    Map<String, Integer> getInterventionsCountByProblemType();
    public Map<String, Double> calculateTBF(Long equipmentId, int month) ;
    public Map<String, Double> getInterventionPercentages(Long comptoireId, int month, int year) ;
    public Map<String, Object> getInterventionsByProblemAndMonth();
    public void importInterventionData(MultipartFile file) throws IOException;

    double calculateTbfByProjetAndYear(Long projetId, int month, int year, Long aeroportId);
    List<Intervention> interventionsByAirport(Long aeroportId);

    List<Intervention> interventionsByAirportAndProjet(Long aeroportId);
    List<Intervention> interventionsByAirportAndProjetEGte(Long aeroportId);
}
