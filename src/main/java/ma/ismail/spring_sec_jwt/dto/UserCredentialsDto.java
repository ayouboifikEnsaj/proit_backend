package ma.ismail.spring_sec_jwt.dto;

import lombok.Data;

@Data
public class UserCredentialsDto {
    private String username;
    private String password;
}
