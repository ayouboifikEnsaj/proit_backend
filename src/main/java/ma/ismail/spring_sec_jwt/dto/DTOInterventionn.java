package ma.ismail.spring_sec_jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ismail.spring_sec_jwt.entites.Status;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOInterventionn {


        private Long id;
        private Date date;
        private Date heureDebut;
        private Status status;
        private Date heureFin;
        private Long compagnie;
        private Long appUser;
        private Long comptoire;
        private Long equipment;
        private Long solution;
        private Long probleme;
        private Long aeroport;
        private Long projet;
}
