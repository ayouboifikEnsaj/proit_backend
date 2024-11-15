package ma.ismail.spring_sec_jwt.entites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Aeroport {
    //Il faux changer la structure car :
    // si un Aeroport est supprimé, tous les AppUser associés seront également supprimés.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String aeroportName;
    @OneToMany(mappedBy = "aeroport",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<AppUser> users=new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Compagnie> compagnies=new ArrayList<>();

}
