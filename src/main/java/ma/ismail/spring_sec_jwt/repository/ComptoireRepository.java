package ma.ismail.spring_sec_jwt.repository;

import ma.ismail.spring_sec_jwt.entites.Comptoire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComptoireRepository extends JpaRepository<Comptoire,Long> {
    Comptoire findComptoireByComptoireName(String name);
    Comptoire findComptoireById(Long id);

}
