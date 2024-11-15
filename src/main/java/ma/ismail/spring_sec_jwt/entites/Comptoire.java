package ma.ismail.spring_sec_jwt.entites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comptoire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comptoireName;
    @ManyToOne
    private Zone zone;

    @OneToMany(mappedBy = "comptoire")
    @JsonIgnore
    List<Intervention> interventions=new ArrayList<>();
}
