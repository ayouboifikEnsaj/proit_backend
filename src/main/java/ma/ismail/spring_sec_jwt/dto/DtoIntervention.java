package ma.ismail.spring_sec_jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ismail.spring_sec_jwt.entites.Intervention;
import ma.ismail.spring_sec_jwt.entites.Status;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoIntervention {
    private Long id;
    private Date date;
    private Date heureDebut;
    private Date heureFin;
    private Long compagnie;
    private String appUser;
    private Long comptoire;
    private Long equipment;
    private Long solution;
    private Long probleme;
    private Long aeroport;
    private Long projet;
}
