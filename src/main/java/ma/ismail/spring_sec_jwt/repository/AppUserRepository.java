package ma.ismail.spring_sec_jwt.repository;


import ma.ismail.spring_sec_jwt.entites.Aeroport;
import ma.ismail.spring_sec_jwt.entites.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    AppUser findByUsername(String username);

    AppUser findAppUserById(Long id);
    List<AppUser> findByRoles_RoleName(String role);
    List<AppUser> findByRoles_RoleNameAndAeroport_AeroportName(String role,String aeroport);

}
