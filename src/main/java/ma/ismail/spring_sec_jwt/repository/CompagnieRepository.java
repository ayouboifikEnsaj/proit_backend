package ma.ismail.spring_sec_jwt.repository;

import ma.ismail.spring_sec_jwt.entites.Compagnie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompagnieRepository extends JpaRepository<Compagnie,Long> {
    Compagnie findCompagnieById(Long id);
    Compagnie findCompagnieByCompagnieName(String compagnieName);

    String getCompagnieNameById(Long key);
}
