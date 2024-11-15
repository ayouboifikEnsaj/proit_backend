package ma.ismail.spring_sec_jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetDTO {
    private Long eqipmentId;
    private String projetName;
}
