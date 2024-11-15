package ma.ismail.spring_sec_jwt.services;

import ma.ismail.spring_sec_jwt.entites.AppRole;
import ma.ismail.spring_sec_jwt.entites.AppUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppUser updateUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUsername(String username,List<AppRole> roles);
    AppUser loadUserByUsername(String username);
    List<AppUser> listUsers();
    List<AppUser> getUsersByRole(String role);
    public boolean verifyCredentials(String username, String password);
    List<AppUser> findByRoles_RoleNameAndAeroport1_AeroportName(String role,String username);
    public void importAppUserData(MultipartFile file) throws IOException;
}