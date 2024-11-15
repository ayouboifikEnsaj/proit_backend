package ma.ismail.spring_sec_jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AeroportDTO {
    private Long id;
    private String aeroportName;
    private List<String> compagnieNames;

    // Getters and Setters
}
