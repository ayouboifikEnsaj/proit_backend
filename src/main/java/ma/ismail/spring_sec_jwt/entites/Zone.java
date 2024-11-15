package ma.ismail.spring_sec_jwt.entites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor@NoArgsConstructor@Entity
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String zoneName;
    @OneToMany(mappedBy = "zone",cascade = CascadeType.REMOVE,orphanRemoval = true)
    @JsonIgnore
    private List<Comptoire> comptoires=new ArrayList<>();


}
