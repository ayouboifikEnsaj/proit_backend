package ma.ismail.spring_sec_jwt.entites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Intervention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Status status;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Temporal(TemporalType.TIME)
    private Date heureDebut;

    @Temporal(TemporalType.TIME)
    private Date heureFin;
    @ManyToOne

    private Compagnie compagnie;
    @ManyToOne

    private AppUser appUser;
    @ManyToOne

    private Comptoire comptoire;
    @ManyToOne

    private Equipment equipment;
    @ManyToOne

    private Solution solution;
    @ManyToOne

    private Probleme probleme;
    @ManyToOne
    private Aeroport aeroport;
    @ManyToOne
    private Projet projet;
}
